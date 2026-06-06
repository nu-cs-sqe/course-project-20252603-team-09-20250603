package ui;

import domain.Game;
import domain.Player;

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
        setLeft(null);
        setBottom(null);
        setRight(null);
        WelcomeController welcomeController = new WelcomeController(this);
        WelcomeView welcomeView = new WelcomeView(welcomeController);
        setCenter(welcomeView);
    }

    public void showSetupView() {
        setLeft(null);
        setBottom(null);
        setRight(null);
        SetupController setupController = new SetupController(this);
        SetupView setupView = new SetupView(setupController);
        setCenter(setupView);
    }

    public void showBoardView(Game game) {
        List<Player> players = game.getPlayers();

        BoardController boardController = new BoardController(game.getBoard(), players);

        SetupGameController setupGameController = new SetupGameController(game, game.getTurnManager());
        boardController.setSetupGameController(setupGameController);

        GameStatsController statsController = new GameStatsController(players);
        GameStatsView statsView = new GameStatsView(statsController);
        statsController.setView(statsView);

        BoardView boardView = new BoardView(boardController);
        setupGameController.setBoardView(boardView);
        boardView.setStatusMessage("Setup Phase: Waiting for Player 1 to place a Settlement.");

        PlayerActionController playerActionController =
                new PlayerActionController(players, game, game.getTurnManager());
        PlayerActionView playerActionView = new PlayerActionView(players);
        playerActionController.setView(playerActionView);
        playerActionView.setController(playerActionController);
        setupGameController.setPlayerActionController(playerActionController);
        setupGameController.setStatsController(statsController);
        setupGameController.setOnSetupComplete(playerActionController::onSetupFinished);
        boardController.setActionController(playerActionController);
        playerActionController.setBoardController(boardController);
        playerActionController.setStatsController(statsController);

        setBottom(null);
        setCenter(boardView);
        setRight(playerActionView);
        setLeft(statsView);
    }
}
