package br.uepb.techsupport.service;

import br.uepb.techsupport.model.system.OrdemServico;
import java.time.Duration;
import java.time.LocalDateTime;

public class EscalonamentoPadrao implements PoliticaEscalonamento {
    private static final double FATOR_ANTIGRAVIDADE = 0.1;
    // [REGRA DE NEGÓCIO: PRIORIDADE]
    // Implementa a lógica de peso baseada no Padrão Strategy para sugerir o próximo atendimento.
    @Override
    public double CalcularPeso(OrdemServico os) {
        double pesoBase = os.getPrioridade().getPeso();
        long minutosEspera = Duration.between(os.getDataAbertura(), LocalDateTime.now()).toMinutes();
        return pesoBase + (minutosEspera * FATOR_ANTIGRAVIDADE);
    }
}