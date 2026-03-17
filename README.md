# 🖥️ TechSupport UEPB - Sistema Inteligente de Help Desk

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-000000?style=for-the-badge&logo=java&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)

O **TechSupport** é uma aplicação desktop robusta desenvolvida em Java para o gerenciamento e escalonamento de chamados de suporte técnico.

Diferente de sistemas de fila tradicionais (FIFO), este projeto implementa um **Motor de Triagem Inteligente**, utilizando a estrutura de dados **Priority Queue (Heap)** aliada ao padrão de projeto **Strategy**. O sistema calcula dinamicamente o "peso" de cada ordem de serviço baseado na sua prioridade e tempo de espera, garantindo eficiência e mitigando o problema de inanição (*starvation*) na fila de atendimento.

---

## ✨ Funcionalidades

### 👤 Para Clientes (Solicitantes)
* **Abertura de Chamados:** Interface intuitiva para relatar problemas, definindo categoria, localização e prioridade sugerida.
* **Dashboard Pessoal:** Acompanhamento em tempo real do status dos tickets ativos e histórico de chamados resolvidos.

### 🛠️ Para a Equipe Técnica
* **Fila Inteligente:** O sistema designa automaticamente o próximo chamado mais urgente compatível com o nível de experiência do técnico (Júnior, Pleno ou Sênior).
* **Painel Gerencial:** Visualização de estatísticas de eficiência, total de tickets pendentes e técnicos ativos.
* **Triagem de Chamados:** Reclassificação manual de prioridade para adequação de fluxo.
* **Encerramento de OS:** Inserção de relatório técnico e valor do serviço ao concluir o atendimento.

---

## 🏗️ Arquitetura e Padrões de Projeto

O projeto foi construído seguindo as melhores práticas de Engenharia de Software:
* **Arquitetura MVC (Model-View-Controller):** Separação estrita entre regras de negócio, interface gráfica e banco de dados.
* **Mapeamento Objeto-Relacional (ORM):** Utilização do **Hibernate/JPA** com herança do tipo *Single Table* no MySQL.
* **Design Patterns:** Aplicação do padrão **Strategy** para desacoplar a política de cálculo de escalonamento.
* **Segurança:** Hashes de senhas implementados com a biblioteca **BCrypt**.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java JDK 17+
* MySQL Server rodando localmente (porta 3306)
* IDE recomendada: IntelliJ IDEA ou Eclipse

### Configuração do Banco de Dados
1. O Hibernate está configurado com `update` para gerar as tabelas automaticamente.
2. Certifique-se de ter um schema chamado `db_techsupport` no seu MySQL ou deixe que a string de conexão o crie (dependendo da sua versão do MySQL).
3. Caso seu usuário e senha do MySQL sejam diferentes do padrão, edite o arquivo `src/main/resources/META-INF/persistence.xml`:
```xml
<property name="jakarta.persistence.jdbc.user" value="SEU_USUARIO" />
<property name="jakarta.persistence.jdbc.password" value="SUA_SENHA" />
```

## 👨‍💻 Autor

Desenvolvido por **Thales de Lima Dias** *Estudante de Ciência da Computação - Universidade Estadual da Paraíba (UEPB)*