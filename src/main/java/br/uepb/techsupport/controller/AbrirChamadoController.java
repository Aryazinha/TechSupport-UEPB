package br.uepb.techsupport.controller;

import br.uepb.techsupport.exception.DescricaoInvalidaException;
import br.uepb.techsupport.model.enums.CategoriaOS;
import br.uepb.techsupport.model.enums.Prioridade;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.WindowUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AbrirChamadoController {

    @FXML private ComboBox<CategoriaOS> cbCategoria;
    @FXML private TextField txtLocalizacao;
    @FXML private TextArea txtDescricao;
    @FXML private ComboBox<Prioridade> cbPrioridade;

    private final OSService osService = new OSService();

    @FXML
    public void initialize() {
        cbCategoria.setItems(FXCollections.observableArrayList(CategoriaOS.values()));
        cbCategoria.getSelectionModel().selectFirst();

        cbPrioridade.setItems(FXCollections.observableArrayList(Prioridade.values()));
        cbPrioridade.getSelectionModel().select(Prioridade.BAIXA);
    }

    @FXML
    private void handleSalvar() {
        try {
            OrdemServico os = new OrdemServico();
            os.setCategoria(cbCategoria.getValue());
            os.setLocalizacao(txtLocalizacao.getText().trim());
            os.setDescricao(txtDescricao.getText().trim());
            os.setPrioridade(cbPrioridade.getValue());

            osService.abrirOrdem(os);

            AlertUtils.mostrarAlerta("Sucesso", "Seu chamado foi enviado para a equipe técnica!", Alert.AlertType.INFORMATION);
            WindowUtils.fecharJanela(txtDescricao);

        } catch (DescricaoInvalidaException e) {
            AlertUtils.mostrarAlerta("Descrição Curta", e.getMessage(), Alert.AlertType.WARNING);
        } catch (RuntimeException e) {
            e.printStackTrace();
            AlertUtils.mostrarAlerta("Erro", "Erro interno no sistema.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSair() {
        WindowUtils.fecharJanela(txtDescricao);
    }
}