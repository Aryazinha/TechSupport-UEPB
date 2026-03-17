package br.uepb.techsupport.controller;

import br.uepb.techsupport.exception.UsuarioNaoEncontradoException;
import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.model.entidade.Pessoa;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.service.LoginService;
import br.uepb.techsupport.util.AlertUtils;
import br.uepb.techsupport.util.NavUtils;
import br.uepb.techsupport.util.SessaoUsuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;

    private final LoginService loginService = new LoginService();

    @FXML
    public void initialize() {
        btnEntrar.setDefaultButton(true);
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String senha = txtSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            AlertUtils.mostrarAlerta("Aviso", "Por favor, preencha o e-mail e a senha.", AlertType.WARNING);
            return;
        }

        try {
            boolean loginComSucesso = loginService.realizarLogin(email, senha);

            if (loginComSucesso) {
                Pessoa usuarioLogado = SessaoUsuario.getUsuarioLogado();
                System.out.println("✅ Login realizado: " + usuarioLogado.getNome());

                if (usuarioLogado instanceof Tecnico) {
                    NavUtils.navegarPara(btnEntrar, "/br/uepb/techsupport/view/dashboard.fxml", "TechSupport UEPB - Técnico");
                } else if (usuarioLogado instanceof Cliente) {
                    System.out.println("➡️ Redirecionando Cliente...");
                    NavUtils.navegarPara(btnEntrar, "/br/uepb/techsupport/view/dashboard_cliente.fxml", "TechSupport UEPB - Cliente");
                }
            } else {
                AlertUtils.mostrarAlerta("Erro de Autenticação", "Senha incorreta!", AlertType.ERROR);
            }
        } catch (UsuarioNaoEncontradoException e) {
            AlertUtils.mostrarAlerta("Erro de Autenticação", e.getMessage() , AlertType.ERROR);
        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro do Servidor", "Falha ao tentar conectar ao banco de dados.", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirTelaCadastro() {
        NavUtils.navegarPara(btnEntrar, "/br/uepb/techsupport/view/cadastro_cliente.fxml", "TechSupport UEPB - Novo Cliente");
    }
}