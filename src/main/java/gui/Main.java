package gui;

import java.io.IOException;

import chungus.app.Chungus;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Chungus using FXML.
 */
public class Main extends Application {

    private Chungus chungus = new Chungus("data/chungus.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            // Set window properties
            stage.setTitle("Chungus Task Manager");
            stage.setMinWidth(300);
            stage.setMinHeight(400);
            stage.setResizable(true);

            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setChungus(chungus); // inject the Chungus instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
