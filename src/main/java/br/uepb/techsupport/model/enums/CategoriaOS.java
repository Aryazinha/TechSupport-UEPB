package br.uepb.techsupport.model.enums;

public enum CategoriaOS {
    SISTEMAS_ACADEMICOS("Sistemas Institucionais (Controle Acadêmico, Portal, etc.)"),
    CONTAS_E_ACESSOS("Contas e Acessos (E-mail Institucional, Redefinição de Senha)"),
    REDE_E_CONECTIVIDADE("Rede e Conectividade (Wi-Fi, Cabeamento de Lab, Sem Internet)"),
    HARDWARE_E_MANUTENCAO("Hardware e Manutenção (PC não liga, Peças danificadas, Lentidão)"),
    SOFTWARES_E_AMBIENTES("Softwares e Ambientes (Instalação de IDEs Java/Python, Ferramentas, Office)"),
    AUDIOVISUAL_E_SALA("Audiovisual de Sala de Aula (Projetor/Datashow, Som, Cabos de vídeo)"),
    IMPRESSAO("Impressão e Digitalização (Falha na Impressora, Troca de Toner, Scanner)"),
    TELEFONIA("Telefonia e Comunicação (Problema em Ramais, Aparelho Mudo)"),
    OUTROS("Outros / Não Listado");

    private final String descricao;

    CategoriaOS(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
    @Override
    public String toString() { return descricao; }
}