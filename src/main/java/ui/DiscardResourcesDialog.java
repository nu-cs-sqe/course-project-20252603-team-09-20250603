package ui;

import domain.Player;
import domain.ResourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class DiscardResourcesDialog {
    private static final String RESOURCE_ICON_DIR = "/ui/resource-icons/";
    private static final ResourceType[] TRADE_RESOURCES = {
        ResourceType.WOOD, ResourceType.BRICK, ResourceType.ORE,
        ResourceType.SHEEP, ResourceType.WHEAT
    };

    private final OverlayModal<Void> modal;
    private final Player player;
    private final int discardCount;
    private final Map<ResourceType, Integer> selections = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Button> plusButtons = new EnumMap<>(ResourceType.class);

    private Label totalLabel;
    private Button discardButton;
    private int totalSelected = 0;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public DiscardResourcesDialog(Node owner, Player player, int discardCount) {
        this.modal = new OverlayModal<>(owner);
        this.player = player;
        this.discardCount = discardCount;
        for (ResourceType r : TRADE_RESOURCES) {
            selections.put(r, 0);
        }

        modal.setContent(buildContent());
        modal.show();
    }

    private Node buildContent() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().addAll("trade-dialog-root", "message-dialog");
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Label playerLabel = new Label(I18n.text("playerAction.playerTurn", player.getName()));
        playerLabel.getStyleClass().add("player-turn-label");
        playerLabel.setStyle("-fx-text-fill: " + mapColorToHex(player.getColor().name()) + ";");

        Label instructionLabel = new Label(
                I18n.text("discard.instruction", discardCount));
        instructionLabel.getStyleClass().add("build-prompt-label");
        instructionLabel.setWrapText(true);

        totalLabel = new Label(I18n.text("discard.progress", 0, discardCount));
        totalLabel.getStyleClass().add("resources-title");

        HBox resourceRow = buildResourceRow();

        discardButton = new Button(I18n.text("discard.button"));
        discardButton.getStyleClass().addAll("action-button", "confirm-button");
        discardButton.setDisable(true);
        discardButton.setOnAction(e -> {
            player.useResources(nonZero(selections));
            modal.close(null);
        });

        root.getChildren().addAll(playerLabel, instructionLabel, totalLabel, resourceRow, discardButton);
        return root;
    }

    private HBox buildResourceRow() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER);
        Map<ResourceType, Integer> playerResources = player.getResources();
        for (ResourceType r : TRADE_RESOURCES) {
            int available = playerResources.getOrDefault(r, 0);
            row.getChildren().add(buildResourceColumn(r, available));
        }
        return row;
    }

    private VBox buildResourceColumn(ResourceType resource, int available) {
        VBox col = new VBox(4);
        col.setAlignment(Pos.CENTER);
        col.getStyleClass().add("trade-resource-col");

        ImageView icon = loadIcon(resource);

        Label availLabel = new Label(I18n.text("stats.resourceCount", available));
        availLabel.getStyleClass().add("trade-avail-count");

        Label selectedLabel = new Label("0");
        selectedLabel.getStyleClass().add("trade-selected-count");

        Button plus = new Button("+");
        plus.getStyleClass().add("trade-adjust-button");
        plus.setDisable(available == 0 || totalSelected >= discardCount);
        plusButtons.put(resource, plus);

        Button minus = new Button("-");
        minus.getStyleClass().add("trade-adjust-button");
        minus.setDisable(true);

        plus.setOnAction(e -> {
            int current = selections.get(resource);
            if (current < available && totalSelected < discardCount) {
                int next = current + 1;
                selections.put(resource, next);
                selectedLabel.setText(String.valueOf(next));
                totalSelected++;
                minus.setDisable(false);
                plus.setDisable(next >= available || totalSelected >= discardCount);
                updateTotal();
            }
        });

        minus.setOnAction(e -> {
            int current = selections.get(resource);
            if (current > 0) {
                int next = current - 1;
                selections.put(resource, next);
                selectedLabel.setText(String.valueOf(next));
                totalSelected--;
                minus.setDisable(next <= 0);
                updateTotal();
            }
        });

        col.getChildren().addAll(icon, availLabel, plus, selectedLabel, minus);
        return col;
    }

    private void updateTotal() {
        totalLabel.setText(I18n.text("discard.progress", totalSelected, discardCount));
        discardButton.setDisable(totalSelected != discardCount);

        Map<ResourceType, Integer> resources = player.getResources();
        for (ResourceType r : TRADE_RESOURCES) {
            Button plus = plusButtons.get(r);
            if (plus != null) {
                int available = resources.getOrDefault(r, 0);
                int selected = selections.getOrDefault(r, 0);
                plus.setDisable(selected >= available || totalSelected >= discardCount);
            }
        }
    }

    private Map<ResourceType, Integer> nonZero(Map<ResourceType, Integer> map) {
        Map<ResourceType, Integer> result = new EnumMap<>(ResourceType.class);
        for (Map.Entry<ResourceType, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 0) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private ImageView loadIcon(ResourceType resource) {
        URL url = getClass().getResource(RESOURCE_ICON_DIR + iconName(resource));
        ImageView iv = new ImageView(new Image(url.toExternalForm()));
        iv.setFitWidth(36.0);
        iv.setFitHeight(36.0);
        iv.setPreserveRatio(true);
        return iv;
    }

    private String iconName(ResourceType resource) {
        switch (resource) {
            case WOOD:  return "wood.png";
            case BRICK: return "brick.png";
            case ORE:   return "ore.png";
            case SHEEP: return "sheep.png";
            case WHEAT: return "wheat.png";
            default:    return "wood.png";
        }
    }

    private String mapColorToHex(String color) {
        switch (color) {
            case "RED":    return "#E74C3C";
            case "BLUE":   return "#3498DB";
            case "ORANGE": return "#E67E22";
            case "WHITE":  return "#BDC3C7";
            default:       return "#2c3e50";
        }
    }
}
