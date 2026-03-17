package br.uepb.techsupport.controller;

import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.exception.RelatorioInvalidoException;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.SessaoUsuario;
import br.uepb.techsupport.util.WindowUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ConcluirChamadoController {

    @FXML private Label lblTitulo;
    @FXML private TextArea txtSolucao;
    @FXML private TextField txtPreco;

    private OrdemServico ordemServico;
    private DashboardController dashboardPai;
    private final OSService osService = new OSService();

    public void setDashboardPai(DashboardController pai) {
        this.dashboardPai = pai;
    }

    public void setOrdemServico(OrdemServico os) {
        this.ordemServico = os;
        lblTitulo.setText("Finalizar Chamado #" + os.getId());
    }

    @FXML
    private void handleConfirmar() {
        String solucao = txtSolucao.getText().trim();
        double valorCobrado = 0.0;
        String precoDigitado = txtPreco.getText().trim();

        if (!precoDigitado.isEmpty()) {
            try {
                valorCobrado = Double.parseDouble(precoDigitado.replace(",", "."));
            } catch (NumberFormatException ex) {
                AlertUtils.mostrarAlerta("Erro de Formato", "Digite um valor numérico válido (Ex: 50.00).", Alert.AlertType.ERROR);
                return;
            }
        }

        try {
            ordemServico.setValor(valorCobrado);

            osService.finalizarOrdem(ordemServico, solucao);

            Tecnico logado = (Tecnico) SessaoUsuario.getUsuarioLogado();
            logado.setDisponivel(true);
            logado.setQuantidadeOsResolvidas(logado.getQuantidadeOsResolvidas() + 1);

            AlertUtils.mostrarAlerta("Sucesso", "Chamado finalizado com sucesso!", Alert.AlertType.INFORMATION);

            if (dashboardPai != null) {
                dashboardPai.atualizarInterface();
            }
            handleCancelar();

        } catch (RelatorioInvalidoException e) {
            AlertUtils.mostrarAlerta("Aviso", e.getMessage(), Alert.AlertType.WARNING);

        } catch (RuntimeException ex) {
            AlertUtils.mostrarAlerta("Erro Interno", "Falha ao encerrar no banco de dados.", Alert.AlertType.ERROR);
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        WindowUtils.fecharJanela(txtSolucao);
    }
}