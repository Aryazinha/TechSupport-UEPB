package br.uepb.techsupport.exception;

public class FilaCheiaException extends TechSupportException {

    public FilaCheiaException(int limite) {
        super("Limite da fila atingido! Não é possível atribuir mais de " + limite + " chamados simultâneos.");
    }

    public FilaCheiaException() {
        super("A fila de atendimento deste técnico está cheia.");
    }
}