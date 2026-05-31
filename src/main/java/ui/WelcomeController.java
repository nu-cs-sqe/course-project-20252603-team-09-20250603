package ui;

import java.util.Locale;

public class WelcomeController {
    private WelcomeView view;
    private final MainView mainView;

    public WelcomeController(MainView mainView) {
        this.mainView = mainView;
    }

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
        view.setStatusMessage(I18n.text("welcome.rulesSoon"));
    }
}
