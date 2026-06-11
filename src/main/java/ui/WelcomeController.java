package ui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class WelcomeController {
    private static final String RULES_URL =
            "https://www.catan.com/sites/default/files/2021-06/catan_base_rules_2020_200707.pdf";
    private WelcomeView view;
    private final MainView mainView;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public WelcomeController(MainView mainView) {
        this.mainView = mainView;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(WelcomeView view) {
        this.view = view;
    }

    public void handleStartGame() {
        mainView.showSetupView();
    }

    public void handleLanguageSelection(Locale locale) {
        I18n.setLocale(locale);
        if (view != null) {
            view.refreshTexts();
            String selectedLanguage = Locale.SIMPLIFIED_CHINESE.equals(locale)
                    ? I18n.text("language.chinese")
                    : I18n.text("language.english");
            view.setStatusMessage(I18n.text("welcome.languageSelected", selectedLanguage));
        }
    }

    public void handleRules() {
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            if (view != null) {
                view.setStatusMessage(RULES_URL);
            }
            return;
        }

        try {
            Desktop.getDesktop().browse(new URI(RULES_URL));
        } catch (IOException | URISyntaxException exception) {
            if (view != null) {
                view.setStatusMessage(RULES_URL);
            }
        }
    }
}
