package ui;

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

    public void handleRules() {
        view.setStatusMessage("I (or my friend) will do this soon :).");
    }
}
