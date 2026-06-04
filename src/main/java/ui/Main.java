package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView, 1000, 700);

        stage.setTitle("Catan");
        stage.setMinWidth(800);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setAlwaysOnTop(true);
        stage.show();

        Platform.runLater(() -> {
            stage.toFront();
            stage.requestFocus();
            stage.setAlwaysOnTop(false);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
