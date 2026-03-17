package br.uepb.techsupport.exception;

public class DescricaoInvalidaException extends TechSupportException {

    public DescricaoInvalidaException(String mensagem) {
        super(mensagem);
    }

    public DescricaoInvalidaException() {
        super("A descrição da Ordem de Serviço deve ter pelo menos 10 caracteres.");
    }
}
