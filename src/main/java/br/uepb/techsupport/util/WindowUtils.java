package br.uepb.techsupport.util;

import javafx.scene.Node;
import javafx.stage.Stage;

public class WindowUtils {
    public static void fecharJanela(Node node) {
        if (node != null && node.getScene() != null) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }
}