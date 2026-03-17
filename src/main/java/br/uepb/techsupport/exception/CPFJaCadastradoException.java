package br.uepb.techsupport.exception;

public class CPFJaCadastradoException extends TechSupportException {

    public CPFJaCadastradoException(String cpf) {
        super("O CPF '" + cpf + "' já está vinculado a outro colaborador no sistema.");
    }

    public CPFJaCadastradoException() {
        super("Este CPF já está cadastrado no sistema.");
    }
}