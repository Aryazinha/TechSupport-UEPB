package br.uepb.techsupport.model.enums;

public enum Prioridade {
    BAIXA(1),
    MEDIA(2),
    ALTA(3);

    private final int peso;

    Prioridade(int peso) {
        this.peso = peso;
    }

    public int getPeso() { return peso; }
}