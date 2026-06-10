package ui;

import domain.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class DiceRollView extends StackPane {
    private static final String[] DIE_FACES = {"", "\u2680", "\u2681", "\u2682", "\u2683", "\u2684", "\u2685"};

    private final Label turnLabel;
    private final Label resultLabel;
    private final Label dieOneLabel;
    private final Label dieTwoLabel;
    private final Button closeButton;
    private Timeline activeTimeline;

    public DiceRollView() {
        setAlignment(Pos.CENTER);
        getStyleClass().add("dice-roll-view");
        setVisible(false);
        setManaged(false);
        setMouseTransparent(false);

        URL stylesheetUrl = getClass().getResource("/ui/dice-roll.css");
        if (stylesheetUrl != null) {
            getStylesheets().add(stylesheetUrl.toExternalForm());
        }

        turnLabel = new Label(I18n.text("dice.setupPhase"));
        turnLabel.getStyleClass().add("dice-turn-label");

        resultLabel = new Label(I18n.text("dice.autoRollInfo"));
        resultLabel.getStyleClass().add("dice-result-label");

        dieOneLabel = createDieLabel();
        dieTwoLabel = createDieLabel();
        closeButton = new Button(I18n.text("dice.close"));
        closeButton.getStyleClass().add("dice-close-button");
        closeButton.setOnAction(event -> hideOverlay());

        VBox textBox = new VBox(4, turnLabel, resultLabel);
        textBox.setAlignment(Pos.CENTER);
        resultLabel.setTextAlignment(TextAlignment.CENTER);

        HBox diceBox = new HBox(14, dieOneLabel, dieTwoLabel);
        diceBox.setAlignment(Pos.CENTER);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        HBox header = new HBox(headerSpacer, closeButton);
        header.setAlignment(Pos.TOP_RIGHT);
        header.getStyleClass().add("dice-header");

        VBox card = new VBox(10, header, textBox, diceBox);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20, 24, 20, 24));
        card.getStyleClass().add("dice-roll-card");
        card.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        getChildren().add(card);
    }

    public void showSetupMessage() {
        stopActiveTimeline();
        turnLabel.setText(I18n.text("dice.setupPhase"));
        resultLabel.setText(I18n.text("dice.beginsAfterSetup"));
        setDiceFaces(1, 1);
        setCloseEnabled(false);
        hideOverlay();
    }

    public void showTurnReady(Player player) {
        stopActiveTimeline();
        showOverlay();
        turnLabel.setText(I18n.text("dice.turnLabel", player.getName()));
        resultLabel.setText(I18n.text("dice.preparing"));
        setDiceFaces(1, 1);
        setCloseEnabled(false);
    }

    public void playRollAnimation(Player player, int finalDieOne, int finalDieTwo, Runnable onFinished) {
        stopActiveTimeline();
        showOverlay();
        turnLabel.setText(I18n.text("dice.turnLabel", player.getName()));
        resultLabel.setText(I18n.text("dice.rolling"));
        setCloseEnabled(false);

        activeTimeline = new Timeline();
        for (int step = 0; step < 10; step++) {
            activeTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(step * 90L), event -> {
                setDiceFaces(randomDieValue(), randomDieValue());
            }));
        }

        activeTimeline.setOnFinished(event -> {
            setDiceFaces(finalDieOne, finalDieTwo);
            resultLabel.setText(I18n.text("dice.rolled", finalDieOne, finalDieTwo, finalDieOne + finalDieTwo));
            activeTimeline = null;
            if (onFinished != null) {
                onFinished.run();
            }
        });
        activeTimeline.playFromStart();
    }

    public void showRollResult(Player player, int dieOne, int dieTwo, String message) {
        stopActiveTimeline();
        showOverlay();
        turnLabel.setText(I18n.text("dice.turnLabel", player.getName()));
        resultLabel.setText(message);
        setDiceFaces(dieOne, dieTwo);
        setCloseEnabled(true);
    }

    private Label createDieLabel() {
        Label label = new Label(DIE_FACES[1]);
        label.getStyleClass().add("die-face");
        return label;
    }

    private void setDiceFaces(int dieOne, int dieTwo) {
        dieOneLabel.setText(DIE_FACES[dieOne]);
        dieTwoLabel.setText(DIE_FACES[dieTwo]);
    }

    private int randomDieValue() {
        return ThreadLocalRandom.current().nextInt(1, 7);
    }

    private void stopActiveTimeline() {
        if (activeTimeline != null) {
            activeTimeline.stop();
            activeTimeline = null;
        }
    }

    private void setCloseEnabled(boolean enabled) {
        closeButton.setDisable(!enabled);
        closeButton.setOpacity(enabled ? 1.0 : 0.0);
        closeButton.setMouseTransparent(!enabled);
    }

    private void showOverlay() {
        setManaged(true);
        setVisible(true);
    }

    private void hideOverlay() {
        setVisible(false);
        setManaged(false);
    }
}
