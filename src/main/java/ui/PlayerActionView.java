package ui;

import domain.DevCard;
import domain.DevCardType;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.ResourceType;
import domain.TradeManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

        URL stylesheet = getClass().getResource("/ui/player-action.css");
        if (stylesheet != null) {
            getStylesheets().add(stylesheet.toExternalForm());
        }
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setController(PlayerActionController controller) {
        this.controller = controller;
    }

    public void renderSetupTurn(Player player, boolean waitingForRoad) {
        getChildren().clear();
        selectedInfraButton = null;
        selectedDevCardButton = null;

        Label title = new Label(I18n.text("playerAction.setupPhaseActive"));
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(I18n.text("playerAction.playerTurn", player.getName()));
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(player.getColor().name()) + ";");

        String instruction = waitingForRoad
                ? I18n.text("playerAction.setupInstructionRoad")
                : I18n.text("playerAction.setupInstructionSettlement");
        Label instructionLabel = new Label(instruction);
        instructionLabel.getStyleClass().add("setup-instruction");
        instructionLabel.setWrapText(true);

        Label invLabel = new Label(
                I18n.text("playerAction.settlementsLeft", player.getInventory().get("settlements"))
                        + "\n"
                        + I18n.text("playerAction.roadsLeft", player.getInventory().get("roads"))
        );
        invLabel.getStyleClass().add("resources-label");

        getChildren().addAll(title, playerTurn, instructionLabel, invLabel);
    }

    public void renderActionMenu() {
        getChildren().clear();
        selectedInfraButton = null;
        selectedDevCardButton = null;

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            Label title = new Label(I18n.text("playerAction.noPlayersAvailable"));
            title.getStyleClass().add("action-title");
            getChildren().add(title);
            return;
        }

        Label title = new Label(I18n.text("playerAction.normalPlay"));
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(I18n.text("playerAction.playerTurn", currentPlayer.getName()));
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(currentPlayer.getColor().name()) + ";");

        Label promptLabel = new Label(controller == null ? "" : controller.getNormalPlayPrompt());
        promptLabel.getStyleClass().add("build-prompt-label");
        promptLabel.setWrapText(true);

        Label actionsTitle = new Label(I18n.text("playerAction.actions"));
        actionsTitle.getStyleClass().add("resources-title");

        VBox actions = new VBox(6);
        actions.getChildren().addAll(
                createActionButton(PlayerAction.BUILD),
                createActionButton(PlayerAction.BUY_DEV_CARD),
                createActionButton(PlayerAction.USE_DEV_CARD),
                createActionButton(PlayerAction.TRADE_WITH_PLAYER),
                createActionButton(PlayerAction.TRADE_WITH_BANK),
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
            Label title = new Label(I18n.text("playerAction.noPlayersAvailable"));
            title.getStyleClass().add("action-title");
            getChildren().add(title);
            return;
        }

        Label title = new Label(I18n.text("playerAction.buildTitle"));
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(I18n.text("playerAction.playerTurn", currentPlayer.getName()));
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(currentPlayer.getColor().name()) + ";");

        Label prompt = new Label(I18n.text("playerAction.buildPrompt"));
        prompt.getStyleClass().add("build-prompt-label");
        prompt.setWrapText(true);

        Button roadButton = createInfraButton(UiText.infra(InfraType.ROAD), InfraType.ROAD);
        Button settlementButton = createInfraButton(UiText.infra(InfraType.SETTLEMENT), InfraType.SETTLEMENT);
        Button cityButton = createInfraButton(UiText.infra(InfraType.CITY), InfraType.CITY);

        Button confirmButton = new Button(I18n.text("button.confirm"));
        confirmButton.getStyleClass().addAll("action-button", "confirm-button");
        confirmButton.setMaxWidth(Double.MAX_VALUE);
        confirmButton.setOnAction(e -> {
            if (controller != null) {
                controller.onBuildConfirmed();
            }
        });

        Button cancelButton = new Button(I18n.text("button.cancel"));
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

    public void renderUseDevCardMenu(Player player, List<DevCard> devCards) {
        getChildren().clear();
        selectedInfraButton = null;
        selectedDevCardButton = null;

        Label title = new Label(I18n.text("playerAction.useDevCardTitle"));
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(I18n.text("playerAction.playerTurn", player.getName()));
        playerTurn.getStyleClass().add("player-turn-label");
        playerTurn.setStyle("-fx-text-fill: " + mapColorToHex(player.getColor().name()) + ";");

        Label prompt = new Label(I18n.text("playerAction.useDevCardPrompt"));
        prompt.getStyleClass().add("build-prompt-label");
        prompt.setWrapText(true);

        getChildren().addAll(title, playerTurn, prompt);

        for (DevCard card : devCards) {
            getChildren().add(createDevCardButton(card));
        }

        Button useButton = new Button(I18n.text("button.use"));
        useButton.getStyleClass().addAll("action-button", "confirm-button");
        useButton.setMaxWidth(Double.MAX_VALUE);
        useButton.setOnAction(e -> {
            if (controller != null) {
                controller.onUseDevCardConfirmed();
            }
        });

        Button cancelButton = new Button(I18n.text("button.cancel"));
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
        return promptChoice(I18n.text("playerAction.chooseResourceTitle"), header, choices, UiText::resource);
    }

    /** Prompts for which player to steal from. Empty if the player cancels. */
    public Optional<Player> promptVictim(List<Player> candidates) {
        return promptChoice(
                I18n.text("playerAction.stealFromTitle"),
                I18n.text("playerAction.stealFromHeader"),
                candidates,
                UiText::playerLabel
        );
    }

    private <T> Optional<T> promptChoice(String title, String header, List<T> options,
                                         java.util.function.Function<T, String> labeler) {
        OverlayModal<Optional<T>> modal = new OverlayModal<>(this);

        VBox root = new VBox(12);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().addAll("trade-dialog-root", "message-dialog");
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("action-title");

        Label headerLabel = new Label(header);
        headerLabel.getStyleClass().add("build-prompt-label");
        headerLabel.setWrapText(true);

        root.getChildren().addAll(titleLabel, headerLabel);

        for (T option : options) {
            Button button = new Button(labeler.apply(option));
            button.setMaxWidth(Double.MAX_VALUE);
            button.getStyleClass().add("action-button");
            button.setOnAction(e -> modal.close(Optional.of(option)));
            root.getChildren().add(button);
        }

        Button cancelButton = new Button(I18n.text("button.cancel"));
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.getStyleClass().addAll("action-button", "cancel-button");
        cancelButton.setOnAction(e -> modal.close(Optional.empty()));
        root.getChildren().add(cancelButton);

        modal.setContent(root);
        return modal.show();
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

    public boolean showTradeWithPlayerDialog(Player activePlayer, List<Player> allPlayers, TradeManager tradeManager) {
        return new TradeWithPlayerDialog(this, activePlayer, allPlayers, tradeManager)
                .wasTradeExecuted();
    }

    public boolean showTradeWithBankDialog(Player activePlayer, TradeManager tradeManager) {
        return new TradeWithBankDialog(this, activePlayer, tradeManager)
                .wasTradeExecuted();
    }

    public void showDiscardDialog(Player player, int discardCount) {
        new DiscardResourcesDialog(this, player, discardCount);
    }

    private Player getCurrentPlayer() {
        if (players.isEmpty() || controller == null) {
            return null;
        }

        Player currentPlayer = controller.getCurrentPlayer();
        return currentPlayer != null ? currentPlayer : players.get(0);
    }

    private Button createActionButton(PlayerAction action) {
        Button button = new Button(UiText.action(action));
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

    private Button createDevCardButton(DevCard card) {
        String statusKey = card.getIsActive()
                ? "playerAction.devCardStatus.active"
                : "playerAction.devCardStatus.inactive";
        Button button = new Button(I18n.text("playerAction.devCardEntry",
                UiText.devCard(card.getType()),
                I18n.text(statusKey)));
        button.getStyleClass().add("action-button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(e -> {
            if (controller != null) {
                selectDevCardButton(button);
                controller.onDevCardSelected(card);
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
