package br.uepb.techsupport.controller;

import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.NivelTecnico;
import br.uepb.techsupport.service.TecnicoService;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.TabelaUtils; // Importação adicionada
import br.uepb.techsupport.util.ValidadorDados;
import br.uepb.techsupport.util.WindowUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.util.Arrays;

public class TecnicoController {

    @FXML private TextField txtNome, txtCpf, txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private ComboBox<NivelTecnico> cbNivel;

    private final TecnicoService tecnicoService = new TecnicoService();

    @FXML
    public void initialize() {
        cbNivel.getItems().setAll(Arrays.asList(NivelTecnico.values()));

        cbNivel.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(NivelTecnico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : TabelaUtils.formatarNivelEscrito(item));
            }
        });

        cbNivel.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(NivelTecnico item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : TabelaUtils.formatarNivelEscrito(item));
            }
        });
    }

    @FXML
    private void handleSalvar() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String email = txtEmail.getText().trim();
        String senhaPura = txtSenha.getText();
        NivelTecnico nivel = cbNivel.getValue();

        if (camposVazios()) {
            AlertUtils.mostrarAlerta("Erro", "Todos os campos são obrigatórios!", AlertType.WARNING);
            return;
        }
        if (!ValidadorDados.isCpfValido(cpf)) {
            AlertUtils.mostrarAlerta("Erro", "CPF inválido.", AlertType.ERROR);
            return;
        }
        if (!ValidadorDados.isEmailValido(email)) {
            AlertUtils.mostrarAlerta("Erro", "E-mail inválido.", AlertType.ERROR);
            return;
        }

        try {
            Tecnico novo = new Tecnico();
            novo.setNome(nome);
            novo.setCpf(cpf);
            novo.setEmail(email);
            novo.setNivel(nivel);
            novo.setSenha(senhaPura);

            tecnicoService.cadastrarTecnico(novo);

            AlertUtils.mostrarAlerta("Sucesso!", "Técnico cadastrado com sucesso.", AlertType.INFORMATION);
            WindowUtils.fecharJanela(txtNome);

        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro", "Não foi possível cadastrar: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        limparCampos();
    }

    @FXML
    private void handleVoltar() {
        WindowUtils.fecharJanela(txtNome);
    }

    private boolean camposVazios() {
        return txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || txtSenha.getText().isEmpty() ||
                cbNivel.getValue() == null;
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtSenha.clear();
        cbNivel.getSelectionModel().clearSelection();
    }
}