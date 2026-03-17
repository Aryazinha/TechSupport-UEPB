package br.uepb.techsupport.exception;

public class TecnicoOcupadoException extends TechSupportException {

    public TecnicoOcupadoException() {
        super("O técnico selecionado já está em um atendimento.");
    }
}
