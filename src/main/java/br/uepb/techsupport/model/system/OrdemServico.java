package br.uepb.techsupport.model.system;

import jakarta.persistence.*;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.entidade.Cliente;
import br.uepb.techsupport.model.enums.Prioridade;
import br.uepb.techsupport.model.enums.CategoriaOS;
import br.uepb.techsupport.model.enums.StatusOS;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordens_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Column(length = 1000)
    private String solucao;

    private LocalDateTime dataFechamento;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOS status = StatusOS.ABERTA;

    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(nullable = false)
    private Double valor;

    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaOS categoria;

    @Column(length = 150, nullable = false)
    private String localizacao;

    public OrdemServico() {}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public Prioridade getPrioridade() {return prioridade;}
    public void setPrioridade(Prioridade prioridade) {this.prioridade = prioridade;}

    public LocalDateTime getDataAbertura() {return dataAbertura;}
    public void setDataAbertura(LocalDateTime dataAbertura) {this.dataAbertura = dataAbertura;}

    public Tecnico getTecnicoResponsavel() {return tecnico;}
    public void setTecnicoResponsavel(Tecnico tecnico) {this.tecnico = tecnico;}

    public StatusOS getStatus() {return status;}
    public void setStatus(StatusOS status) {this.status = status;}

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }

    public String getSolucao() { return solucao; }
    public void setSolucao(String solucao) { this.solucao = solucao; }

    public LocalDateTime getDataFechamento() { return dataFechamento; }
    public void setDataFechamento(LocalDateTime dataFechamento) { this.dataFechamento = dataFechamento; }

    public CategoriaOS getCategoria() { return categoria; }
    public void setCategoria(CategoriaOS categoria) { this.categoria = categoria;}

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) {
        if (valor != null && valor < 0) {
            throw new IllegalArgumentException("O valor cobrado pelo serviço não pode ser negativo.");
        }
        this.valor = valor;
    }
}
