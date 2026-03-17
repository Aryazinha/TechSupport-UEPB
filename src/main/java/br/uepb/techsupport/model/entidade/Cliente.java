package br.uepb.techsupport.model.entidade;

import jakarta.persistence.*;

@Entity
public class Cliente extends Pessoa {

    @Column(unique = true, nullable = true)
    private String telefone;

    public Cliente() {}

    public Cliente(String nome, String email, String cpf, String senha, String telefone) {
        super(nome, email, cpf, senha);
        this.telefone = telefone;
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
