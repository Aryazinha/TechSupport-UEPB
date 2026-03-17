package br.uepb.techsupport.controller;

import br.uepb.techsupport.exception.NivelInsuficienteException;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.Prioridade;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.ModalUtils;
import br.uepb.techsupport.util.SessaoUsuario;
import br.uepb.techsupport.util.TabelaUtils;
import br.uepb.techsupport.util.WindowUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class FilaTriagemController {

    @FXML private ListView<OrdemServico> listViewChamados;
    @FXML private ComboBox<Prioridade> cbNovaPrioridade;

    private final OSService osService = new OSService();
    private DashboardController dashboardPai;

    @FXML
    public void initialize() {
        cbNovaPrioridade.setItems(FXCollections.observableArrayList(Prioridade.values()));

        TabelaUtils.configurarCellFilaTriagem(listViewChamados, osService);

        carregarFila();

        listViewChamados.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                OrdemServico selecionado = listViewChamados.getSelectionModel().getSelectedItem();
                if (selecionado != null) {
                    ModalUtils.abrirDetalhesChamado(selecionado, dashboardPai);
                    carregarFila();
                }
            }
        });
    }

    public void setDashboardPai(DashboardController pai) {
        this.dashboardPai = pai;
    }

    private void carregarFila() {
        listViewChamados.setItems(FXCollections.observableArrayList(osService.listarFilaPrioridade()));
    }

    @FXML
    private void handleAtualizarPrioridade() {
        OrdemServico selecionado = listViewChamados.getSelectionModel().getSelectedItem();
        Prioridade novaPrio = cbNovaPrioridade.getValue();

        if (selecionado == null) {
            AlertUtils.mostrarAlerta("Aviso", "Selecione um chamado na lista.", AlertType.WARNING);
            return;
        }

        try {
            Tecnico logado = (Tecnico) SessaoUsuario.getUsuarioLogado();

            osService.realizarTriagem(selecionado, novaPrio, logado);

            Long idAtualizado = selecionado.getId();

            carregarFila();

            listViewChamados.refresh();

            if (dashboardPai != null) {
                dashboardPai.atualizarInterface();
            }

            listViewChamados.getItems().stream()
                    .filter(os -> os.getId().equals(idAtualizado))
                    .findFirst()
                    .ifPresent(os -> listViewChamados.getSelectionModel().select(os));

            AlertUtils.mostrarAlerta("Sucesso", "Triagem atualizada com sucesso!", AlertType.INFORMATION);

        } catch (NivelInsuficienteException e) {
            AlertUtils.mostrarAlerta("Acesso Negado", e.getMessage(), AlertType.ERROR);
        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro Técnico", "Não foi possível atualizar o banco de dados.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoltar() {
        WindowUtils.fecharJanela(cbNovaPrioridade);
    }
}