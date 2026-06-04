package ui;

import domain.Player;
import domain.ResourceType;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayerActionView extends VBox {
    private final PlayerActionController controller;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionView(PlayerActionController controller) {
        this.controller = controller;
        setPadding(new Insets(15));
        setSpacing(10);
        getStyleClass().add("player-action-view");
        getStylesheets().add(getClass().getResource("/ui/player-action.css").toExternalForm());
    }

    public void renderSetupTurn(Player player, boolean waitingForRoad) {
        getChildren().clear();

        Label title = new Label("Setup Phase");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(player.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");

        String instruction = waitingForRoad
                ? "Click an edge on the board to place a road."
                : "Click a node on the board to place a settlement.";
        Label instructionLabel = new Label(instruction);
        instructionLabel.getStyleClass().add("setup-instruction");
        instructionLabel.setWrapText(true);

        Label invLabel = new Label("Settlements left: " + player.getInventory().get("settlements")
                + "\nRoads left: " + player.getInventory().get("roads"));
        invLabel.getStyleClass().add("resources-label");

        getChildren().addAll(title, playerTurn, instructionLabel, invLabel);
    }

    public void renderNormalPlay(Player currentPlayer) {
        getChildren().clear();

        Label title = new Label("Normal Play");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(currentPlayer.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");

        Label resourcesTitle = new Label("Resources:");
        resourcesTitle.getStyleClass().add("resources-title");
        Label resources = new Label(formatResources(currentPlayer));
        resources.getStyleClass().add("resources-label");
        resources.setWrapText(true);

        Label actionsTitle = new Label("Actions:");
        actionsTitle.getStyleClass().add("resources-title");
        VBox actions = new VBox(6);
        actions.getChildren().addAll(
                stubActionButton("Build Road"),
                stubActionButton("Build Settlement"),
                stubActionButton("Build City"),
                stubActionButton("Buy Development Card"),
                stubActionButton("Use Development Card"),
                stubActionButton("Trade"),
                stubActionButton("End Turn")
        );

        getChildren().addAll(title, playerTurn, resourcesTitle, resources, actionsTitle, actions);
    }

    private Button stubActionButton(String label) {
        Button button = new Button(label);
        button.setDisable(true);
        button.getStyleClass().add("action-button");
        return button;
    }

    private String formatResources(Player player) {
        Map<ResourceType, Integer> resources = player.getResources();
        if (resources.isEmpty()) {
            return "none";
        }

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            if (entry.getValue() <= 0) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(entry.getKey().name().toLowerCase())
                    .append(" x")
                    .append(entry.getValue());
        }
        return builder.length() == 0 ? "none" : builder.toString();
    }
}

