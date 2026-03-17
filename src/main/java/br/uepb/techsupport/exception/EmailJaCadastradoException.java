package br.uepb.techsupport.exception;

public class EmailJaCadastradoException extends TechSupportException {

    public EmailJaCadastradoException(String email) {
        super("O e-mail '" + email + "' já está cadastrado no sistema.");
    }

    public EmailJaCadastradoException() {
        super("Este endereço de e-mail já está sendo utilizado por outro usuário.");
    }
}
