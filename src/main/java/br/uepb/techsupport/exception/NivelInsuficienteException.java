package br.uepb.techsupport.exception;

public class NivelInsuficienteException extends TechSupportException {

    public NivelInsuficienteException() {
        super("Este técnico não possui o nível necessário para esta prioridade.");
    }

    public NivelInsuficienteException(String mensagem) {super(mensagem);}
}
