package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
        if (getClass().getResource(STYLESHEET) != null) {
            getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
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
        englishOption.setSelected(true);
        englishOption.setOnAction(event -> controller.handleLanguageSelection("English"));

        chineseOption.getStyleClass().add("language-option");
        chineseOption.setToggleGroup(languageGroup);
        chineseOption.setOnAction(event -> controller.handleLanguageSelection("Chinese"));

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

        // Initialize texts using default language (English)
        updateLanguage("English");
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Update the visible strings on the welcome screen based on the selected language.
     * Accepts either "English" or "Chinese" (case-insensitive). Falls back to English
     * if the bundle cannot be loaded.
     */
    public void updateLanguage(String language) {
        Locale locale = Locale.ENGLISH;
        if (language != null && language.toLowerCase().startsWith("c")) {
            // use simplified Chinese locale
            locale = Locale.SIMPLIFIED_CHINESE;
        }

        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle("labels", locale);
        } catch (MissingResourceException e) {
            // fallback to English bundle bundled with the app
            try {
                bundle = ResourceBundle.getBundle("labels", Locale.ENGLISH);
            } catch (MissingResourceException ex) {
                // if the English bundle is missing, set hard-coded fallbacks
                titleLabel.setText("Welcome to Catan");
                subtitleLabel.setText("Game by: Team 09 - 20252603");
                languageLabel.setText("Select game language:");
                englishOption.setText("English");
                chineseOption.setText("Chinese");
                startButton.setText("Play Game");
                rulesLink.setText("View Rules");
                return;
            }
        }

        titleLabel.setText(bundle.getString("welcome.title"));
        subtitleLabel.setText(bundle.getString("welcome.subtitle"));
        languageLabel.setText(bundle.getString("welcome.selectLanguage"));
        englishOption.setText(bundle.getString("language.english"));
        chineseOption.setText(bundle.getString("language.chinese"));
        startButton.setText(bundle.getString("button.play"));
        rulesLink.setText(bundle.getString("button.rules"));
    }
}
