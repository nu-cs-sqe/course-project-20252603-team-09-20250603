package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SetupView extends VBox {
    private final SetupController controller;

    private final List<TextField> nameFields;
    private final Label statusLabel;

    public SetupView(SetupController controller) {
        this.controller = controller;
        controller.setView(this);
        this.nameFields = new ArrayList<>();

        setSpacing(15);
        setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            TextField nameField = new TextField();
            nameField.setPromptText("Player " + (i + 1) + " Name");
            nameField.setMaxWidth(200);
            nameFields.add(nameField);
            getChildren().add(nameField);
        }

        Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("primary-button");
        startButton.setOnAction(e -> controller.handleStartSetup(getEnteredNames()));

        Button backButton = new Button("Back to Create Game");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(e -> controller.handleBackToWelcome());

        statusLabel = new Label("Enter 3 or 4 player names.");

        getChildren().addAll(statusLabel, startButton, backButton);
    }

    private List<String> getEnteredNames() {
        List<String> names = new ArrayList<>();
        for (TextField field : nameFields) {
            names.add(field.getText());
        }
        return names;
    }

    public void setStatusMessage(String msg) {
        statusLabel.setText(msg);
    }
}
