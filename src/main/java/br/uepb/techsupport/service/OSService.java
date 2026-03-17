package br.uepb.techsupport.service;

import br.uepb.techsupport.exception.DescricaoInvalidaException;
import br.uepb.techsupport.exception.FilaVaziaException;
import br.uepb.techsupport.exception.NivelInsuficienteException;
import br.uepb.techsupport.exception.StatusInvalidoException;
import br.uepb.techsupport.exception.TecnicoOcupadoException;
import br.uepb.techsupport.exception.RelatorioInvalidoException;
import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.NivelTecnico;
import br.uepb.techsupport.model.enums.Prioridade;
import br.uepb.techsupport.model.enums.StatusOS;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.repository.OrdemServiceRepository;
import br.uepb.techsupport.util.SessaoUsuario;
import br.uepb.techsupport.util.ValidadorDados;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class OSService {

    private final OrdemServiceRepository repository = new OrdemServiceRepository();

    private PoliticaEscalonamento politica = new EscalonamentoPadrao();

    public void abrirOrdem(OrdemServico os) {

        if (!ValidadorDados.isDescricaoValida(os.getDescricao())) {
            throw new DescricaoInvalidaException();
        }

        if (SessaoUsuario.getUsuarioLogado() instanceof Cliente cliente) {
            os.setCliente(cliente);
        }

        os.setValor(0.0);
        os.setStatus(StatusOS.ABERTA);
        os.setDataAbertura(LocalDateTime.now());

        repository.salvar(os);
    }

    public void atribuirTecnico(OrdemServico os, Tecnico tecnico) {
        // [REGRA DE NEGÓCIO: DISPONIBILIDADE]
        // Garante que o sistema não aloque tarefas para técnicos que já estão em atendimento.
        if (!tecnico.isDisponivel()) {
            throw new TecnicoOcupadoException();
        }

        if (tecnico.getNivel() == NivelTecnico.JUNIOR && os.getPrioridade() == Prioridade.ALTA) {
            throw new NivelInsuficienteException();
        }

        os.setTecnicoResponsavel(tecnico);
        os.setStatus(StatusOS.EM_ANDAMENTO);
        tecnico.setDisponivel(false);

        repository.salvar(os);
        repository.atualizarTecnico(tecnico);
    }

    public void finalizarOrdem(OrdemServico os, String relatorioSolucao) throws RelatorioInvalidoException {

        if (relatorioSolucao == null || relatorioSolucao.trim().length() < 10) {
            throw new RelatorioInvalidoException("A solução descrita é muito curta. Descreva o que foi feito com pelo menos 10 caracteres.");
        }

        if (os.getTecnicoResponsavel() == null) {
            throw new StatusInvalidoException("Não é possível finalizar uma ordem sem técnico atribuído.");
        }

        if (os.getStatus() == StatusOS.CONCLUIDA) {
            throw new StatusInvalidoException("Essa ordem já foi finalizada.");
        }

        os.setStatus(StatusOS.CONCLUIDA);
        os.setSolucao(relatorioSolucao);
        os.setDataFechamento(LocalDateTime.now());

        Tecnico t = os.getTecnicoResponsavel();
        t.setQuantidadeOsResolvidas(t.getQuantidadeOsResolvidas() + 1);
        t.setDisponivel(true);

        repository.salvar(os);
        repository.atualizarTecnico(t);
    }

    public Double calcularPesoOrdem(OrdemServico os) {
        return politica.CalcularPeso(os);
    }

    public PriorityQueue<OrdemServico> construirFilaPrioridade() {
        // [REGRA DE NEGÓCIO: PRIORIDADE]
        // Utiliza PriorityQueue (Heap) para organizar os chamados pelo peso calculado dinamicamente.
        PriorityQueue<OrdemServico> fila = new PriorityQueue<>(
                (os1, os2) -> Double.compare(calcularPesoOrdem(os2), calcularPesoOrdem(os1))
        );

        repository.buscarTodas().stream()
                .filter(os -> os.getStatus() == StatusOS.ABERTA)
                .forEach(fila::offer);

        return fila;
    }

    public List<OrdemServico> listarFilaPrioridade() {
        PriorityQueue<OrdemServico> filaPrio = construirFilaPrioridade();
        List<OrdemServico> listaOrdenada = new java.util.ArrayList<>();

        while (!filaPrio.isEmpty()) {
            listaOrdenada.add(filaPrio.poll());
        }

        return listaOrdenada;
    }

    public OrdemServico chamarProximoChamado(Tecnico tecnico) {
        PriorityQueue<OrdemServico> fila = construirFilaPrioridade();

        if (fila.isEmpty()) {
            throw new FilaVaziaException("Não há chamados pendentes na fila no momento.");
        }

        // [REGRA DE NEGÓCIO: COMPETÊNCIA]
        // Impede que técnicos Nível JUNIOR assumam ordens de complexidade ALTA.
        if (tecnico.getNivel() == NivelTecnico.JUNIOR) {
            while (!fila.isEmpty()) {
                OrdemServico os = fila.poll();
                if (os.getPrioridade() != Prioridade.ALTA) {
                    return os;
                }
            }
            throw new FilaVaziaException("Não há chamados de nível compatível disponíveis.");
        }

        return fila.poll();
    }

    public Map<String, Long> obterEstatisticasCliente(List<OrdemServico> chamados) {
        Map<String, Long> stats = new java.util.HashMap<>();
        stats.put("total", (long) chamados.size());

        long ativos = chamados.stream()
                .filter(os -> os.getStatus() == StatusOS.ABERTA || os.getStatus() == StatusOS.EM_ANDAMENTO)
                .count();
        stats.put("ativos", ativos);

        long resolvidos = chamados.stream()
                .filter(os -> os.getStatus() == StatusOS.CONCLUIDA)
                .count();
        stats.put("resolvidos", resolvidos);

        return stats;
    }

    public Map<String, Long> obterResumoDashboard() {
        return repository.buscarEstatisticas();
    }

    public void realizarTriagem(OrdemServico os, Prioridade novaPrio, Tecnico logado) {
        // [REGRA DE NEGÓCIO: COMPETÊNCIA]
        // Impede que técnicos Nível JUNIOR assumam ordens de complexidade ALTA.
        if (logado.getNivel() == NivelTecnico.JUNIOR && (os.getPrioridade() == Prioridade.ALTA || novaPrio == Prioridade.ALTA)) {
            throw new NivelInsuficienteException("Técnicos JÚNIOR não podem manipular chamados de prioridade ALTA.");
        }
        if (novaPrio != null) {
            os.setPrioridade(novaPrio);
            repository.atualizar(os);
        }
    }

    public List<OrdemServico> buscarTodas() { return repository.buscarTodas(); }

    public List<OrdemServico> buscarPorCliente(Cliente cliente) { return repository.buscarPorCliente(cliente); }

    public List<OrdemServico> buscarPorTecnico(Tecnico tecnico) { return repository.buscarPorTecnico(tecnico); }
}