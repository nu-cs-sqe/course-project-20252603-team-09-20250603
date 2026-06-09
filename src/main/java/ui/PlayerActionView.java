package ui;

import domain.DevCardType;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.ResourceType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class PlayerActionView extends VBox {
    private final List<Player> players;
    private PlayerActionController controller;
    private Button selectedInfraButton = null;
    private Button selectedDevCardButton = null;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionView(List<Player> players) {
        this.players = new ArrayList<>(players);

        setPadding(new Insets(15));
        setSpacing(10);
        getStyleClass().add("player-action-view");

        URL stylesheetUrl = getClass().getResource("/ui/player-action.css");
        if (stylesheetUrl != null) {
            getStylesheets().add(stylesheetUrl.toExternalForm());
        }
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setController(PlayerActionController controller) {
        this.controller = controller;
    }

    public void renderSetupTurn(Player player, boolean waitingForRoad) {
        getChildren().clear();
        selectedInfraButton = null;

        Label title = new Label("Setup Phase");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(player.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(player.getColor().name()) + ";");

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

        Label title = new Label("Normal Play");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(currentPlayer.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(currentPlayer.getColor().name()) + ";");

        Label promptLabel = new Label(controller == null ? "" : controller.getNormalPlayPrompt());
        promptLabel.getStyleClass().add("build-prompt-label");
        promptLabel.setWrapText(true);

        Label actionsTitle = new Label("Actions:");
        actionsTitle.getStyleClass().add("resources-title");

        VBox actions = new VBox(6);
        actions.getChildren().addAll(
                createActionButton(PlayerAction.BUILD),
                createActionButton(PlayerAction.BUY_DEV_CARD),
                createActionButton(PlayerAction.USE_DEV_CARD),
                createActionButton(PlayerAction.END_TURN)
        );

        boolean actionsDisabled = controller != null && !controller.canTakeNormalPlayActions();
        actions.setDisable(actionsDisabled);

        getChildren().addAll(title, playerTurn, promptLabel, actionsTitle, actions);
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

        Label title = new Label("Build");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(currentPlayer.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(currentPlayer.getColor().name()) + ";");

        Label prompt = new Label("Choose an infrastructure type, click the board, then confirm.");
        prompt.getStyleClass().add("build-prompt-label");
        prompt.setWrapText(true);

        Button roadButton = createInfraButton("Road", InfraType.ROAD);
        Button settlementButton = createInfraButton("Settlement", InfraType.SETTLEMENT);
        Button cityButton = createInfraButton("City", InfraType.CITY);

        Button confirmButton = new Button("Confirm");
        confirmButton.getStyleClass().addAll("action-button", "confirm-button");
        confirmButton.setMaxWidth(Double.MAX_VALUE);
        confirmButton.setOnAction(e -> {
            if (controller != null) {
                controller.onBuildConfirmed();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("action-button", "cancel-button");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setOnAction(e -> {
            if (controller != null) {
                controller.onBuildCanceled();
            }
        });

        getChildren().addAll(
                title,
                playerTurn,
                prompt,
                roadButton,
                settlementButton,
                cityButton,
                confirmButton,
                cancelButton
        );
    }

    public void renderUseDevCardMenu(Player player, Map<DevCardType, Integer> cardCounts) {
        getChildren().clear();
        selectedInfraButton = null;
        selectedDevCardButton = null;

        Label title = new Label("Use Development Card");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(player.getName() + "'s Turn");
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(player.getColor().name()) + ";");

        Label prompt = new Label("Select a development card, then press Use.");
        prompt.getStyleClass().add("build-prompt-label");
        prompt.setWrapText(true);

        getChildren().addAll(title, playerTurn, prompt);

        for (DevCardType type : DevCardType.values()) {
            int count = cardCounts.getOrDefault(type, 0);
            if (count > 0) {
                getChildren().add(createDevCardButton(type, count));
            }
        }

        Button useButton = new Button("Use");
        useButton.getStyleClass().addAll("action-button", "confirm-button");
        useButton.setMaxWidth(Double.MAX_VALUE);
        useButton.setOnAction(e -> {
            if (controller != null) {
                controller.onUseDevCardConfirmed();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().addAll("action-button", "cancel-button");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setOnAction(e -> {
            if (controller != null) {
                controller.onUseDevCardCanceled();
            }
        });

        getChildren().addAll(useButton, cancelButton);
    }

    /** Prompts for a single non-desert resource. Empty if the player cancels. */
    public Optional<ResourceType> promptResource(String header) {
        List<ResourceType> choices = playableResources();
        ChoiceDialog<ResourceType> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Choose Resource");
        dialog.setHeaderText(header);
        dialog.setContentText("Resource:");
        return dialog.showAndWait();
    }

    /** Prompts for which player to steal from. Empty if the player cancels. */
    public Optional<Player> promptVictim(List<Player> candidates) {
        Map<String, Player> byLabel = new LinkedHashMap<>();
        for (Player candidate : candidates) {
            byLabel.put(candidate.getName() + " (" + candidate.getColor() + ")", candidate);
        }

        List<String> labels = new ArrayList<>(byLabel.keySet());
        ChoiceDialog<String> dialog = new ChoiceDialog<>(labels.get(0), labels);
        dialog.setTitle("Steal From");
        dialog.setHeaderText("Choose a player to steal from");
        dialog.setContentText("Victim:");

        return dialog.showAndWait().map(byLabel::get);
    }

    private List<ResourceType> playableResources() {
        List<ResourceType> choices = new ArrayList<>();
        for (ResourceType resource : ResourceType.values()) {
            if (resource != ResourceType.DESERT) {
                choices.add(resource);
            }
        }
        return choices;
    }

    public void onBuildTypeSelected(InfraType infraType) {
        if (infraType == null) {
            return;
        }
    }

    public void showError(String message) {
        MessageDialog.showError(this, message);
    }

    public void showSuccess(String message) {
        MessageDialog.showInfo(this, message);
    }

    private Player getCurrentPlayer() {
        if (players.isEmpty() || controller == null) {
            return null;
        }

        Player currentPlayer = controller.getCurrentPlayer();
        return currentPlayer != null ? currentPlayer : players.get(0);
    }

    private Button createActionButton(PlayerAction action) {
        Button button = new Button(formatActionLabel(action));
        button.getStyleClass().add("action-button");
        if (action == PlayerAction.END_TURN) {
            button.getStyleClass().add("confirm-button");
        }
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            if (controller != null) {
                controller.onActionClicked(action);
            }
        });
        return button;
    }

    private Button createInfraButton(String label, InfraType infraType) {
        Button button = new Button(label);
        button.getStyleClass().add("action-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            if (controller != null) {
                selectInfraButton(button);
                controller.onBuildTypeSelected(infraType);
            }
        });
        return button;
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

    private Button createDevCardButton(DevCardType type, int count) {
        Button button = new Button(formatDevCardLabel(type) + "  x" + count);
        button.getStyleClass().add("action-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            if (controller != null) {
                selectDevCardButton(button);
                controller.onDevCardTypeSelected(type);
            }
        });
        return button;
    }

    private void selectDevCardButton(Button button) {
        if (selectedDevCardButton != null) {
            selectedDevCardButton.getStyleClass().remove("selected-infra-button");
        }
        selectedDevCardButton = button;
        if (!selectedDevCardButton.getStyleClass().contains("selected-infra-button")) {
            selectedDevCardButton.getStyleClass().add("selected-infra-button");
        }
    }

    private String formatDevCardLabel(DevCardType type) {
        switch (type) {
            case KNIGHT:
                return "Knight";
            case ROAD_BUILDING:
                return "Road Building";
            case YEAR_OF_PLENTY:
                return "Year of Plenty";
            case MONOPOLY:
                return "Monopoly";
            case VICTORY_POINT:
                return "Victory Point";
            default:
                return type.name();
        }
    }

    private String formatActionLabel(PlayerAction action) {
        switch (action) {
            case BUILD:
                return "Build";
            case BUY_DEV_CARD:
                return "Buy Development Card";
            case USE_DEV_CARD:
                return "Use Development Card";
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
