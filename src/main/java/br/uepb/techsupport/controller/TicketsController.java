package br.uepb.techsupport.controller;

import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.util.ModalUtils;
import br.uepb.techsupport.util.TabelaUtils;
import br.uepb.techsupport.util.WindowUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class TicketsController {

    @FXML private TextField txtPesquisa;
    @FXML private ComboBox<String> cbFiltroStatus, cbFiltroPrioridade;
    @FXML private TableView<OrdemServico> tblHistorico;
    @FXML private TableColumn<OrdemServico, String> colId, colCliente, colCategoria, colPrioridade, colDescricao, colStatus, colData;

    private final OSService osService = new OSService();

    private ObservableList<OrdemServico> dadosTabela;
    private FilteredList<OrdemServico> listaFiltrada;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDados();
        configurarFiltros();

        tblHistorico.getColumns().forEach(c -> c.setReorderable(false));

        tblHistorico.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblHistorico.getSelectionModel().getSelectedItem() != null) {
                OrdemServico selecionado = tblHistorico.getSelectionModel().getSelectedItem();
                ModalUtils.abrirDetalhesChamado(selecionado, null);
            }
        });
    }

    private void configurarTabela() {
        colId.setCellValueFactory(d -> new SimpleStringProperty("#" + d.getValue().getId()));
        colCliente.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCliente() != null ? d.getValue().getCliente().getNome() : "Desconhecido"));
        colCategoria.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategoria() != null ? d.getValue().getCategoria().toString() : "-"));
        colDescricao.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescricao()));
        colPrioridade.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrioridade() != null ? d.getValue().getPrioridade().toString() : "-"));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus().toString()));

        colData.setCellValueFactory(d -> new SimpleStringProperty(
                TabelaUtils.formatarData(d.getValue().getDataAbertura())
        ));

        TabelaUtils.formatarColunaPrioridade(colPrioridade);
        TabelaUtils.formatarColunaStatus(colStatus);
    }

    private void carregarDados() {
        List<OrdemServico> todasOS = osService.buscarTodas();
        dadosTabela = FXCollections.observableArrayList(todasOS);

        cbFiltroStatus.setItems(FXCollections.observableArrayList("TODOS", "ABERTA", "EM ANDAMENTO", "CONCLUIDA"));
        cbFiltroPrioridade.setItems(FXCollections.observableArrayList("TODAS", "BAIXA", "MÉDIA", "ALTA"));
    }

    private void configurarFiltros() {
        listaFiltrada = new FilteredList<>(dadosTabela, b -> true);

        txtPesquisa.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        cbFiltroStatus.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        cbFiltroPrioridade.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());

        SortedList<OrdemServico> listaOrdenada = new SortedList<>(listaFiltrada);
        listaOrdenada.comparatorProperty().bind(tblHistorico.comparatorProperty());
        tblHistorico.setItems(listaOrdenada);
    }

    private void aplicarFiltros() {
        listaFiltrada.setPredicate(os -> {
            String termoBusca = txtPesquisa.getText() == null ? "" : txtPesquisa.getText().toLowerCase();
            String status = cbFiltroStatus.getValue();
            String prioridade = cbFiltroPrioridade.getValue();

            boolean bateTexto = true;
            if (!termoBusca.isEmpty()) {
                String nomeCliente = os.getCliente() != null ? os.getCliente().getNome().toLowerCase() : "";
                String desc = os.getDescricao() != null ? os.getDescricao().toLowerCase() : "";
                String local = os.getLocalizacao() != null ? os.getLocalizacao().toLowerCase() : "";

                bateTexto = nomeCliente.contains(termoBusca) || desc.contains(termoBusca) || local.contains(termoBusca) || String.valueOf(os.getId()).equals(termoBusca);
            }

            boolean bateStatus = true;
            if (status != null && !status.equals("TODOS")) {
                String statusParaFiltro = status.replace(" ", "_");
                bateStatus = os.getStatus() != null && os.getStatus().name().equals(statusParaFiltro);
            }

            boolean batePrioridade = true;
            if (prioridade != null && !prioridade.equals("TODAS")) {
                String prioParaFiltro = prioridade.equals("MÉDIA") ? "MEDIA" : prioridade;
                batePrioridade = os.getPrioridade() != null && os.getPrioridade().name().equals(prioParaFiltro);
            }

            return bateTexto && bateStatus && batePrioridade;
        });
    }

    @FXML
    private void handleLimparFiltros() {
        txtPesquisa.clear();
        cbFiltroStatus.getSelectionModel().clearSelection();
        cbFiltroPrioridade.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleVoltar() {
        WindowUtils.fecharJanela(tblHistorico);
    }
}