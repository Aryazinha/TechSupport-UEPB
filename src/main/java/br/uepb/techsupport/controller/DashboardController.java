package br.uepb.techsupport.controller;

import br.uepb.techsupport.exception.FilaVaziaException;
import br.uepb.techsupport.exception.NivelInsuficienteException;
import br.uepb.techsupport.model.entidade.Pessoa;
import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.NivelTecnico;
import br.uepb.techsupport.model.enums.StatusOS;
import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DashboardController {

    private final OSService osService = new OSService();

    @FXML private Label lblBoasVindas, labelTotalAbertas, labelTecnicosDisponiveis,
            lblChamadosConcluidos, lblTotalGeral, lblEficiencia;

    @FXML private Button btnDash, btnTickets, btnEquipe, btnConcluirChamado;

    @FXML private TableView<OrdemServico> tblChamados;
    @FXML private TableColumn<OrdemServico, String> colDescricao, colPrioridade, colStatus, colData;

    @FXML
    public void initialize() {
        Pessoa usuario = SessaoUsuario.getUsuarioLogado();
        if (usuario != null) {
            lblBoasVindas.setText("Olá, " + usuario.getNome() + "!");
        }

        tblChamados.getColumns().forEach(c -> c.setReorderable(false));

        if (btnDash != null) alternarEstiloMenu(btnDash);

        configurarTabela();
        configurarListeners();
        atualizarInterface();
    }

    private void alternarEstiloMenu(Button selecionado) {
        List<Button> botoes = Arrays.asList(btnDash, btnTickets, btnEquipe);
        for (Button b : botoes) {
            if (b != null) {
                b.getStyleClass().remove("menu-button-active");
                b.getStyleClass().add("menu-button");
            }
        }
        if (selecionado != null) {
            selecionado.getStyleClass().remove("menu-button");
            selecionado.getStyleClass().add("menu-button-active");
        }
    }

    private void configurarListeners() {
        tblChamados.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            boolean podeConcluir = (novo != null && novo.getStatus() == StatusOS.EM_ANDAMENTO);
            if (btnConcluirChamado != null) btnConcluirChamado.setDisable(!podeConcluir);
        });

        tblChamados.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && tblChamados.getSelectionModel().getSelectedItem() != null) {
                OrdemServico selecionado = tblChamados.getSelectionModel().getSelectedItem();
                ModalUtils.abrirDetalhesChamado(selecionado, this);
            }
        });
    }

    public void atualizarInterface() {
        try {
            if (SessaoUsuario.getUsuarioLogado() instanceof Tecnico logado) {
                tblChamados.setItems(FXCollections.observableArrayList(osService.buscarPorTecnico(logado)));
                aplicarRestricoesDeAcesso(logado);
            }
            atualizarEstatisticas();
        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro", "Falha ao atualizar dados do servidor.", AlertType.ERROR);
        }
    }

    private void aplicarRestricoesDeAcesso(Tecnico logado) {
        boolean isSenior = logado.getNivel() == NivelTecnico.SENIOR;
        if (btnEquipe != null) {
            btnEquipe.setVisible(isSenior);
            btnEquipe.setManaged(isSenior);
        }
    }

    private void configurarTabela() {
        colDescricao.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescricao()));
        colPrioridade.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrioridade().toString()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatus().toString()));

        colData.setCellValueFactory(d -> new SimpleStringProperty(
                TabelaUtils.formatarData(d.getValue().getDataAbertura())
        ));

        tblChamados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TabelaUtils.formatarColunaPrioridade(colPrioridade);
        TabelaUtils.formatarColunaStatus(colStatus);
    }

    private void atualizarEstatisticas() {
        Map<String, Long> stats = osService.obterResumoDashboard();
        labelTotalAbertas.setText(String.valueOf(stats.getOrDefault("abertas", 0L)));
        lblChamadosConcluidos.setText(String.valueOf(stats.getOrDefault("concluidas", 0L)));
        labelTecnicosDisponiveis.setText(String.valueOf(stats.getOrDefault("tecnicos", 0L)));
        lblTotalGeral.setText(String.valueOf(stats.getOrDefault("total", 0L)));

        long total = stats.getOrDefault("total", 0L);
        double eficiencia = total > 0 ? ((double) stats.getOrDefault("concluidas", 0L) / total) * 100 : 0;
        lblEficiencia.setText(String.format("%.1f%%", eficiencia));
    }

    @FXML
    private void handleCardPendenteClick(javafx.scene.input.MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (osService.listarFilaPrioridade().isEmpty()) {
                AlertUtils.mostrarAlerta("Fila Vazia", "Não há chamados pendentes no momento!", AlertType.INFORMATION);
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/uepb/techsupport/view/fila_triagem.fxml"));
                Parent root = loader.load();
                FilaTriagemController controller = loader.getController();
                controller.setDashboardPai(this);

                Stage stage = new Stage();
                stage.setTitle("Fila de Triagem Inteligente");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                AlertUtils.mostrarAlerta("Erro", "Não foi possível carregar a tela da fila.", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handlePuxarChamado() {
        try {
            Tecnico logado = (Tecnico) SessaoUsuario.getUsuarioLogado();
            OrdemServico proximo = osService.chamarProximoChamado(logado);
            ModalUtils.abrirDetalhesChamado(proximo, this);
        } catch (FilaVaziaException | NivelInsuficienteException e) {
            AlertUtils.mostrarAlerta("Aviso", e.getMessage(), AlertType.INFORMATION);
        } catch (RuntimeException e) {
            AlertUtils.mostrarAlerta("Erro", "Falha ao puxar próximo chamado.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleConcluirChamado() {
        OrdemServico selecionada = tblChamados.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/br/uepb/techsupport/view/concluir_chamado.fxml"));
                Parent root = loader.load();
                ConcluirChamadoController controller = loader.getController();
                controller.setOrdemServico(selecionada);
                controller.setDashboardPai(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException e) {
                AlertUtils.mostrarAlerta("Erro", "Falha ao carregar a tela de conclusão.", AlertType.ERROR);
            }
        }
    }

    @FXML
    public void handleLogout() {
        SessaoUsuario.encerrarSessao();
        NavUtils.navegarPara(lblBoasVindas, "/br/uepb/techsupport/view/login.fxml", "TechSupport - Login");
    }

    @FXML
    private void handleEquipe() {
        alternarEstiloMenu(btnEquipe);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/uepb/techsupport/view/cadastro_tecnico.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Equipe - UEPB");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            atualizarInterface();
        } catch (IOException e) {
            AlertUtils.mostrarAlerta("Erro", "Não foi possível carregar a tela de cadastro.", AlertType.ERROR);
        } finally {
            alternarEstiloMenu(btnDash);
        }
    }

    @FXML
    private void handleAbrirTickets() {
        alternarEstiloMenu(btnTickets);
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/br/uepb/techsupport/view/tickets.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Histórico e Pesquisa de Tickets - UEPB");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            AlertUtils.mostrarAlerta("Erro", "Não foi possível carregar a tela de Histórico de Tickets.", AlertType.ERROR);
        } finally {
            alternarEstiloMenu(btnDash);
        }
    }
}