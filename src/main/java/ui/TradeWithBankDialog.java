package ui;

import domain.IllegalActionException;
import domain.Player;
import domain.ResourceType;
import domain.TradeManager;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

public class TradeWithBankDialog {
    private static final String RESOURCE_ICON_DIR = "/ui/resource-icons/";
    private static final ResourceType[] TRADE_RESOURCES = {
        ResourceType.WOOD, ResourceType.BRICK, ResourceType.ORE,
        ResourceType.SHEEP, ResourceType.WHEAT
    };
    private static final int BANK_RATE = 4;

    private final Stage stage;
    private final Player player;
    private final TradeManager tradeManager;
    private boolean tradeExecuted;

    private ResourceType selectedGive = null;
    private ResourceType selectedReceive = null;

    private final Map<ResourceType, Button> givePlusButtons = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Button> giveMinusButtons = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Label> giveSelectedLabels = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Button> receivePlusButtons = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Button> receiveMinusButtons = new EnumMap<>(ResourceType.class);
    private final Map<ResourceType, Label> receiveSelectedLabels = new EnumMap<>(ResourceType.class);

    public TradeWithBankDialog(Window owner, Player player, TradeManager tradeManager) {
        this.player = player;
        this.tradeManager = tradeManager;

        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setTitle("Trade With Bank");
        stage.setResizable(false);

        buildScene();
        stage.showAndWait();
    }

    public boolean wasTradeExecuted() {
        return tradeExecuted;
    }

    private void buildScene() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("trade-dialog-root");

        Label title = new Label(player.getName() + " - Trade With Bank  (4 : 1)");
        title.getStyleClass().add("action-title");

        VBox givePanel = buildGivePanel();
        VBox receivePanel = buildReceivePanel();

        Separator sep = new Separator(Orientation.VERTICAL);
        sep.getStyleClass().add("trade-separator");

        HBox panels = new HBox(20, givePanel, sep, receivePanel);
        panels.setAlignment(Pos.TOP_CENTER);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.getStyleClass().addAll("action-button", "confirm-button");
        confirmBtn.setOnAction(e -> {
            if (executeTrade()) {
                tradeExecuted = true;
                stage.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().addAll("action-button", "cancel-button");
        cancelBtn.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, confirmBtn, cancelBtn);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, panels, buttons);

        Scene scene = new Scene(root, 740, 340);
        URL css = getClass().getResource("/ui/player-action.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        stage.setScene(scene);
    }

    private VBox buildGivePanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.getStyleClass().add("trade-panel");
        panel.setPadding(new Insets(12));

        Label header = new Label("You give  (select one):");
        header.getStyleClass().add("resources-title");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER);

        Map<ResourceType, Integer> resources = player.getResources();
        for (ResourceType r : TRADE_RESOURCES) {
            int count = resources.getOrDefault(r, 0);
            row.getChildren().add(buildGiveColumn(r, count));
        }

        panel.getChildren().addAll(header, row);
        return panel;
    }

    private VBox buildReceivePanel() {
        VBox panel = new VBox(10);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.getStyleClass().add("trade-panel");
        panel.setPadding(new Insets(12));

        Label header = new Label("You receive  (select one):");
        header.getStyleClass().add("resources-title");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER);

        for (ResourceType r : TRADE_RESOURCES) {
            row.getChildren().add(buildReceiveColumn(r));
        }

        panel.getChildren().addAll(header, row);
        return panel;
    }

    private VBox buildGiveColumn(ResourceType resource, int available) {
        VBox col = new VBox(4);
        col.setAlignment(Pos.CENTER);
        col.getStyleClass().add("trade-resource-col");

        ImageView icon = loadIcon(resource);

        Label availLabel = new Label("x" + available);
        availLabel.getStyleClass().add("trade-avail-count");

        Label selectedLabel = new Label("0");
        selectedLabel.getStyleClass().add("trade-selected-count");
        giveSelectedLabels.put(resource, selectedLabel);

        Button plus = new Button("+");
        plus.getStyleClass().add("trade-adjust-button");
        plus.setDisable(available < BANK_RATE);
        givePlusButtons.put(resource, plus);

        Button minus = new Button("-");
        minus.getStyleClass().add("trade-adjust-button");
        minus.setDisable(true);
        giveMinusButtons.put(resource, minus);

        plus.setOnAction(e -> selectGiveResource(resource));
        minus.setOnAction(e -> deselectGiveResource(resource));

        col.getChildren().addAll(icon, availLabel, plus, selectedLabel, minus);
        return col;
    }

    private VBox buildReceiveColumn(ResourceType resource) {
        VBox col = new VBox(4);
        col.setAlignment(Pos.CENTER);
        col.getStyleClass().add("trade-resource-col");

        ImageView icon = loadIcon(resource);

        Label bankLabel = new Label("bank");
        bankLabel.getStyleClass().add("trade-avail-count");

        Label selectedLabel = new Label("0");
        selectedLabel.getStyleClass().add("trade-selected-count");
        receiveSelectedLabels.put(resource, selectedLabel);

        Button plus = new Button("+");
        plus.getStyleClass().add("trade-adjust-button");
        receivePlusButtons.put(resource, plus);

        Button minus = new Button("-");
        minus.getStyleClass().add("trade-adjust-button");
        minus.setDisable(true);
        receiveMinusButtons.put(resource, minus);

        plus.setOnAction(e -> selectReceiveResource(resource));
        minus.setOnAction(e -> deselectReceiveResource(resource));

        col.getChildren().addAll(icon, bankLabel, plus, selectedLabel, minus);
        return col;
    }

    private void selectGiveResource(ResourceType resource) {
        if (selectedGive != null && selectedGive != resource) {
            giveSelectedLabels.get(selectedGive).setText("0");
            giveMinusButtons.get(selectedGive).setDisable(true);
            int prevAvail = player.getResources().getOrDefault(selectedGive, 0);
            givePlusButtons.get(selectedGive).setDisable(prevAvail < BANK_RATE);
        }
        selectedGive = resource;
        giveSelectedLabels.get(resource).setText(String.valueOf(BANK_RATE));
        givePlusButtons.get(resource).setDisable(true);
        giveMinusButtons.get(resource).setDisable(false);
    }

    private void deselectGiveResource(ResourceType resource) {
        giveSelectedLabels.get(resource).setText("0");
        giveMinusButtons.get(resource).setDisable(true);
        int avail = player.getResources().getOrDefault(resource, 0);
        givePlusButtons.get(resource).setDisable(avail < BANK_RATE);
        selectedGive = null;
    }

    private void selectReceiveResource(ResourceType resource) {
        if (selectedReceive != null && selectedReceive != resource) {
            receiveSelectedLabels.get(selectedReceive).setText("0");
            receiveMinusButtons.get(selectedReceive).setDisable(true);
            receivePlusButtons.get(selectedReceive).setDisable(false);
        }
        selectedReceive = resource;
        receiveSelectedLabels.get(resource).setText("1");
        receivePlusButtons.get(resource).setDisable(true);
        receiveMinusButtons.get(resource).setDisable(false);
    }

    private void deselectReceiveResource(ResourceType resource) {
        receiveSelectedLabels.get(resource).setText("0");
        receiveMinusButtons.get(resource).setDisable(true);
        receivePlusButtons.get(resource).setDisable(false);
        selectedReceive = null;
    }

    private boolean executeTrade() {
        if (selectedGive == null) {
            showError("Select a resource to give.");
            return false;
        }
        if (selectedReceive == null) {
            showError("Select a resource to receive.");
            return false;
        }
        if (selectedGive == selectedReceive) {
            showError("Cannot trade a resource for itself.");
            return false;
        }

        try {
            tradeManager.tradeWithBank(player, selectedGive, selectedReceive);
            return true;
        } catch (IllegalActionException | IllegalArgumentException e) {
            showError(e.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle("Trade Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}
