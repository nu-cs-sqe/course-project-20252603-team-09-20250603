package ui;

import domain.Player;
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

    public void renderCurrentPlayer(Player player, boolean isPlacingSettlement) {
        getChildren().clear();

        Label title = new Label("Setup Phase Active");
        title.getStyleClass().add("action-title");

        Label playerTurn = new Label(player.getName() + "'s Turn!");
        playerTurn.getStyleClass().add("player-turn-label");

        // Show infrastructure counts dynamically
        Label invLabel = new Label("Settlements left: " + player.getInventory().get("settlements") +
                "\nRoads left: " + player.getInventory().get("roads"));

        getChildren().addAll(title, playerTurn, invLabel);

        Button actionBtn;
        if (isPlacingSettlement) {
            actionBtn = new Button("Confirm Settlement Placement");
        } else {
            actionBtn = new Button("Confirm Road Placement");
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
        Label complete = new Label("Setup Phase Complete!");
        complete.getStyleClass().add("action-title");
        getChildren().add(complete);
    }
}

