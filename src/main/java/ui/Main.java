package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        StackPane rootPane = new StackPane();
        MainView mainView = new MainView();
        rootPane.getChildren().add(mainView);
        Scene scene = new Scene(rootPane, 1000, 700);

        stage.setTitle(I18n.text("app.title"));
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
