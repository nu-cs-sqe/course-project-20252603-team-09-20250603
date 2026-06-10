package ui;

import domain.Player;
import domain.ResourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

public class GameStatsView extends VBox {
    private static final String BUILDING_COSTS_IMAGE = "/ui/building-costs.jpg";
    private static final String BUILDING_COSTS_IMAGE_CHINESE = "/ui/building-costs-chinese.png";
    private static final String RESOURCE_ICON_DIR = "/ui/resource-icons/";

    public GameStatsView(GameStatsController controller) {

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

            Label nameLabel = new Label(UiText.playerLabel(p));
            nameLabel.getStyleClass().add("player-name");

            Label vpLabel = new Label(I18n.text("stats.victoryPoints", p.getVictoryPoints()));
            vpLabel.getStyleClass().add("player-vp");

            Label devCardLabel = new Label(I18n.text("stats.devCards", p.getDevCardHand().size()));
            devCardLabel.getStyleClass().add("player-vp");

            HBox resourcesRow = buildResourcesRow(p);

            playerBox.getChildren().addAll(nameLabel, vpLabel, devCardLabel, resourcesRow);

            if (p.getHasLongestRoad()) {
                Label longestRoadBadge = new Label(I18n.text("stats.longestRoad"));
                longestRoadBadge.getStyleClass().add("player-vp");
                playerBox.getChildren().add(longestRoadBadge);
            }

            getChildren().add(playerBox);
        }

        ImageView buildingCostsImage = new ImageView(
                new Image(getClass().getResource(getBuildingCostsImagePath()).toExternalForm())
        );
        buildingCostsImage.getStyleClass().add("building-costs-image");
        buildingCostsImage.setFitWidth(190.0);
        buildingCostsImage.setPreserveRatio(true);

        VBox.setMargin(buildingCostsImage, new Insets(12, 0, 0, 0));
        getChildren().add(buildingCostsImage);
    }

    private String getBuildingCostsImagePath() {
        return java.util.Locale.SIMPLIFIED_CHINESE.getLanguage().equals(I18n.getLocale().getLanguage())
                ? BUILDING_COSTS_IMAGE_CHINESE
                : BUILDING_COSTS_IMAGE;
    }

    // Snake draft placeholder
    private String getPlacementOrderString(int numPlayers) {
        if (numPlayers == 3) {
            return I18n.text("stats.order.threePlayers");
        }
        return I18n.text("stats.order.fourPlayers");
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

    private HBox buildResourcesRow(Player player) {
        Map<ResourceType, Integer> resources = player.getResources();
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("resource-row");
        row.getChildren().addAll(
                createResourceChip("wood.png", resources.getOrDefault(ResourceType.WOOD, 0)),
                createResourceChip("brick.png", resources.getOrDefault(ResourceType.BRICK, 0)),
                createResourceChip("ore.png", resources.getOrDefault(ResourceType.ORE, 0)),
                createResourceChip("sheep.png", resources.getOrDefault(ResourceType.SHEEP, 0)),
                createResourceChip("wheat.png", resources.getOrDefault(ResourceType.WHEAT, 0))
        );
        return row;
    }

    private HBox createResourceChip(String iconName, int count) {
        ImageView iconView = new ImageView(new Image(getClass().getResource(RESOURCE_ICON_DIR + iconName).toExternalForm()));
        iconView.setFitWidth(18.0);
        iconView.setFitHeight(18.0);
        iconView.setPreserveRatio(true);
        iconView.getStyleClass().add("resource-icon");

        Label countLabel = new Label(I18n.text("stats.resourceCount", count));
        countLabel.getStyleClass().add("player-resources");

        HBox chip = new HBox(3, iconView, countLabel);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().add("resource-chip");
        return chip;
    }
}
