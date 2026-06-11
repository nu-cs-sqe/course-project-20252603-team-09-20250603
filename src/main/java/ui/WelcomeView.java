package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Locale;

public class WelcomeView extends VBox {
    private static final String STYLESHEET = "/ui/welcome-view.css";

    // UI elements kept as fields so we can update their text when language changes
    private final Label titleLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final Label statusLabel = new Label();
    private final Button startButton = new Button();
    private final Button rulesLink = new Button();
    private final Label languageLabel = new Label();
    private final RadioButton englishOption = new RadioButton();
    private final RadioButton chineseOption = new RadioButton();

    public WelcomeView(WelcomeController controller) {
        controller.setView(this);

        getStyleClass().add("welcome-view");
        java.net.URL stylesheet = getClass().getResource(STYLESHEET);
        if (stylesheet != null) {
            getStylesheets().add(stylesheet.toExternalForm());
        }
        setMinSize(800, 560);
        setPrefSize(1000, 700);
        setAlignment(Pos.CENTER);
        setSpacing(18);

        titleLabel.getStyleClass().add("welcome-title");

        subtitleLabel.getStyleClass().add("welcome-subtitle");

        languageLabel.getStyleClass().add("language-label");

        ToggleGroup languageGroup = new ToggleGroup();

        englishOption.getStyleClass().add("language-option");
        englishOption.setToggleGroup(languageGroup);
        englishOption.setOnAction(event -> controller.handleLanguageSelection(Locale.US));

        chineseOption.getStyleClass().add("language-option");
        chineseOption.setToggleGroup(languageGroup);
        chineseOption.setOnAction(event -> controller.handleLanguageSelection(Locale.SIMPLIFIED_CHINESE));

        HBox languageSection = new HBox(16, englishOption, chineseOption);
        languageSection.setAlignment(Pos.CENTER);

        startButton.getStyleClass().add("primary-button");
        startButton.setOnAction(event -> controller.handleStartGame());

        rulesLink.getStyleClass().add("secondary-button");
        rulesLink.setOnAction(event -> controller.handleRules());

        statusLabel.getStyleClass().add("status-label");

        getChildren().addAll(
            titleLabel,
            subtitleLabel,
            languageLabel,
            languageSection,
            startButton,
            rulesLink,
            statusLabel
        );

        refreshTexts();
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public void refreshTexts() {
        boolean chinese = Locale.SIMPLIFIED_CHINESE.getLanguage().equals(I18n.getLocale().getLanguage());
        englishOption.setSelected(!chinese);
        chineseOption.setSelected(chinese);

        titleLabel.setText(I18n.text("welcome.title"));
        subtitleLabel.setText(I18n.text("welcome.subtitle"));
        languageLabel.setText(I18n.text("welcome.selectLanguage"));
        englishOption.setText(I18n.text("language.english"));
        chineseOption.setText(I18n.text("language.chinese"));
        startButton.setText(I18n.text("button.play"));
        rulesLink.setText(I18n.text("button.rules"));
    }
}
