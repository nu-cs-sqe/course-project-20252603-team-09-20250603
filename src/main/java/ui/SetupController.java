package ui;

import domain.GameInitializer;
import domain.Player;

import java.util.ArrayList;
import java.util.List;

public class SetupController {
    private SetupView view;
    private final MainView mainView;
    private final GameInitializer gameInitializer;

    public SetupController(MainView mainView) {
        this.mainView = mainView;
        this.gameInitializer = new GameInitializer();
    }

    public void setView(SetupView view) {
        this.view = view;
    }

    public void handleStartSetup(List<String> names) {
        try {
            List<Player> players = gameInitializer.setupPlayers(getEnteredPlayerNames(names));
            mainView.showBoardView(players);
        } catch (IllegalArgumentException e) {
            view.setStatusMessage("Error: " + e.getMessage());
        }
    }

    public void handleBackToWelcome() {
        mainView.showWelcomeView();
    }

    private List<String> getEnteredPlayerNames(List<String> names) {
        List<String> enteredNames = new ArrayList<>();

        if (names == null) {
            return enteredNames;
        }

        for (String name : names) {
            if (name != null && !name.trim().isEmpty()) {
                enteredNames.add(name.trim());
            }
        }

        return enteredNames;
    }

}
