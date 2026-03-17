package br.uepb.techsupport.controller;

import br.uepb.techsupport.exception.FilaVaziaException;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.StatusOS;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.SessaoUsuario;
import br.uepb.techsupport.util.TabelaUtils;
import br.uepb.techsupport.util.WindowUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DetalhesChamadoController {

    @FXML private Label lblCliente, lblCategoria, lblLocalizacao;
    @FXML private Label lblStatus, lblPrioridade;
    @FXML private TextArea txtDescricao;
    @FXML private Button btnAssumir;

    @FXML private VBox boxSolucao;
    @FXML private TextArea txtSolucao;
    @FXML private HBox boxValor;
    @FXML private Label lblValor;

    private OrdemServico ordemServico;
    private DashboardController dashboardPai;
    private final OSService osService = new OSService();

    public void setDashboardPai(DashboardController pai) {
        this.dashboardPai = pai;
    }

    public void setOrdemServico(OrdemServico os) {
        this.ordemServico = os;
        preencherDadosNaTela();
        verificarPermissaoAssumir();
    }

    private void preencherDadosNaTela() {
        lblCliente.setText((ordemServico.getCliente() != null) ? ordemServico.getCliente().getNome() : "Desconhecido");

        String categoria = (ordemServico.getCategoria() != null) ? ordemServico.getCategoria().getDescricao() : "Não especificada";
        lblCategoria.setText(categoria);

        String local = (ordemServico.getLocalizacao() != null && !ordemServico.getLocalizacao().isEmpty()) ? ordemServico.getLocalizacao() : "Não especificado";
        lblLocalizacao.setText(local);

        txtDescricao.setText(ordemServico.getDescricao());

        String statusStr = ordemServico.getStatus().toString();
        Label badgeStatusGerado = TabelaUtils.criarBadgeStatus(statusStr);
        lblStatus.setText(badgeStatusGerado.getText());
        lblStatus.getStyleClass().setAll(badgeStatusGerado.getStyleClass());

        String prioStr = (ordemServico.getPrioridade() != null) ? ordemServico.getPrioridade().toString() : "-";
        Label badgePrioGerado = TabelaUtils.criarBadgePrioridade(prioStr);
        lblPrioridade.setText(badgePrioGerado.getText());
        lblPrioridade.getStyleClass().setAll(badgePrioGerado.getStyleClass());

        boolean isConcluida = isChamadoConcluido();
        boolean isCliente = isUsuarioCliente();

        boxSolucao.setVisible(isConcluida);
        boxSolucao.setManaged(isConcluida);

        if (isConcluida && ordemServico.getSolucao() != null) {
            txtSolucao.setText(ordemServico.getSolucao());
        }

        boolean mostrarValor = isConcluida && isCliente;

        boxValor.setVisible(mostrarValor);
        boxValor.setManaged(mostrarValor);

        if (mostrarValor && ordemServico.getValor() != null) {
            lblValor.setText(String.format("R$ %.2f", ordemServico.getValor()));
        }
    }

    private void verificarPermissaoAssumir() {
        btnAssumir.setVisible(false);
        btnAssumir.setManaged(false);

        if (ordemServico.getStatus() == StatusOS.ABERTA) {
            try {
                Tecnico logado = (Tecnico) SessaoUsuario.getUsuarioLogado();
                OrdemServico proximoCorreto = osService.chamarProximoChamado(logado);

                if (proximoCorreto != null && ordemServico.getId().equals(proximoCorreto.getId())) {
                    btnAssumir.setVisible(true);
                    btnAssumir.setManaged(true);
                }
            } catch (FilaVaziaException e) {
            } catch (RuntimeException e) {
                System.err.println("Erro ao verificar permissão do botão Assumir: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleAssumir() {
        try {
            Tecnico logado = (Tecnico) SessaoUsuario.getUsuarioLogado();
            osService.atribuirTecnico(ordemServico, logado);

            AlertUtils.mostrarAlerta("Chamado Assumido", "Você assumiu o ticket #" + ordemServico.getId(), Alert.AlertType.INFORMATION);

            if (dashboardPai != null) {
                dashboardPai.atualizarInterface();
            }
            handleVoltar();

        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro", "Falha ao assumir o chamado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltar() {
        WindowUtils.fecharJanela(btnAssumir);
    }

    private boolean isChamadoConcluido() {
        return ordemServico != null &&
                ordemServico.getStatus() != null &&
                ordemServico.getStatus().name().equals("CONCLUIDA");
    }

    private boolean isUsuarioCliente() {
        return SessaoUsuario.getUsuarioLogado() instanceof br.uepb.techsupport.model.entidade.Cliente;
    }
}