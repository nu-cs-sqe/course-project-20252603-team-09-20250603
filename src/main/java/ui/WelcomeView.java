package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WelcomeView extends VBox {
    private static final String STYLESHEET = "/ui/welcome-view.css";

    private final Label statusLabel = new Label();

    public WelcomeView(WelcomeController controller) {
        controller.setView(this);

        getStyleClass().add("welcome-view");
        getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
        setMinSize(800, 560);
        setPrefSize(1000, 700);
        setAlignment(Pos.CENTER);
        setSpacing(18);

        Label titleLabel = new Label("Welcome to Catan");
        titleLabel.getStyleClass().add("welcome-title");

        Label subtitleLabel = new Label("Game by: Team 09 - 20252603");
        subtitleLabel.getStyleClass().add("welcome-subtitle");

        Button startButton = new Button("Play Game");
        startButton.getStyleClass().add("primary-button");
        startButton.setOnAction(event -> controller.handleStartGame());

        Button rulesLink = new Button("View Rules");
        rulesLink.getStyleClass().add("secondary-button");
        rulesLink.setOnAction(event -> controller.handleRules());

        statusLabel.getStyleClass().add("status-label");

        getChildren().addAll(titleLabel, subtitleLabel, startButton, rulesLink, statusLabel);
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }
}
