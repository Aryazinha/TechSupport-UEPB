package br.uepb.techsupport.util;

import br.uepb.techsupport.controller.DashboardController;
import br.uepb.techsupport.controller.DetalhesChamadoController;
import br.uepb.techsupport.model.system.OrdemServico;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ModalUtils {

    public static void abrirDetalhesChamado(OrdemServico os, DashboardController dashboardPai) {
        try {
            FXMLLoader loader = new FXMLLoader(ModalUtils.class.getResource("/br/uepb/techsupport/view/detalhes_chamado.fxml"));
            Parent root = loader.load();

            DetalhesChamadoController controller = loader.getController();
            controller.setOrdemServico(os);

            if (dashboardPai != null) {
                controller.setDashboardPai(dashboardPai);
            }

            Stage stage = new Stage();
            stage.setTitle("Detalhes do Chamado #" + os.getId());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Não foi possível carregar a tela de detalhes.");
            alert.showAndWait();
        }
    }
}