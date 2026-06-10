package ui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.net.URL;

final class OverlayModal<T> {
    private static final String OVERLAY_STYLE_CLASS = "message-overlay";

    private final StackPane host;
    private final StackPane overlay;
    private final Object loopKey = new Object();
    private boolean showing;
    private T result;

    OverlayModal(Node owner) {
        if (owner == null || owner.getScene() == null || !(owner.getScene().getRoot() instanceof StackPane)) {
            throw new IllegalArgumentException("Overlay modal requires a node inside a StackPane scene root.");
        }

        host = (StackPane) owner.getScene().getRoot();
        overlay = new StackPane();
        overlay.getStyleClass().add(OVERLAY_STYLE_CLASS);
        overlay.prefWidthProperty().bind(host.widthProperty());
        overlay.prefHeightProperty().bind(host.heightProperty());
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        overlay.setPickOnBounds(true);
        loadStylesheet("/ui/message-dialog.css");
        loadStylesheet("/ui/player-action.css");
    }

    void setContent(Node content) {
        overlay.getChildren().setAll(content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    T show() {
        host.getChildren().add(overlay);
        showing = true;
        Platform.enterNestedEventLoop(loopKey);
        return result;
    }

    void close(T nextResult) {
        if (!showing) {
            result = nextResult;
            return;
        }

        result = nextResult;
        host.getChildren().remove(overlay);
        showing = false;
        Platform.exitNestedEventLoop(loopKey, nextResult);
    }

    private void loadStylesheet(String path) {
        URL stylesheetUrl = getClass().getResource(path);
        if (stylesheetUrl != null) {
            String external = stylesheetUrl.toExternalForm();
            if (!overlay.getStylesheets().contains(external)) {
                overlay.getStylesheets().add(external);
            }
        }
    }
}
