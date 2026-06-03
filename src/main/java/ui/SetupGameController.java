package ui;

import domain.BuildType;
import domain.Game;
import domain.GamePhase;
import domain.Player;
import domain.TurnManager;

public class SetupGameController {
    private final Game game;
    private final TurnManager turnManager;

    private boolean waitingForRoad = false;
    private int lastSettlementNode = -1;
    private BoardView boardView;

    public SetupGameController(Game game, TurnManager turnManager) {
        this.game = game;
        this.turnManager = turnManager;
        this.turnManager.nextPlayer(); // prime player 1, list now = [2,3,4,4,3,2,1]
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
        Player first = game.getPlayer(turnManager.getCurrentPlayerIndex());
        boardView.setStatusMessage(first.getName() + " - Place your first settlement.");
    }

    public void handleInitialPlacement(int locationId, BuildType buildType) {
        if (!game.PhaseSetupCheck()) {
            return;
        }

        try {
            if (!waitingForRoad) {
                handleInitialSettlement(locationId, buildType);
            } else {
                handleInitialRoad(locationId, buildType);
            }
        } catch (IllegalStateException e) {
            if (boardView != null) boardView.setStatusMessage("Error: " + e.getMessage());
        } catch (Exception e) {
            if (boardView != null) boardView.setStatusMessage("Invalid placement.");
        }
    }

    private void handleInitialSettlement(int locationId, BuildType buildType) {
        if (buildType != BuildType.SETTLEMENT) {
            if (boardView != null) boardView.setStatusMessage("Please click a node to place a Settlement.");
            return;
        }

        Player currentPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        game.build(currentPlayer, BuildType.SETTLEMENT, locationId);
        lastSettlementNode = locationId;
        waitingForRoad = true;

        if (boardView != null) {
            boardView.setStatusMessage(currentPlayer.getName()
                    + " placed settlement. Now place a road.");
        }
    }

    private void handleInitialRoad(int locationId, BuildType buildType) {
        if (buildType != BuildType.ROAD) {
            if (boardView != null) boardView.setStatusMessage("Please click an edge to place a Road.");
            return;
        }

        Player currentPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        game.build(currentPlayer, BuildType.ROAD, locationId);
        waitingForRoad = false;
        lastSettlementNode = -1;

        turnManager.nextPlayer();

        if (turnManager.SetupStatus()) {
            game.advancePhase();
            if (boardView != null) boardView.setStatusMessage("Setup complete! Starting normal play.");
        } else {
            Player nextPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
            if (boardView != null) {
                boardView.setStatusMessage("Next up: " + nextPlayer.getName() + " - Place a settlement.");
            }
        }
    }
}