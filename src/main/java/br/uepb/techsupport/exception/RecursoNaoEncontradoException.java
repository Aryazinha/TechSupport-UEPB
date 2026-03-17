package br.uepb.techsupport.exception;

public class RecursoNaoEncontradoException extends TechSupportException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RecursoNaoEncontradoException() {
        super("O recurso solicitado não foi encontrado no sistema.");
    }
}
