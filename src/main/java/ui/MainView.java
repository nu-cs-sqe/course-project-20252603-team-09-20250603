package ui;

import domain.Player;

import javafx.scene.layout.BorderPane;

import java.util.List;

public class MainView extends BorderPane {
    public MainView() {
        setPrefSize(1000, 700);
        setMinSize(800, 560);
        setStyle("-fx-background-color: #f2efe6;");
        getStyleClass().add("main-view");

        // Load shared UI stylesheet so child views (setup, welcome, board) can use
        // common classes like .primary-button and .secondary-button.
        String sharedStylesheet = "/ui/common.css";
        if (getClass().getResource(sharedStylesheet) != null) {
            getStylesheets().add(getClass().getResource(sharedStylesheet).toExternalForm());
        }

        showWelcomeView();
    }

    public void showWelcomeView() {
        WelcomeController welcomeController = new WelcomeController(this);
        WelcomeView welcomeView = new WelcomeView(welcomeController);
        setCenter(welcomeView);
        setLeft(null);
    }

    public void showSetupView() {
        SetupController setupController = new SetupController(this);
        SetupView setupView = new SetupView(setupController);
        setCenter(setupView);
        setLeft(null);
    }

    public void showBoardView(List<Player> players) {
        BoardController boardController = new BoardController(this, players);
        BoardView boardView = new BoardView(boardController);
        setCenter(boardView);

        GameStatsController statsController = new GameStatsController(players);
        GameStatsView statsView = new GameStatsView(statsController);
        statsController.setView(statsView);
        setLeft(statsView);

        // Remove the hard Game/TurnManager instantiation from the router since
        // they require package-private domain constructors like Dice(Random)
        // that shouldn't be exposed directly to the UI layer yet.
        // We will mock/defer this until GameController is established.

        // PlayerActionController actionController = new PlayerActionController(players, null, null);
        // PlayerActionView actionView = new PlayerActionView(actionController);
        // actionController.setView(actionView);
        // setRight(actionView);
    }
}
