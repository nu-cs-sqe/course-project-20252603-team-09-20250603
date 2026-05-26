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

    public void showBoardView(List<Player> players) {
        BoardController boardController = new BoardController(players);
        BoardView boardView = new BoardView(boardController);
        setCenter(boardView);
    }
}
