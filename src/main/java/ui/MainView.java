package ui;

import domain.Player;
import domain.Game;

import javafx.scene.layout.BorderPane;

import java.util.List;

public class MainView extends BorderPane {
    public MainView() {
        setPrefSize(1000, 700);
        setMinSize(800, 560);
        setStyle("-fx-background-color: #f2efe6;");
        getStyleClass().add("main-view");

        showWelcomeView();
    }

    public void showWelcomeView() {
        WelcomeController welcomeController = new WelcomeController(this);
        WelcomeView welcomeView = new WelcomeView(welcomeController);
        setCenter(welcomeView);
    }

    public void showSetupView() {
        SetupController setupController = new SetupController(this);
        SetupView setupView = new SetupView(setupController);
        setCenter(setupView);
    }

    public void showBoardView(Game game) {
        List<Player> players = game.getPlayers();

        BoardController boardController = new BoardController(game.getBoard(), players);

        SetupGameController setupGameController = new SetupGameController(game, game.getTurnManager());
        boardController.setSetupGameController(setupGameController);

       BoardView boardView = new BoardView(boardController);
        setupGameController.setBoardView(boardView);
        boardView.setStatusMessage("Setup Phase: Waiting for Player 1 to place a Settlement.");

        setCenter(boardView);

        GameStatsController statsController = new GameStatsController(players);
        GameStatsView statsView = new GameStatsView(statsController);
        statsController.setView(statsView);
        setLeft(statsView);
    }
}
