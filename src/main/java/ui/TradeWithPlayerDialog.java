package ui;

import domain.IllegalActionException;
import domain.Player;
import domain.ResourceType;
import domain.TradeManager;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TradeWithPlayerDialog {
    private static final String RESOURCE_ICON_DIR = "/ui/resource-icons/";
    private static final ResourceType[] TRADE_RESOURCES = {
        ResourceType.WOOD, ResourceType.BRICK, ResourceType.ORE,
        ResourceType.SHEEP, ResourceType.WHEAT
    };

    private final Node owner;
    private final OverlayModal<Boolean> modal;
    private final Player activePlayer;
    private final TradeManager tradeManager;
    private final List<Player> otherPlayers;
    private boolean tradeExecuted;

    private Player opponent;
    private final Map<ResourceType, Integer> offer = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Integer> request = new EnumMap<>(ResourceType.class);

    public TradeWithPlayerDialog(Node owner, Player activePlayer, List<Player> allPlayers, TradeManager tradeManager) {
        this.owner = owner;
        this.modal = new OverlayModal<>(owner);
        this.activePlayer = activePlayer;
        this.tradeManager = tradeManager;
        this.otherPlayers = new ArrayList<>();
        for (Player p : allPlayers) {
            if (p.getId() != activePlayer.getId()) {
                otherPlayers.add(p);
            }
        }
        for (ResourceType r : TRADE_RESOURCES) {
            offer.put(r, 0);
            request.put(r, 0);
        }

        showPlayerSelectScreen();
        Boolean result = modal.show();
        tradeExecuted = Boolean.TRUE.equals(result);
    }

    public boolean wasTradeExecuted() {
        return tradeExecuted;
    }

    private void showPlayerSelectScreen() {
        VBox root = createDialogRoot();

        Label title = new Label("Who do you want to trade with?");
        title.getStyleClass().add("action-title");
        root.getChildren().add(title);

        for (Player p : otherPlayers) {
            Button btn = new Button(p.getName() + " (" + p.getColor() + ")");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("action-button");
            btn.setOnAction(e -> {
                opponent = p;
                showResourceSelectionScreen();
            });
            root.getChildren().add(btn);
        }

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        cancelBtn.getStyleClass().addAll("action-button", "cancel-button");
        cancelBtn.setOnAction(e -> modal.close(false));
        root.getChildren().add(cancelBtn);

        modal.setContent(root);
    }

    private void showResourceSelectionScreen() {
        for (ResourceType r : TRADE_RESOURCES) {
            offer.put(r, 0);
            request.put(r, 0);
        }

        VBox root = createDialogRoot();

        Label title = new Label(activePlayer.getName() + " <-> " + opponent.getName());
        title.getStyleClass().add("action-title");

        VBox offerPanel = buildResourcePanel(activePlayer.getName() + " offers:", activePlayer, offer);
        VBox requestPanel = buildResourcePanel(activePlayer.getName() + " receives:", opponent, request);

        Separator sep = new Separator(Orientation.VERTICAL);
        sep.getStyleClass().add("trade-separator");

        HBox panels = new HBox(20, offerPanel, sep, requestPanel);
        panels.setAlignment(Pos.TOP_CENTER);

        Button continueBtn = new Button("Continue");
        continueBtn.getStyleClass().addAll("action-button", "confirm-button");
        continueBtn.setOnAction(e -> showConfirmScreen());

        Button backBtn = new Button("Back");
        backBtn.getStyleClass().add("action-button");
        backBtn.setOnAction(e -> showPlayerSelectScreen());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().addAll("action-button", "cancel-button");
        cancelBtn.setOnAction(e -> modal.close(false));

        HBox buttons = new HBox(10, continueBtn, backBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, panels, buttons);
        modal.setContent(root);
    }

    private VBox buildResourcePanel(String header, Player player, Map<ResourceType, Integer> selections) {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.getStyleClass().add("trade-panel");
        panel.setPadding(new Insets(12));

        Label headerLabel = new Label(header);
        headerLabel.getStyleClass().add("resources-title");

        HBox resourceRow = new HBox(12);
        resourceRow.setAlignment(Pos.CENTER);

        Map<ResourceType, Integer> playerResources = player.getResources();
        for (ResourceType r : TRADE_RESOURCES) {
            int available = playerResources.getOrDefault(r, 0);
            resourceRow.getChildren().add(buildResourceColumn(r, available, selections));
        }

        panel.getChildren().addAll(headerLabel, resourceRow);
        return panel;
    }

    private VBox buildResourceColumn(ResourceType resource, int available, Map<ResourceType, Integer> selections) {
        VBox col = new VBox(4);
        col.setAlignment(Pos.CENTER);
        col.getStyleClass().add("trade-resource-col");

        ImageView icon = loadIcon(resource);

        Label availLabel = new Label("x" + available);
        availLabel.getStyleClass().add("trade-avail-count");

        Label selectedLabel = new Label("0");
        selectedLabel.getStyleClass().add("trade-selected-count");

        Button plus = new Button("+");
        plus.getStyleClass().add("trade-adjust-button");
        plus.setDisable(available == 0);

        Button minus = new Button("-");
        minus.getStyleClass().add("trade-adjust-button");
        minus.setDisable(true);

        plus.setOnAction(e -> {
            int current = selections.get(resource);
            if (current < available) {
                int next = current + 1;
                selections.put(resource, next);
                selectedLabel.setText(String.valueOf(next));
                minus.setDisable(false);
                plus.setDisable(next >= available);
            }
        });

        minus.setOnAction(e -> {
            int current = selections.get(resource);
            if (current > 0) {
                int next = current - 1;
                selections.put(resource, next);
                selectedLabel.setText(String.valueOf(next));
                plus.setDisable(false);
                minus.setDisable(next <= 0);
            }
        });

        col.getChildren().addAll(icon, availLabel, plus, selectedLabel, minus);
        return col;
    }

    private void showConfirmScreen() {
        VBox root = createDialogRoot();

        Label title = new Label(opponent.getName() + ": Accept this trade?");
        title.getStyleClass().add("action-title");

        Label offerLine = new Label(activePlayer.getName() + " offers:  " + summarize(offer));
        offerLine.getStyleClass().add("trade-summary-label");

        Label requestLine = new Label(activePlayer.getName() + " wants:   " + summarize(request));
        requestLine.getStyleClass().add("trade-summary-label");

        Button acceptBtn = new Button("Accept");
        acceptBtn.getStyleClass().addAll("action-button", "confirm-button");
        acceptBtn.setOnAction(e -> {
            if (executeTrade()) {
                modal.close(true);
            }
        });

        Button declineBtn = new Button("Decline");
        declineBtn.getStyleClass().addAll("action-button", "cancel-button");
        declineBtn.setOnAction(e -> modal.close(false));

        HBox buttons = new HBox(12, acceptBtn, declineBtn);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, offerLine, requestLine, buttons);
        modal.setContent(root);
    }

    private boolean executeTrade() {
        Map<ResourceType, Integer> activeOffer = nonZero(offer);
        Map<ResourceType, Integer> activeRequest = nonZero(request);
        try {
            tradeManager.tradeWithPlayer(activePlayer, opponent, activeOffer, activeRequest);
            return true;
        } catch (IllegalActionException | IllegalArgumentException e) {
            MessageDialog.showError(owner, e.getMessage());
            return false;
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

    private String summarize(Map<ResourceType, Integer> selection) {
        StringBuilder sb = new StringBuilder();
        for (ResourceType r : TRADE_RESOURCES) {
            int count = selection.getOrDefault(r, 0);
            if (count > 0) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(count).append(" ").append(r.name().toLowerCase());
            }
        }
        return sb.length() > 0 ? sb.toString() : "nothing";
    }

    private ImageView loadIcon(ResourceType resource) {
        String path = RESOURCE_ICON_DIR + iconName(resource);
        URL url = getClass().getResource(path);
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

    private VBox createDialogRoot() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().addAll("trade-dialog-root", "message-dialog");
        root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        return root;
    }
}
