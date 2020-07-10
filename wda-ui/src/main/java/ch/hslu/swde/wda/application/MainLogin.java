package ch.hslu.swde.wda.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainLogin extends Application {

    private static final Logger LOG = LogManager.getLogger(MainLogin.class);

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("WeatherApp SWDE");

            AnchorPane root = FXMLLoader.load(getClass().getResource("view/LoginView.fxml"));

            Scene scene = new Scene(root, 850, 700);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            LOG.info("Error: ");
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
