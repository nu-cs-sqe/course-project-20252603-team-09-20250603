package ui;

public class WelcomeController {
    private WelcomeView view;

    public void setView(WelcomeView view) {
        this.view = view;
    }

    public void handleStartGame() {
        view.setStatusMessage("I (or my friend) will do this soon");
    }

    public void handleRules() {
        view.setStatusMessage("I (or my friend) will do this soon :).");
    }
}
