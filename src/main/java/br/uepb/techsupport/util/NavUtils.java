package br.uepb.techsupport.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;

public class NavUtils {
    public static void navegarPara(Node nodeOrigem, String caminhoFxml, String titulo) {
        try {
            Parent root = FXMLLoader.load(NavUtils.class.getResource(caminhoFxml));
            Stage stage = (Stage) nodeOrigem.getScene().getWindow();
            Scene scene = new Scene(root);

            String css = NavUtils.class.getResource("/br/uepb/techsupport/view/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.mostrarAlerta("Erro de Navegação", "Não foi possível carregar a tela: " + caminhoFxml, javafx.scene.control.Alert.AlertType.ERROR);
        }
    }
}