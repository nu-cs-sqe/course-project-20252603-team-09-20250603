package ui;

import domain.DevCard;
import domain.DevCardType;
import domain.Game;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.TurnManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionController {
    public enum LocationType {
        NODE, EDGE
    }

    private PlayerActionView view;
    private BoardController boardController;
    private GameStatsController statsController;
    private DiceRollView diceRollView;
    private final Game game;
    private final TurnManager turnManager;
    private final List<Player> players;
    private int normalPlayPlayerIndex = 1;
    private boolean rollingForTurn = false;
    private boolean turnRollResolved = false;
    private int lastDieOne = 1;
    private int lastDieTwo = 1;

    private int selectedLocationId = -1;
    private LocationType selectedLocationType = null;
    private InfraType selectedBuildType = null;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionController(List<Player> players, Game game, TurnManager turnManager) {
        this.players = new ArrayList<>(players);
        this.game = game;
        this.turnManager = turnManager;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(PlayerActionView view) {
        this.view = view;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setBoardController(BoardController boardController) {
        this.boardController = boardController;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setStatsController(GameStatsController statsController) {
        this.statsController = statsController;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "Shares mutable dice roll UI collaborator"
    )
    public void setDiceRollView(DiceRollView diceRollView) {
        this.diceRollView = diceRollView;
    }

    public void update() {
        if (view != null) {
            view.renderActionMenu();
        }
    }

    public void refreshSetupTurn(boolean waitingForRoad) {
        if (view == null || !game.phaseSetupCheck()) {
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        view.renderSetupTurn(currentPlayer, waitingForRoad);
    }

    public void onSetupFinished() {
        int setupEndingPlayer = turnManager.getCurrentPlayerIndex();
        if (setupEndingPlayer > 0 && setupEndingPlayer <= players.size()) {
            normalPlayPlayerIndex = setupEndingPlayer;
        } else {
            normalPlayPlayerIndex = 1;
        }
        update();
        beginNormalPlayTurn();
    }

    public void onActionClicked(PlayerAction action) {
        if (!canTakeNormalPlayActions()) {
            if (view != null) {
                view.showError("Wait for the dice roll to finish before taking actions.");
            }
            return;
        }

        switch (action) {
            case BUILD:
                clearBuildState();
                if (view != null) {
                    view.renderBuildMenu();
                }
                break;
            case BUY_DEV_CARD:
                buyDevCard();
                break;
            case USE_DEV_CARD:
                // TODO: implement later
                break;
            case END_TURN:
                advanceNormalPlayTurn();
                clearBuildState();
                update();
                break;
            default:
                break;
        }
    }

    private void buyDevCard() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        try {
            game.drawDevCard(currentPlayer.getId());

            List<DevCard> hand = currentPlayer.getDevCardHand();
            DevCard drawn = hand.get(hand.size() - 1);

            if (boardController != null) {
                boardController.setStatusMessage(
                        currentPlayer.getName() + " bought a development card.");
            }
            if (view != null) {
                view.showSuccess("You drew a " + formatDevCardType(drawn.getType()) + " card!");
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            update();
        } catch (IllegalStateException | IllegalArgumentException e) {
            if (view != null) {
                view.showError(e.getMessage());
            }
        }
    }

    private String formatDevCardType(DevCardType type) {
        switch (type) {
            case KNIGHT:
                return "Knight";
            case ROAD_BUILDING:
                return "Road Building";
            case YEAR_OF_PLENTY:
                return "Year of Plenty";
            case MONOPOLY:
                return "Monopoly";
            case VICTORY_POINT:
                return "Victory Point";
            default:
                return type.name();
        }
    }

    public void onBuildTypeSelected(InfraType infraType) {
        if (!canTakeNormalPlayActions()) {
            if (view != null) {
                view.showError("Wait for the dice roll to finish before taking actions.");
            }
            return;
        }

        if (infraType == null) {
            return;
        }
        selectedBuildType = infraType;
        selectedLocationId = -1;
        selectedLocationType = null;
        clearBoardSelection();
        if (view != null) {
            view.onBuildTypeSelected(infraType);
        }
    }

    public void onLocationSelected(int locationId, LocationType locationType) {
        if (!canTakeNormalPlayActions()) {
            if (view != null) {
                view.showError("Wait for the dice roll to finish before selecting a build location.");
            }
            return;
        }

        if (selectedBuildType == null) {
            if (view != null) {
                view.showError("Select infrastructure type to build first!");
            }
            return;
        }

        if (!isLocationTypeValidForInfra(locationType)) {
            String infraName = selectedBuildType.name().toLowerCase();
            String allowed = (selectedBuildType == InfraType.ROAD) ? "edges" : "nodes";
            if (view != null) {
                view.showError(infraName + " must be placed on " + allowed + "!");
            }
            return;
        }

        selectedLocationId = locationId;
        selectedLocationType = locationType;
    }

    public void onBuildConfirmed() {
        if (!canTakeNormalPlayActions()) {
            if (view != null) {
                view.showError("Wait for the dice roll to finish before building.");
            }
            return;
        }

        if (selectedBuildType == null || selectedLocationId < 0 || selectedLocationType == null) {
            if (view != null) {
                view.showError("must select both infrastructure type and location in order to build");
            }
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        try {
            game.build(currentPlayer, selectedBuildType, selectedLocationId);
            if (boardController != null) {
                boardController.refreshBoard();
                boardController.setStatusMessage(
                        currentPlayer.getName()
                                + " built a "
                                + selectedBuildType.name().toLowerCase()
                                + " at "
                                + selectedLocationType.name().toLowerCase()
                                + " "
                                + selectedLocationId + "."
                );
            }
            if (view != null) {
                view.showSuccess("Successful build");
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            clearBuildState();
            update();
        } catch (IllegalStateException | IllegalArgumentException e) {
            if (view != null) {
                view.showError(e.getMessage());
            }
            clearBuildState();
            update();
        }
    }

    public void onBuildCanceled() {
        clearBuildState();
        update();
    }

    public Player getCurrentPlayer() {
        int index = game.phaseSetupCheck() ? turnManager.getCurrentPlayerIndex() : normalPlayPlayerIndex;
        if (index > 0 && index <= players.size()) {
            return players.get(index - 1);
        }
        return players.get(0);
    }

    private void advanceNormalPlayTurn() {
        if (game.phaseSetupCheck() || players.isEmpty()) {
            return;
        }

        normalPlayPlayerIndex = (normalPlayPlayerIndex % players.size()) + 1;
        beginNormalPlayTurn();
    }

    public boolean canTakeNormalPlayActions() {
        return game.phaseSetupCheck() || (!rollingForTurn && turnRollResolved);
    }

    public boolean isRollingForTurn() {
        return rollingForTurn;
    }

    public String getNormalPlayPrompt() {
        if (game.phaseSetupCheck()) {
            return "";
        }

        if (rollingForTurn) {
            return "Dice are rolling for this turn.";
        }

        if (!turnRollResolved) {
            return "Waiting for dice roll.";
        }

        return "Choose an action for this turn.";
    }

    private boolean isLocationTypeValidForInfra(LocationType locationType) {
        if (selectedBuildType == InfraType.ROAD) {
            return locationType == LocationType.EDGE;
        } else {
            return locationType == LocationType.NODE;
        }
    }

    private void clearBuildState() {
        selectedBuildType = null;
        selectedLocationId = -1;
        selectedLocationType = null;
        clearBoardSelection();
    }

    private void clearBoardSelection() {
        if (boardController != null) {
            boardController.clearSelection();
        }
    }

    private void beginNormalPlayTurn() {
        if (game.phaseSetupCheck() || players.isEmpty()) {
            return;
        }

        rollingForTurn = true;
        turnRollResolved = false;
        clearBuildState();

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            rollingForTurn = false;
            return;
        }

        if (boardController != null) {
            boardController.setStatusMessage(currentPlayer.getName() + "'s turn. Rolling dice...");
        }
        if (diceRollView != null) {
            diceRollView.showTurnReady(currentPlayer);
        }
        update();

        game.rollDice();
        lastDieOne = game.getDie1();
        lastDieTwo = game.getDie2();

        if (diceRollView != null) {
            diceRollView.playRollAnimation(currentPlayer, lastDieOne, lastDieTwo, this::finishCurrentTurnRoll);
        } else {
            finishCurrentTurnRoll();
        }
    }

    private void finishCurrentTurnRoll() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            rollingForTurn = false;
            turnRollResolved = true;
            update();
            return;
        }

        int rollTotal = game.getDieSum();
        if (rollTotal != 7) {
            game.getBoard().distributeResourcesOnRoll(rollTotal);
        }

        rollingForTurn = false;
        turnRollResolved = true;

        if (diceRollView != null) {
            String message = (rollTotal == 7)
                    ? "Rolled 7. Robber handling is not wired yet."
                    : "Rolled " + lastDieOne + " + " + lastDieTwo + " = " + rollTotal + ". Resources distributed.";
            diceRollView.showRollResult(currentPlayer, lastDieOne, lastDieTwo, message);
        }

        if (boardController != null) {
            String message = (rollTotal == 7)
                    ? currentPlayer.getName() + " rolled 7. Robber handling is not wired yet."
                    : currentPlayer.getName() + " rolled " + rollTotal + ". Resources distributed.";
            boardController.setStatusMessage(message);
            boardController.refreshBoard();
        }

        if (statsController != null) {
            statsController.updateStats();
        }

        update();
    }
}
