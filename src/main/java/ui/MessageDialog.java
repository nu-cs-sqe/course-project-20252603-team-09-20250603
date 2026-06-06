package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.net.URL;

public final class MessageDialog {
    private static final String OVERLAY_STYLE_CLASS = "message-overlay";
    private static final String STYLESHEET = "/ui/message-dialog.css";

    private MessageDialog() {
    }

    public static void showError(Node owner, String message) {
        show(owner, "Error", message, "error-dialog");
    }

    public static void showInfo(Node owner, String message) {
        show(owner, "Success", message, "info-dialog");
    }

    private static void show(Node owner, String title, String message, String dialogStyleClass) {
        if (owner == null || owner.getScene() == null) {
            return;
        }

        Node root = owner.getScene().getRoot();
        if (!(root instanceof StackPane)) {
            return;
        }

        StackPane stackPane = (StackPane) root;
        stackPane.getChildren().removeIf(node -> node.getStyleClass().contains(OVERLAY_STYLE_CLASS));

        StackPane overlay = new StackPane();
        overlay.getStyleClass().add(OVERLAY_STYLE_CLASS);
        overlay.prefWidthProperty().bind(stackPane.widthProperty());
        overlay.prefHeightProperty().bind(stackPane.heightProperty());
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setPickOnBounds(true);

        URL stylesheetUrl = MessageDialog.class.getResource(STYLESHEET);
        if (stylesheetUrl != null && !overlay.getStylesheets().contains(stylesheetUrl.toExternalForm())) {
            overlay.getStylesheets().add(stylesheetUrl.toExternalForm());
        }

        VBox dialog = new VBox(12);
        dialog.getStyleClass().addAll("message-dialog", dialogStyleClass);
        dialog.setPadding(new Insets(20));
        dialog.setAlignment(Pos.CENTER);
        dialog.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("message-dialog-title");

        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("message-dialog-text");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        messageLabel.setMaxWidth(320);

        Button okButton = new Button("OK");
        okButton.getStyleClass().add("message-dialog-button");
        okButton.setDefaultButton(true);
        okButton.setOnAction(e -> stackPane.getChildren().remove(overlay));

        dialog.getChildren().addAll(titleLabel, messageLabel, okButton);
        overlay.getChildren().add(dialog);
        StackPane.setAlignment(dialog, Pos.CENTER);
        stackPane.getChildren().add(overlay);
        okButton.requestFocus();
    }
}
