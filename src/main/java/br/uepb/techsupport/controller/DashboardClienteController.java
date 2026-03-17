package br.uepb.techsupport.controller;

import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DashboardClienteController {

    @FXML private Label lblBoasVindas, lblAtivos, lblResolvidos, lblTotal;
    @FXML private Button btnMeusChamados, btnNovoTicket;
    @FXML private TableView<OrdemServico> tblChamados;
    @FXML private TableColumn<OrdemServico, String> colId, colPrioridade, colDescricao, colStatus, colData;

    private final OSService osService = new OSService();

    @FXML
    public void initialize() {
        Cliente clienteLogado = (Cliente) SessaoUsuario.getUsuarioLogado();
        if (clienteLogado != null) {
            lblBoasVindas.setText("Bem-vindo(a), " + clienteLogado.getNome() + "!");
        }

        configurarTabela();
        carregarDados();

        tblChamados.getColumns().forEach(c -> c.setReorderable(false));

        tblChamados.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblChamados.getSelectionModel().getSelectedItem() != null) {
                OrdemServico selecionado = tblChamados.getSelectionModel().getSelectedItem();
                ModalUtils.abrirDetalhesChamado(selecionado, null);
            }
        });
    }

    private void configurarTabela() {
        colId.setCellValueFactory(d -> new SimpleStringProperty("#" + d.getValue().getId()));
        colPrioridade.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrioridade() != null ? d.getValue().getPrioridade().toString() : "-"));
        colDescricao.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescricao()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus().toString()));

        colData.setCellValueFactory(d -> new SimpleStringProperty(
                TabelaUtils.formatarData(d.getValue().getDataAbertura())
        ));

        TabelaUtils.formatarColunaPrioridade(colPrioridade);
        TabelaUtils.formatarColunaStatus(colStatus);
    }

    public void carregarDados() {
        if (SessaoUsuario.getUsuarioLogado() instanceof Cliente cliente) {
            List<OrdemServico> meusChamados = osService.buscarPorCliente(cliente);
            Map<String, Long> estatisticas = osService.obterEstatisticasCliente(meusChamados);

            tblChamados.setItems(FXCollections.observableArrayList(meusChamados));

            lblAtivos.setText(String.valueOf(estatisticas.get("ativos")));
            lblResolvidos.setText(String.valueOf(estatisticas.get("resolvidos")));
            lblTotal.setText(String.valueOf(estatisticas.get("total")));
        }
    }

    @FXML
    private void handleNovoChamado() {
        alternarEstiloBotoes(btnNovoTicket, btnMeusChamados);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/uepb/techsupport/view/abrir_chamado.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Novo Chamado - TechSupport UEPB");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            carregarDados();
        } catch (IOException e) {
            AlertUtils.mostrarAlerta("Erro", "Não foi possível abrir a tela de chamados.", Alert.AlertType.ERROR);
        } finally {
            alternarEstiloBotoes(btnMeusChamados, btnNovoTicket);
        }
    }

    @FXML
    public void handleLogout() {
        SessaoUsuario.encerrarSessao();
        NavUtils.navegarPara(lblBoasVindas, "/br/uepb/techsupport/view/login.fxml", "TechSupport - Login");
    }

    private void alternarEstiloBotoes(Button ativo, Button inativo) {
        ativo.getStyleClass().remove("menu-button");
        ativo.getStyleClass().add("menu-button-active");
        inativo.getStyleClass().remove("menu-button-active");
        inativo.getStyleClass().add("menu-button");
    }
}