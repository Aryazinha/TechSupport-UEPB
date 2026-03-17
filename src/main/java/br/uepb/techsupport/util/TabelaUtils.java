package br.uepb.techsupport.util;

import br.uepb.techsupport.model.system.OrdemServico;
import br.uepb.techsupport.service.OSService;
import br.uepb.techsupport.model.enums.NivelTecnico;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TabelaUtils {

    public static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String formatarData(LocalDateTime data) {
        return data != null ? data.format(FORMATADOR_DATA) : "-";
    }

    public static Label criarBadgePrioridade(String prioridade) {
        if (prioridade == null || prioridade.equals("-")) return new Label("-");

        String texto = prioridade.toUpperCase().equals("MEDIA") ? "MÉDIA" : prioridade.toUpperCase();
        Label badge = new Label(texto);
        badge.getStyleClass().add("badge-tabela");

        switch (prioridade.toUpperCase()) {
            case "ALTA": badge.getStyleClass().add("badge-alta"); break;
            case "MEDIA":
            case "MÉDIA": badge.getStyleClass().add("badge-media"); break;
            case "BAIXA": badge.getStyleClass().add("badge-baixa"); break;
            default: badge.getStyleClass().add("badge-aberta"); break;
        }
        return badge;
    }

    public static Label criarBadgeStatus(String status) {
        if (status == null) return new Label("-");

        Label badge = new Label(status.replace("_", " ").toUpperCase());
        badge.getStyleClass().add("badge-tabela");

        switch (status.toUpperCase()) {
            case "CONCLUIDA":
            case "CONCLUÍDA": badge.getStyleClass().add("badge-sucesso"); break;
            case "EM_ANDAMENTO":
            case "EM ANDAMENTO": badge.getStyleClass().add("badge-andamento"); break;
            case "ABERTA": badge.getStyleClass().add("badge-aberta"); break;
            default: badge.getStyleClass().add("badge-pendente"); break;
        }
        return badge;
    }

    public static void formatarColunaPrioridade(TableColumn<OrdemServico, String> coluna) {
        coluna.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox box = new HBox(criarBadgePrioridade(item));
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                    setText(null);
                }
            }
        });
    }

    public static void formatarColunaStatus(TableColumn<OrdemServico, String> coluna) {
        coluna.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox box = new HBox(criarBadgeStatus(item));
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                    setText(null);
                }
            }
        });
    }

    public static String formatarNivelEscrito(NivelTecnico nivel) {
        if (nivel == null) return "Selecione...";
        switch (nivel) {
            case JUNIOR: return "JÚNIOR";
            case SENIOR: return "SÊNIOR";
            case PLENO:  return "PLENO";
            default: return nivel.toString();
        }
    }

    public static void configurarCellFilaTriagem(ListView<OrdemServico> listView, OSService osService) {
        listView.setCellFactory(lv -> new ListCell<>() {

            private final HBox cellBox = new HBox(15);
            private final Label badge = new Label();
            private final Label info = new Label();
            private final Label pesoLabel = new Label();
            private final Region spacer = new Region();

            {
                cellBox.setAlignment(Pos.CENTER_LEFT);
                cellBox.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));

                badge.setMinWidth(75);
                badge.setAlignment(Pos.CENTER);
                badge.getStyleClass().add("badge-tabela");

                info.getStyleClass().add("info-ticket-triagem");
                HBox.setHgrow(spacer, Priority.ALWAYS);
                pesoLabel.getStyleClass().add("peso-ticket-triagem");

                cellBox.getChildren().addAll(badge, info, spacer, pesoLabel);
            }

            @Override
            protected void updateItem(OrdemServico item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    info.setText("Ticket #" + item.getId() + "  |  " + item.getDescricao());
                    pesoLabel.setText("Peso: " + String.format("%.1f", osService.calcularPesoOrdem(item)));

                    String prioStr = item.getPrioridade().name();
                    badge.setText(prioStr.equals("MEDIA") ? "MÉDIA" : prioStr);

                    badge.getStyleClass().removeAll("badge-alta", "badge-media", "badge-baixa", "badge-aberta");

                    switch (prioStr) {
                        case "ALTA": badge.getStyleClass().add("badge-alta"); break;
                        case "MEDIA": badge.getStyleClass().add("badge-media"); break;
                        case "BAIXA": badge.getStyleClass().add("badge-baixa"); break;
                        default: badge.getStyleClass().add("badge-aberta"); break;
                    }

                    setGraphic(cellBox);
                }
            }
        });
    }
}