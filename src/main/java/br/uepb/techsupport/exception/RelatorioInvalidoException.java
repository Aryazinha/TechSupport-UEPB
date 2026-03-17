package br.uepb.techsupport.exception;

public class RelatorioInvalidoException extends Exception {
    public RelatorioInvalidoException() {
        super("O relatório de solução é obrigatório e deve ser detalhado.");
    }

    public RelatorioInvalidoException(String mensagem) {
        super(mensagem);
    }
}
