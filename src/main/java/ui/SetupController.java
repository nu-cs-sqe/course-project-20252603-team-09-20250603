package ui;

import domain.Game;
import domain.GameInitializer;

import java.util.ArrayList;
import java.util.List;

public class SetupController {
    private SetupView view;
    private final MainView mainView;
    private final GameInitializer gameInitializer;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public SetupController(MainView mainView) {
        this.mainView = mainView;
        this.gameInitializer = new GameInitializer();
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(SetupView view) {
        this.view = view;
    }

    public void handleStartSetup(List<String> names) {
        try {
            List<String> enteredNames = getEnteredPlayerNames(names);
            Game game = gameInitializer.setupGame(enteredNames);
            mainView.showBoardView(game);
        } catch (IllegalArgumentException e) {
            view.setStatusMessage("Error: " + e.getMessage());
        } catch (Exception e) {
            view.setStatusMessage("System Error: " + e.getMessage());
            e.printStackTrace();
        }
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
