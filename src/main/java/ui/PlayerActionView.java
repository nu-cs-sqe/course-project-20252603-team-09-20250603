package ui;

import domain.Player;
import domain.PlayerAction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionView extends VBox {
    private final List<Player> players;
    private int currentPlayerIndex;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionView(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.currentPlayerIndex = 0;

        setPadding(new Insets(15));
        setSpacing(12);
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("player-action-view");
        getStylesheets().add(getClass().getResource("/ui/player-action.css").toExternalForm());

        renderActionMenu();
    }

    private Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }

        return players.get(currentPlayerIndex);
    }

    private void renderActionMenu() {
        getChildren().clear();

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

        Button buildButton = createActionButton(PlayerAction.BUILD, e -> renderBuildMenu());
        Button buyDevCardButton = createActionButton(PlayerAction.BUY_DEV_CARD, e -> {
            // Placeholder for later controller/backend hookup.
        });
        Button useDevCardButton = createActionButton(PlayerAction.USE_DEV_CARD, e -> {
            // Placeholder for later controller/backend hookup.
        });
        Button tradeButton = createActionButton(PlayerAction.TRADE, e -> {
            // Placeholder for later controller/backend hookup.
        });
        Button endTurnButton = createActionButton(PlayerAction.END_TURN, e -> {
            advanceTurn();
            renderActionMenu();
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

    private void renderBuildMenu() {
        getChildren().clear();

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Label title = new Label("No players available");
            title.getStyleClass().add("action-title");
            getChildren().add(title);
            return;
        }

        VBox header = createTurnHeader(currentPlayer, "Choose a building type:");

        HBox buttonRow = new HBox(8);
        buttonRow.getStyleClass().add("action-button-row");
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        Button roadButton = createActionButton(null, e -> renderActionMenu());
        roadButton.setText("Road");
        Button settlementButton = createActionButton(null, e -> renderActionMenu());
        settlementButton.setText("Settlement");
        Button cityButton = createActionButton(null, e -> renderActionMenu());
        cityButton.setText("City");

        buttonRow.getChildren().addAll(roadButton, settlementButton, cityButton);

        getChildren().addAll(header, buttonRow);
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

    private void advanceTurn() {
        if (players.isEmpty()) {
            return;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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

    public void renderCurrentPlayer(Player player, boolean isPlacingSettlement) {
        if (player != null) {
            int playerIndex = players.indexOf(player);
            if (playerIndex >= 0) {
                currentPlayerIndex = playerIndex;
            }
        }

        renderActionMenu();
    }

    public void renderSetupComplete() {
        renderActionMenu();
    }
}

