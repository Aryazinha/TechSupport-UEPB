package br.uepb.techsupport.exception;

public class FilaVaziaException extends RuntimeException {
    public FilaVaziaException() {
        super("Não há ordens de serviço pendentes na fila de espera.");
    }

    public FilaVaziaException(String mensagem) {
        super(mensagem);
    }

}
