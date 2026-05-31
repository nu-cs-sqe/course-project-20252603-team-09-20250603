package ui;

import domain.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PlayerActionView extends VBox {
    private PlayerActionController controller;

    public PlayerActionView(PlayerActionController controller) {
        this.controller = controller;
        setPadding(new Insets(15));
        setSpacing(10);
        getStyleClass().add("player-action-view");
        java.net.URL stylesheet = getClass().getResource("/ui/player-action.css");
        if (stylesheet != null) {
            getStylesheets().add(stylesheet.toExternalForm());
        }
    }

    public void renderCurrentPlayer(Player player, boolean isPlacingSettlement) {
        getChildren().clear();

        Label title = new Label(I18n.text("playerAction.setupPhaseActive"));
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(I18n.text("playerAction.playerTurn", player.getName()));
        playerTurn.getStyleClass().add("player-turn-label");

        // Show infrastructure counts dynamically
        Label invLabel = new Label(
                I18n.text("playerAction.settlementsLeft", player.getInventory().get("settlements")) +
                        "\n" +
                        I18n.text("playerAction.roadsLeft", player.getInventory().get("roads"))
        );

        getChildren().addAll(title, playerTurn, invLabel);

        Button actionBtn;
        if (isPlacingSettlement) {
            actionBtn = new Button(I18n.text("playerAction.confirmSettlement"));
        } else {
            actionBtn = new Button(I18n.text("playerAction.confirmRoad"));
        }

        actionBtn.setOnAction(e -> {
            // Note: Eventually, this button will read the Node/Edge selected on the BoardView
            // and pass the locationID to the Controller to call game.build(...)!
            controller.handleNextActionDone();
        });

        getChildren().add(actionBtn);
    }

    public void renderSetupComplete() {
        getChildren().clear();
        Label complete = new Label(I18n.text("playerAction.setupComplete"));
        complete.getStyleClass().add("action-title");
        getChildren().add(complete);
    }
}

