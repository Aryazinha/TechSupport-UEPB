package br.uepb.techsupport.controller;

import br.uepb.techsupport.exception.CPFJaCadastradoException;
import br.uepb.techsupport.exception.EmailJaCadastradoException;
import br.uepb.techsupport.exception.FormatoInvalidoException;
import br.uepb.techsupport.exception.TelefoneJaCadastradoException;
import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.service.ClienteService;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.NavUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ClienteCadastroController {

    @FXML private TextField txtNome, txtEmail, txtCpf, txtTelefone;
    @FXML private PasswordField txtSenha;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    private void handleCadastrar() {
        if (camposVazios()) {
            AlertUtils.mostrarAlerta("Atenção", "Preencha todos os campos obrigatórios!", AlertType.WARNING);
            return;
        }

        try {
            Cliente novo = new Cliente();
            novo.setNome(txtNome.getText());
            novo.setEmail(txtEmail.getText());
            novo.setCpf(txtCpf.getText());
            novo.setTelefone(txtTelefone.getText());
            novo.setSenha(txtSenha.getText());

            clienteService.cadastrarCliente(novo);

            AlertUtils.mostrarAlerta("Sucesso", "Conta criada com sucesso!", AlertType.INFORMATION);
            handleVoltarLogin();

        } catch (CPFJaCadastradoException | EmailJaCadastradoException | FormatoInvalidoException |
                 TelefoneJaCadastradoException e) {
            AlertUtils.mostrarAlerta("Erro de Cadastro", e.getMessage(), AlertType.ERROR);
        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro Crítico", "Ocorreu um erro inesperado no servidor.", AlertType.ERROR);
            e.printStackTrace();
        }
    }


    private boolean camposVazios() {
        return txtNome.getText().trim().isEmpty() ||
                txtCpf.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                txtSenha.getText().trim().isEmpty();
    }

    @FXML
    private void handleVoltarLogin() {
        NavUtils.navegarPara(txtNome, "/br/uepb/techsupport/view/login.fxml", "TechSupport UEPB - Login");
    }
}