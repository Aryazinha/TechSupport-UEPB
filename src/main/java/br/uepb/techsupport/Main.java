package br.uepb.techsupport;

import br.uepb.techsupport.model.entidade.Tecnico;
import br.uepb.techsupport.model.enums.NivelTecnico;
import br.uepb.techsupport.repository.HibernateUtil;
import br.uepb.techsupport.util.SenhaUtil;
import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    private void cadastrarTecnicoMestre() {
        EntityManager em = HibernateUtil.getEntityManager();

        try {
            Long contagem = em.createQuery("SELECT COUNT(t) FROM Tecnico t", Long.class)
                    .getSingleResult();

            if (contagem == 0) {
                System.out.println("Banco vazio! Criando técnico sênior padrão...");

                em.getTransaction().begin();

                Tecnico admin = new Tecnico();
                admin.setNome("Administrador UEPB");
                admin.setEmail("admin@uepb.edu.br");
                admin.setCpf("000.000.000-00");

                admin.setSenha(SenhaUtil.hashSenha("admin123"));

                admin.setNivel(NivelTecnico.SENIOR);
                admin.setDisponivel(true);
                admin.setQuantidadeOsResolvidas(0);

                em.persist(admin);
                em.getTransaction().commit();

                System.out.println("✅ Técnico sênior mestre criado com sucesso!");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Erro ao tentar criar o técnico mestre: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        cadastrarTecnicoMestre();

        try {
            URL fxmlLocation = getClass().getResource("/br/uepb/techsupport/view/login.fxml");

            if (fxmlLocation == null) {
                throw new RuntimeException("Não foi possível encontrar o arquivo login.fxml no caminho especificado.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root, 500, 550);

            primaryStage.centerOnScreen();

            String cssPath = "/br/uepb/techsupport/view/style.css";
            URL cssLocation = getClass().getResource(cssPath);
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            }

            primaryStage.setTitle("TechSupport UEPB - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erro crítico ao carregar a interface:");
            e.printStackTrace();
        }
    }
}