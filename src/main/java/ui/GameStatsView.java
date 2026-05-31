package ui;

import domain.Player;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class GameStatsView extends VBox {
    private GameStatsController controller;

    public GameStatsView(GameStatsController controller) {
        this.controller = controller;

        setPadding(new Insets(15));
        setSpacing(10);
        getStyleClass().add("game-stats-view");

        java.net.URL stylesheet = getClass().getResource("/ui/game-stats.css");
        if (stylesheet != null) {
            getStylesheets().add(stylesheet.toExternalForm());
        }

        // Let the controller populate the initial stats
        // This is done via setView() from MainView
    }

    public void renderStats(List<Player> players) {
        getChildren().clear();

        Label title = new Label(I18n.text("stats.title"));
        title.getStyleClass().add("stats-title");
        getChildren().add(title);

        Label orderLabel = new Label(I18n.text("stats.order", getPlacementOrderString(players.size())));
        orderLabel.getStyleClass().add("stats-order");
        getChildren().add(orderLabel);

        for (Player p : players) {
            VBox playerBox = new VBox(5);
            playerBox.getStyleClass().add("player-stat-box");

            // Map the domain color to a standard CSS friendly color
            String colorHex = mapColorToHex(p.getColor().name());
            playerBox.setStyle("-fx-border-color: " + colorHex + "; -fx-border-width: 2px;");

            Label nameLabel = new Label(p.getName() + " (" + p.getColor() + ")");
            nameLabel.getStyleClass().add("player-name");

            Label vpLabel = new Label(I18n.text("stats.victoryPoints", p.getVictoryPoints()));
            vpLabel.getStyleClass().add("player-vp");

            playerBox.getChildren().addAll(nameLabel, vpLabel);
            getChildren().add(playerBox);
        }
    }

    // Snake draft placeholder
    private String getPlacementOrderString(int numPlayers) {
        if (numPlayers == 3) {
            return "1, 2, 3, 3, 2, 1";
        }
        return "1, 2, 3, 4, 4, 3, 2, 1";
    }

    private String mapColorToHex(String color) {
        switch (color) {
            case "RED": return "#E74C3C";
            case "BLUE": return "#3498DB";
            case "ORANGE": return "#E67E22";
            case "WHITE": return "#BDC3C7"; // off-white so it shows up
            default: return "#95A5A6";
        }
    }
}

