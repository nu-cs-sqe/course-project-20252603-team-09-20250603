package ui;

import domain.InfraType;
import domain.Game;
import domain.Player;
import domain.TurnManager;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class SetupGameController {
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Shares mutable game model")
    private final Game game;
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Shares mutable turn tracker")
    private final TurnManager turnManager;

    private boolean waitingForRoad = false;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Shares mutable board UI view")
    private BoardView boardView;
    @SuppressFBWarnings( value = "EI_EXPOSE_REP2", justification = "Shares mutable UI controller collaborator")
    private PlayerActionController playerActionController;
    private Runnable onSetupComplete;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public SetupGameController(Game game, TurnManager turnManager) {
        this.game = game;
        this.turnManager = turnManager;
        this.turnManager.nextPlayer(); // prime player 1, list now = [2,3,4,4,3,2,1]
    }

    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
        Player first = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        boardView.setStatusMessage(first.getName() + " - Place your first settlement.");
        refreshSidePanel();
    }

    public void setPlayerActionController(PlayerActionController playerActionController) {
        this.playerActionController = playerActionController;
        refreshSidePanel();
    }

    public void setOnSetupComplete(Runnable onSetupComplete) {
        this.onSetupComplete = onSetupComplete;
    }

    private void refreshSidePanel() {
        if (playerActionController != null) {
            playerActionController.refreshSetupTurn(waitingForRoad);
        }
    }

    public void handleInitialPlacement(int locationId, InfraType buildType) {
        if (!game.phaseSetupCheck()) {
            return;
        }

        try {
            if (!waitingForRoad) {
                handleInitialSettlement(locationId, buildType);
            } else {
                handleInitialRoad(locationId, buildType);
            }
        } catch (IllegalStateException e) {
            if (boardView != null) {boardView.setStatusMessage("Error: " + e.getMessage());}
        } catch (Exception e) {
            if (boardView != null) {boardView.setStatusMessage("Invalid placement.");}
        }
    }

    private void handleInitialSettlement(int locationId, InfraType buildType) {
        if (buildType != InfraType.SETTLEMENT) {
            if (boardView != null) {boardView.setStatusMessage("Please click a node to place a Settlement.");}
            return;
        }

        Player currentPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        game.build(currentPlayer, InfraType.SETTLEMENT, locationId);
        waitingForRoad = true;

        if (boardView != null) {
            boardView.setStatusMessage(currentPlayer.getName()
                    + " placed settlement. Now place a road.");
            boardView.refreshBoard();
        }
        refreshSidePanel();
    }

    private void handleInitialRoad(int locationId, InfraType buildType) {
        if (buildType != InfraType.ROAD) {
            if (boardView != null) {boardView.setStatusMessage("Please click an edge to place a Road.");}
            return;
        }

        Player currentPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        game.build(currentPlayer, InfraType.ROAD, locationId);
        waitingForRoad = false;

        turnManager.nextPlayer();

        if (boardView != null) {
            boardView.refreshBoard();
        }

        if (turnManager.setupStatus()) {
            game.advancePhase();
            if (onSetupComplete != null) {
                onSetupComplete.run();
            }
            if (boardView != null) {
                boardView.setStatusMessage("Setup complete! Starting normal play.");
            }
        } else {
            Player nextPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
            if (boardView != null) {
                boardView.setStatusMessage("Next up: " + nextPlayer.getName() + " - Place a settlement.");
            }
            refreshSidePanel();
        }
    }
}