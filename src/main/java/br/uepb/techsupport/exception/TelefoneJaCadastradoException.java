package br.uepb.techsupport.exception;

public class TelefoneJaCadastradoException extends RuntimeException {
    public TelefoneJaCadastradoException(String telefone) {
        super("O Telefone '" + telefone + "' já está vinculado a outro colaborador no sistema.");
    }

    public TelefoneJaCadastradoException() {
        super("Este Telefone já está cadastrado no sistema.");
    }
}
