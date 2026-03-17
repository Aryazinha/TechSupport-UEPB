package br.uepb.techsupport.exception;

public class EmailException extends RuntimeException {

    public EmailException(String mensagem) {
        super(mensagem);
    }

    public EmailException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}