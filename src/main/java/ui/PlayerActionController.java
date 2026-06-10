package ui;

import domain.DevCard;
import domain.DevCardType;
import domain.Game;
import domain.IllegalActionException;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.ResourceType;
import domain.TradeManager;
import domain.TurnManager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private final TradeManager tradeManager;
    private final List<Player> players;
    private int normalPlayPlayerIndex = 1;
    private boolean rollingForTurn = false;
    private boolean turnRollResolved = false;
    private int lastDieOne = 1;
    private int lastDieTwo = 1;

    private int selectedLocationId = -1;
    private LocationType selectedLocationType = null;
    private InfraType selectedBuildType = null;
    private DevCardType selectedDevCardType = null;
    private boolean awaitingKnightHex = false;
    private boolean awaitingRobberHex = false;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionController(List<Player> players, Game game, TurnManager turnManager) {
        this.players = new ArrayList<>(players);
        this.game = game;
        this.turnManager = turnManager;
        this.tradeManager = new TradeManager();
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

        awaitingKnightHex = false;

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
                openUseDevCardMenu();
                break;
            case TRADE_WITH_PLAYER:
                openTradeWithPlayer();
                break;
            case TRADE_WITH_BANK:
                openTradeWithBank();
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

    private void openTradeWithPlayer() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        boolean hasOtherPlayers = false;
        for (Player p : players) {
            if (p.getId() != currentPlayer.getId()) {
                hasOtherPlayers = true;
                break;
            }
        }
        if (!hasOtherPlayers) {
            if (view != null) {
                view.showError("No other players to trade with.");
            }
            return;
        }

        boolean tradeExecuted = view != null
                && view.showTradeWithPlayerDialog(currentPlayer, players, tradeManager);
        if (tradeExecuted && statsController != null) {
            statsController.updateStats();
        }
    }

    private void openTradeWithBank() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        boolean tradeExecuted = view != null
                && view.showTradeWithBankDialog(currentPlayer, tradeManager);
        if (tradeExecuted && statsController != null) {
            statsController.updateStats();
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

    private void openUseDevCardMenu() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        Map<DevCardType, Integer> counts = countDevCards(currentPlayer);
        if (counts.isEmpty()) {
            if (view != null) {
                view.showError("You have no development cards to use.");
            }
            return;
        }

        selectedDevCardType = null;
        awaitingKnightHex = false;
        if (view != null) {
            view.renderUseDevCardMenu(currentPlayer, counts);
        }
    }

    private Map<DevCardType, Integer> countDevCards(Player player) {
        Map<DevCardType, Integer> counts = new EnumMap<>(DevCardType.class);
        for (DevCard card : player.getDevCardHand()) {
            counts.merge(card.getType(), 1, Integer::sum);
        }
        return counts;
    }

    public void onDevCardTypeSelected(DevCardType type) {
        selectedDevCardType = type;
    }

    public void onUseDevCardCanceled() {
        selectedDevCardType = null;
        awaitingKnightHex = false;
        setHexSelectionMode(false);
        update();
    }

    public void onUseDevCardConfirmed() {
        if (selectedDevCardType == null) {
            if (view != null) {
                view.showError("Select a development card to use first.");
            }
            return;
        }

        switch (selectedDevCardType) {
            case ROAD_BUILDING:
                executeUseDevCard(DevCardType.ROAD_BUILDING, -1, -1, null, null, null, "Road Building played.");
                break;
            case YEAR_OF_PLENTY:
                playYearOfPlenty();
                break;
            case MONOPOLY:
                playMonopoly();
                break;
            case KNIGHT:
                startKnightTargeting();
                break;
            case VICTORY_POINT:
                if (view != null) {
                    view.showSuccess("Victory Point cards are scored automatically and cannot be played.");
                }
                break;
            default:
                break;
        }
    }

    private void playYearOfPlenty() {
        if (view == null) {
            return;
        }

        Optional<ResourceType> first = view.promptResource("Year of Plenty: choose the first resource");
        if (first.isEmpty()) {
            update();
            return;
        }

        Optional<ResourceType> second = view.promptResource("Year of Plenty: choose the second resource");
        if (second.isEmpty()) {
            update();
            return;
        }

        executeUseDevCard(DevCardType.YEAR_OF_PLENTY, -1, -1, first.get(), second.get(), null,
                "Year of Plenty played.");
    }

    private void playMonopoly() {
        if (view == null) {
            return;
        }

        Optional<ResourceType> choice = view.promptResource("Monopoly: choose a resource to take from all opponents");
        if (choice.isEmpty()) {
            update();
            return;
        }

        executeUseDevCard(DevCardType.MONOPOLY, -1, -1, null, null, choice.get(), "Monopoly played.");
    }

    private void startKnightTargeting() {
        awaitingKnightHex = true;
        setHexSelectionMode(true);
        if (boardController != null) {
            boardController.setStatusMessage("Knight: click a hex on the board to move the robber.");
        }
        update();
    }

    public void onHexSelected(int hexId) {
        if (awaitingKnightHex) {
            awaitingKnightHex = false;
            setHexSelectionMode(false);
            handleKnightHexSelected(hexId);
        } else if (awaitingRobberHex) {
            awaitingRobberHex = false;
            setHexSelectionMode(false);
            handleRobberHexSelected(hexId);
        }
    }

    private void handleKnightHexSelected(int hexId) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        List<Player> candidates = new ArrayList<>();
        for (Player occupant : game.getBoard().getPlayersOnHex(hexId)) {
            if (occupant.getId() != currentPlayer.getId()) {
                candidates.add(occupant);
            }
        }

        int victimId = -1;
        if (candidates.size() == 1) {
            victimId = candidates.get(0).getId();
        } else if (candidates.size() > 1 && view != null) {
            Optional<Player> chosen = view.promptVictim(candidates);
            if (chosen.isEmpty()) {
                if (boardController != null) {
                    boardController.setStatusMessage("Knight canceled.");
                }
                update();
                return;
            }
            victimId = chosen.get().getId();
        }

        executeUseDevCard(DevCardType.KNIGHT, hexId, victimId, null, null, null, "Knight played.");
    }

    private void handleRobberHexSelected(int hexId) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        List<Player> candidates = new ArrayList<>();
        for (Player p : game.getBoard().getPlayersOnHex(hexId)) {
            if (p.getId() != currentPlayer.getId()) {
                candidates.add(p);
            }
        }

        Player victim = null;
        if (candidates.size() == 1) {
            victim = candidates.get(0);
        } else if (candidates.size() > 1 && view != null) {
            Optional<Player> chosen = view.promptVictim(candidates);
            victim = chosen.orElse(null);
        }

        boolean victimHadResources = victim != null && getTotalResources(victim) > 0;
        try {
            game.handleMoveRobber(7, hexId, currentPlayer.getId(), victim == null ? -1 : victim.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            if (view != null) {
                view.showError(e.getMessage());
            }
            awaitingRobberHex = true;
            if (boardController != null) {
                boardController.setStatusMessage(
                        currentPlayer.getName() + ": click a different hex to place the robber.");
            }
            setHexSelectionMode(true);
            return;
        }

        if (boardController != null) {
            boardController.refreshBoard();
            if (victim == null) {
                boardController.setStatusMessage(
                        currentPlayer.getName() + " moved the robber. No opponents to steal from.");
            } else if (victimHadResources) {
                boardController.setStatusMessage(
                        currentPlayer.getName() + " stole a resource from " + victim.getName() + ".");
            } else {
                boardController.setStatusMessage(
                        currentPlayer.getName() + " moved the robber. "
                                + victim.getName() + " had no resources to steal.");
            }
        }

        if (statsController != null) {
            statsController.updateStats();
        }
        update();
    }

    private void handleRollSeven() {
        for (Player p : players) {
            if (p.hasMoreThanSevenResources()) {
                int discardCount = getTotalResources(p) / 2;
                if (discardCount > 0 && view != null) {
                    view.showDiscardDialog(p, discardCount);
                }
            }
        }

        Player currentPlayer = getCurrentPlayer();
        awaitingRobberHex = true;
        setHexSelectionMode(true);
        if (boardController != null) {
            boardController.setStatusMessage(
                    currentPlayer.getName() + ": click a hex on the board to place the robber.");
        }
        if (statsController != null) {
            statsController.updateStats();
        }
        update();
    }

    private int getTotalResources(Player player) {
        int total = 0;
        for (int count : player.getResources().values()) {
            total += count;
        }
        return total;
    }

    private void executeUseDevCard(DevCardType type, int hexId, int victimId,
                                   ResourceType choice1, ResourceType choice2, ResourceType targetType,
                                   String successMessage) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        try {
            game.useDevCard(currentPlayer.getId(), type, hexId, victimId, choice1, choice2, targetType);
            if (boardController != null) {
                boardController.refreshBoard();
                boardController.setStatusMessage(currentPlayer.getName() + " played " + formatDevCardType(type) + ".");
            }
            if (view != null) {
                view.showSuccess(successMessage);
            }
            if (statsController != null) {
                statsController.updateStats();
            }
        } catch (IllegalActionException | IllegalArgumentException | IllegalStateException e) {
            if (view != null) {
                view.showError(e.getMessage());
            }
        }

        selectedDevCardType = null;
        awaitingKnightHex = false;
        setHexSelectionMode(false);
        update();
    }

    private void setHexSelectionMode(boolean enabled) {
        if (boardController != null) {
            boardController.setHexSelectionMode(enabled);
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
        return game.phaseSetupCheck() || (!rollingForTurn && turnRollResolved && !awaitingRobberHex);
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

        if (awaitingRobberHex) {
            return "Click a hex on the board to place the robber.";
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

        // Activate dev cards bought on previous turns and reset the once-per-turn limit.
        currentPlayer.manageDevCardActivation(currentPlayer.getId());

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
        rollingForTurn = false;
        turnRollResolved = true;

        if (rollTotal == 7) {
            if (diceRollView != null) {
                diceRollView.showRollResult(currentPlayer, lastDieOne, lastDieTwo,
                        "Rolled 7! The robber activates.");
            }
            handleRollSeven();
        } else {
            game.getBoard().distributeResourcesOnRoll(rollTotal);

            if (diceRollView != null) {
                diceRollView.showRollResult(currentPlayer, lastDieOne, lastDieTwo,
                        "Rolled " + lastDieOne + " + " + lastDieTwo + " = " + rollTotal
                                + ". Resources distributed.");
            }
            if (boardController != null) {
                boardController.setStatusMessage(
                        currentPlayer.getName() + " rolled " + rollTotal + ". Resources distributed.");
                boardController.refreshBoard();
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            update();
        }
    }
}
