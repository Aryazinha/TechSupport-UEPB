package br.uepb.techsupport.model.entidade;

import jakarta.persistence.*;
import br.uepb.techsupport.model.enums.NivelTecnico;

@Entity
public class Tecnico extends Pessoa {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private NivelTecnico nivel;

    private boolean disponivel = true;

    private int quantidadeOsResolvidas;

    public Tecnico() {}

    public Tecnico(String nome, String email, String cpf, String senha, NivelTecnico nivel) {
        super(nome, email, cpf, senha);
        this.nivel = nivel;
        this.disponivel = true;
        this.quantidadeOsResolvidas = 0;
    }

    public int getQuantidadeOsResolvidas() { return quantidadeOsResolvidas; }
    public void setQuantidadeOsResolvidas(int quantidadeOsResolvidas) { this.quantidadeOsResolvidas = quantidadeOsResolvidas; }

    public NivelTecnico getNivel() {return nivel;}
    public void setNivel(NivelTecnico nivel) {this.nivel = nivel;}

    public boolean isDisponivel() {return disponivel;}
    public void setDisponivel(boolean disponivel) {this.disponivel = disponivel;}
}
