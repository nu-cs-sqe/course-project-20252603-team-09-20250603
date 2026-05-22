package ui;

import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane {
    public MainView() {
        setPrefSize(1000, 700);
        setMinSize(800, 560);
        setStyle("-fx-background-color: #f2efe6;");
        getStyleClass().add("main-view");

        WelcomeController welcomeController = new WelcomeController();
        WelcomeView welcomeView = new WelcomeView(welcomeController);

        setCenter(welcomeView);
    }
}
