package ui;

import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlayerActionView extends VBox {
    private final List<Player> players;
    private PlayerActionController controller;
    private Button selectedInfraButton = null;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionView(List<Player> players) {
        this.players = new ArrayList<>(players);

        setPadding(new Insets(15));
        setSpacing(12);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("player-action-view");

        URL stylesheetUrl = getClass().getResource("/ui/player-action.css");
        if (stylesheetUrl != null) {
            getStylesheets().add(stylesheetUrl.toExternalForm());
        }
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setController(PlayerActionController controller) {
        this.controller = controller;
        renderActionMenu();
    }

    private Player getCurrentPlayer() {
        if (players.isEmpty() || controller == null) {
            return null;
        }

        Player currentPlayer = controller.getCurrentPlayer();
        return currentPlayer != null ? currentPlayer : players.get(0);
    }

    public void renderActionMenu() {
        getChildren().clear();
        selectedInfraButton = null;

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Label title = new Label("No players available");
            title.getStyleClass().add("action-title");
            getChildren().add(title);
            return;
        }

        VBox header = createTurnHeader(currentPlayer, "Choose an action:");

        HBox buttonRow = new HBox(8);
        buttonRow.getStyleClass().add("action-button-row");
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        Button buildButton = createActionButton(PlayerAction.BUILD, e -> {
            if (controller != null) {
                controller.onActionClicked(PlayerAction.BUILD);
            }
        });
        Button buyDevCardButton = createActionButton(PlayerAction.BUY_DEV_CARD, e -> {
            if (controller != null) {
                controller.onActionClicked(PlayerAction.BUY_DEV_CARD);
            }
        });
        Button useDevCardButton = createActionButton(PlayerAction.USE_DEV_CARD, e -> {
            if (controller != null) {
                controller.onActionClicked(PlayerAction.USE_DEV_CARD);
            }
        });
        Button tradeButton = createActionButton(PlayerAction.TRADE, e -> {
            if (controller != null) {
                controller.onActionClicked(PlayerAction.TRADE);
            }
        });
        Button endTurnButton = createActionButton(PlayerAction.END_TURN, e -> {
            if (controller != null) {
                controller.onActionClicked(PlayerAction.END_TURN);
            }
        });

        buttonRow.getChildren().addAll(
                buildButton,
                buyDevCardButton,
                useDevCardButton,
                tradeButton,
                endTurnButton
        );

        getChildren().addAll(header, buttonRow);
    }

    public void renderBuildMenu() {
        getChildren().clear();
        selectedInfraButton = null;

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Label title = new Label("No players available");
            title.getStyleClass().add("action-title");
            getChildren().add(title);
            return;
        }

        VBox header = createTurnHeader(currentPlayer, "Choose an infrastructure type:");

        HBox buttonRow = new HBox(8);
        buttonRow.getStyleClass().add("action-button-row");
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        Button roadButton = new Button("Road");
        roadButton.getStyleClass().add("action-bar-button");
        roadButton.setMinWidth(110.0);
        roadButton.setOnAction(e -> {
            if (controller != null) {
                selectInfraButton(roadButton);
                controller.onBuildTypeSelected(InfraType.ROAD);
            }
        });

        Button settlementButton = new Button("Settlement");
        settlementButton.getStyleClass().add("action-bar-button");
        settlementButton.setMinWidth(110.0);
        settlementButton.setOnAction(e -> {
            if (controller != null) {
                selectInfraButton(settlementButton);
                controller.onBuildTypeSelected(InfraType.SETTLEMENT);
            }
        });

        Button cityButton = new Button("City");
        cityButton.getStyleClass().add("action-bar-button");
        cityButton.setMinWidth(110.0);
        cityButton.setOnAction(e -> {
            if (controller != null) {
                selectInfraButton(cityButton);
                controller.onBuildTypeSelected(InfraType.CITY);
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().addAll("action-bar-button", "confirm-button");
        confirmButton.setMinWidth(110.0);
        confirmButton.setOnAction(e -> {
            if (controller != null) {
                controller.onBuildConfirmed();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("action-bar-button");
        cancelButton.setMinWidth(110.0);
        cancelButton.setOnAction(e -> {
            if (controller != null) {
                controller.onBuildCanceled();
            }
        });

        buttonRow.getChildren().addAll(roadButton, settlementButton, cityButton, spacer, confirmButton, cancelButton);

        getChildren().addAll(header, buttonRow);
    }

    public void onBuildTypeSelected(InfraType infraType) {
        if (infraType == null) {
            return;
        }
    }

    public void showError(String message) {
        showPopup("Error", message, Alert.AlertType.ERROR);
    }

    public void showSuccess(String message) {
        showPopup("Success", message, Alert.AlertType.INFORMATION);
    }

    private void showPopup(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void selectInfraButton(Button button) {
        if (selectedInfraButton != null) {
            selectedInfraButton.getStyleClass().remove("selected-infra-button");
        }
        selectedInfraButton = button;
        if (!selectedInfraButton.getStyleClass().contains("selected-infra-button")) {
            selectedInfraButton.getStyleClass().add("selected-infra-button");
        }
    }

    private VBox createTurnHeader(Player player, String promptText) {
        Label playerTurn = new Label(player.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(player.getColor().name()) + ";");

        Label turnPrompt = new Label(promptText);
        turnPrompt.getStyleClass().add("action-prompt-label");

        VBox header = new VBox(2, playerTurn, turnPrompt);
        header.getStyleClass().add("action-header");
        return header;
    }

    private Button createActionButton(PlayerAction action, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(action == null ? "" : formatActionLabel(action));
        button.getStyleClass().add("action-bar-button");
        button.setMinWidth(110.0);
        button.setOnAction(handler);
        return button;
    }

    private String formatActionLabel(PlayerAction action) {
        switch (action) {
            case BUILD:
                return "Build";
            case BUY_DEV_CARD:
                return "Buy Dev Card";
            case USE_DEV_CARD:
                return "Use Dev Card";
            case TRADE:
                return "Trade";
            case END_TURN:
                return "End Turn";
            default:
                return action.name();
        }
    }

    private String mapColorToHex(String color) {
        switch (color) {
            case "RED":
                return "#E74C3C";
            case "BLUE":
                return "#3498DB";
            case "ORANGE":
                return "#E67E22";
            case "WHITE":
                return "#BDC3C7";
            default:
                return "#2c3e50";
        }
    }
}
