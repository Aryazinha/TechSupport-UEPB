package br.uepb.techsupport.exception;

public abstract class TechSupportException extends RuntimeException {

    public TechSupportException(String mensagem) {
        super(mensagem);
    }
}
