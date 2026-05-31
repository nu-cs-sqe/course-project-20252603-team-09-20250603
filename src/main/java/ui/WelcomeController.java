package ui;

public class WelcomeController {
    private WelcomeView view;
    private final MainView mainView;
    private String selectedLanguage = "English";

    public WelcomeController(MainView mainView) {
        this.mainView = mainView;
    }

    public void setView(WelcomeView view) {
        this.view = view;
    }

    public void handleStartGame() {
        mainView.showSetupView();
    }

    public void handleLanguageSelection(String language) {
        selectedLanguage = language;
        // update the welcome view's strings and also show a short status message
        if (view != null) {
            view.updateLanguage(selectedLanguage);
            view.setStatusMessage("Language selected: " + selectedLanguage);
        }
    }

    public void handleRules() {
        view.setStatusMessage("I (or my friend) will do this soon :).");
    }
}
