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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Shares mutable stats UI controller collaborator")
    private GameStatsController statsController;
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
        boardView.setStatusMessage(I18n.text("setupGame.firstSettlementPrompt", first.getName()));
        refreshSidePanel();
    }

    public void setPlayerActionController(PlayerActionController playerActionController) {
        this.playerActionController = playerActionController;
        refreshSidePanel();
    }

    public void setStatsController(GameStatsController statsController) {
        this.statsController = statsController;
    }

    public void setOnSetupComplete(Runnable onSetupComplete) {
        this.onSetupComplete = onSetupComplete;
    }

    public boolean isInSetupPhase() {
        return game.phaseSetupCheck();
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
            showError(UiText.exceptionMessage(e));
        } catch (Exception e) {
            showError(I18n.text("error.setup.invalidPlacement"));
        }
    }

    private void handleInitialSettlement(int locationId, InfraType buildType) {
        if (buildType != InfraType.SETTLEMENT) {
            showError(I18n.text("setupGame.error.placeSettlement"));
            return;
        }

        Player currentPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        game.build(currentPlayer, InfraType.SETTLEMENT, locationId);
        waitingForRoad = true;

        if (boardView != null) {
            boardView.setStatusMessage(I18n.text("setupGame.status.settlementPlaced", currentPlayer.getName(), locationId));
            boardView.refreshBoard();
        }
        if (statsController != null) {
            statsController.updateStats();
        }
        refreshSidePanel();
    }

    private void handleInitialRoad(int locationId, InfraType buildType) {
        if (buildType != InfraType.ROAD) {
            showError(I18n.text("setupGame.error.placeRoad"));
            return;
        }

        Player currentPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
        game.build(currentPlayer, InfraType.ROAD, locationId);
        waitingForRoad = false;

        turnManager.nextPlayer();

        if (boardView != null) {
            boardView.refreshBoard();
        }
        if (statsController != null) {
            statsController.updateStats();
        }

        if (turnManager.setupStatus()) {
            game.advancePhase();
            if (statsController != null) {
                statsController.updateStats();
            }
            if (onSetupComplete != null) {
                onSetupComplete.run();
            }
            if (boardView != null) {
                boardView.setStatusMessage(I18n.text("setupGame.complete"));
            }
        } else {
            Player nextPlayer = game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
            if (boardView != null) {
                boardView.setStatusMessage(I18n.text("setupGame.status.roadPlacedNext", locationId, nextPlayer.getName()));
            }
            refreshSidePanel();
        }
    }

    private void showError(String message) {
        if (boardView != null) {
            boardView.showError(message);
        }
    }
}
