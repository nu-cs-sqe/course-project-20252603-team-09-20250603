package ui;

public class WelcomeController {
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

    public void handleRules() {
        view.setStatusMessage("I (or my friend) will do this soon :).");
    }
}
