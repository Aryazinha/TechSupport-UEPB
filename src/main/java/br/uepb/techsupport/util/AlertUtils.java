package br.uepb.techsupport.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import java.util.Objects;

public class AlertUtils {
    public static void mostrarAlerta(String titulo, String msg, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(msg);

        DialogPane dialogPane = alerta.getDialogPane();
        try {
            String cssPath = Objects.requireNonNull(AlertUtils.class.getResource("/br/uepb/techsupport/view/style.css")).toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            dialogPane.getStyleClass().add("alerta-dark");
        } catch (NullPointerException e) {
            System.err.println("Aviso: CSS não encontrado para o Alerta.");
        }

        alerta.showAndWait();
    }
}