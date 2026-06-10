package ui;

import domain.DevCard;
import domain.DevCardType;
import domain.DomainErrorKey;
import domain.Game;
import domain.IllegalActionException;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.ResourceType;
import domain.TradeManager;
import domain.TurnManager;

import java.util.ArrayList;
import java.util.List;
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
    private DevCard selectedDevCard = null;
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
                view.showError(I18n.text("playerAction.error.waitDiceActions"));
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
                view.showError(I18n.text("playerAction.error.noOtherPlayersToTrade"));
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
                        I18n.text("playerAction.status.devCardBought", currentPlayer.getName()));
            }
            if (view != null) {
                view.showSuccess(I18n.text("playerAction.success.devCardDrawn", UiText.devCard(drawn.getType())));
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            announceWinnerIfGameOver();
            update();
        } catch (IllegalStateException | IllegalArgumentException e) {
            if (view != null) {
                view.showError(UiText.exceptionMessage(e));
            }
        }
    }

    private void openUseDevCardMenu() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        List<DevCard> devCards = currentPlayer.getDevCardHand();
        if (devCards.isEmpty()) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.noDevCards"));
            }
            return;
        }

        selectedDevCard = null;
        awaitingKnightHex = false;
        if (view != null) {
            view.renderUseDevCardMenu(currentPlayer, devCards);
        }
    }

    public void onDevCardSelected(DevCard card) {
        selectedDevCard = card;
    }

    public void onUseDevCardCanceled() {
        selectedDevCard = null;
        awaitingKnightHex = false;
        setHexSelectionMode(false);
        update();
    }

    public void onUseDevCardConfirmed() {
        if (selectedDevCard == null) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.selectDevCardFirst"));
            }
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        if (currentPlayer.getHasPlayedDevCardThisTurn()) {
            if (view != null) {
                view.showError(I18n.text(DomainErrorKey.DEV_CARD_ALREADY_PLAYED_THIS_TURN.key()));
            }
            return;
        }

        DevCard cardToPlay = selectedDevCard;
        if (!cardToPlay.getIsActive()) {
            if (view != null) {
                view.showError(I18n.text(DomainErrorKey.DEV_CARD_NOT_PLAYABLE_ON_PURCHASE_TURN.key()));
            }
            return;
        }

        switch (cardToPlay.getType()) {
            case ROAD_BUILDING:
                executeUseDevCard(cardToPlay, -1, -1, null, null, null, I18n.text("playerAction.success.roadBuildingPlayed"));
                break;
            case YEAR_OF_PLENTY:
                playYearOfPlenty(cardToPlay);
                break;
            case MONOPOLY:
                playMonopoly(cardToPlay);
                break;
            case KNIGHT:
                startKnightTargeting(cardToPlay);
                break;
            case VICTORY_POINT:
                if (view != null) {
                    view.showSuccess(I18n.text("playerAction.success.victoryPointAuto"));
                }
                break;
            default:
                break;
        }
    }

    private void playYearOfPlenty(DevCard cardToPlay) {
        if (view == null) {
            return;
        }

        Optional<ResourceType> first = view.promptResource(I18n.text("playerAction.yearOfPlenty.firstChoice"));
        if (first.isEmpty()) {
            update();
            return;
        }

        Optional<ResourceType> second = view.promptResource(I18n.text("playerAction.yearOfPlenty.secondChoice"));
        if (second.isEmpty()) {
            update();
            return;
        }

        executeUseDevCard(cardToPlay, -1, -1, first.get(), second.get(), null,
                I18n.text("playerAction.success.yearOfPlentyPlayed"));
    }

    private void playMonopoly(DevCard cardToPlay) {
        if (view == null) {
            return;
        }

        Optional<ResourceType> choice = view.promptResource(I18n.text("playerAction.monopoly.choice"));
        if (choice.isEmpty()) {
            update();
            return;
        }

        executeUseDevCard(cardToPlay, -1, -1, null, null, choice.get(), I18n.text("playerAction.success.monopolyPlayed"));
    }

    private void startKnightTargeting(DevCard cardToPlay) {
        selectedDevCard = cardToPlay;
        awaitingKnightHex = true;
        setHexSelectionMode(true);
        if (boardController != null) {
            boardController.setStatusMessage(I18n.text("playerAction.status.knightSelectHex"));
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
                    boardController.setStatusMessage(I18n.text("playerAction.status.knightCanceled"));
                }
                update();
                return;
            }
            victimId = chosen.get().getId();
        }

        executeUseDevCard(selectedDevCard, hexId, victimId, null, null, null, I18n.text("playerAction.success.knightPlayed"));
    }

    private void handleRobberHexSelected(int hexId) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        List<Player> candidates = robberVictimsForHex(hexId, currentPlayer);

        Player victim = null;
        if (!candidates.isEmpty()) {
            if (view != null) {
                Optional<Player> chosen = view.promptVictim(candidates);
                if (chosen.isEmpty()) {
                    awaitingRobberHex = true;
                    setHexSelectionMode(true);
                    if (boardController != null) {
                        boardController.setStatusMessage(I18n.text("playerAction.status.robberChooseVictim", currentPlayer.getName()));
                    }
                    update();
                    return;
                }
                victim = chosen.get();
            } else {
                victim = candidates.get(0);
            }
        }

        boolean victimHadResources = victim != null && getTotalResources(victim) > 0;
        try {
            if (victim == null) {
                game.handleMoveRobberLocation(hexId);
            } else {
                game.handleMoveRobber(7, hexId, currentPlayer.getId(), victim.getId());
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            if (view != null) {
                view.showError(UiText.exceptionMessage(e));
            }
            awaitingRobberHex = true;
            if (boardController != null) {
                boardController.setStatusMessage(I18n.text("playerAction.status.robberRetry", currentPlayer.getName()));
            }
            setHexSelectionMode(true);
            return;
        }

        if (boardController != null) {
            boardController.refreshBoard();
            if (victim == null) {
                boardController.setStatusMessage(I18n.text("playerAction.status.robberMovedNoVictim", currentPlayer.getName()));
            } else if (victimHadResources) {
                boardController.setStatusMessage(I18n.text("playerAction.status.robberStole", currentPlayer.getName(), victim.getName()));
            } else {
                boardController.setStatusMessage(I18n.text("playerAction.status.robberNoResources", currentPlayer.getName(), victim.getName()));
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
            boardController.setStatusMessage(I18n.text("playerAction.status.robberChooseHex", currentPlayer.getName()));
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

    private List<Player> robberVictimsForHex(int hexId, Player currentPlayer) {
        List<Player> candidates = new ArrayList<>();
        for (Player occupant : game.getBoard().getPlayersOnHex(hexId)) {
            if (occupant.getId() != currentPlayer.getId()) {
                candidates.add(occupant);
            }
        }
        return candidates;
    }

    private void executeUseDevCard(DevCard cardToPlay, int hexId, int victimId,
                                   ResourceType choice1, ResourceType choice2, ResourceType targetType,
                                   String successMessage) {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null || cardToPlay == null) {
            return;
        }

        DevCardType type = cardToPlay.getType();
        try {
            game.useDevCard(currentPlayer.getId(), type, hexId, victimId, choice1, choice2, targetType);
            if (boardController != null) {
                boardController.refreshBoard();
                boardController.setStatusMessage(I18n.text("playerAction.status.devCardPlayed", currentPlayer.getName(), UiText.devCard(type)));
            }
            if (view != null) {
                view.showSuccess(successMessage);
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            announceWinnerIfGameOver();
        } catch (IllegalActionException | IllegalArgumentException | IllegalStateException e) {
            if (view != null) {
                view.showError(UiText.exceptionMessage(e));
            }
        }

        selectedDevCard = null;
        awaitingKnightHex = false;
        setHexSelectionMode(false);
        update();
    }

    private void setHexSelectionMode(boolean enabled) {
        if (boardController != null) {
            boardController.setHexSelectionMode(enabled);
        }
    }

    private void beginRobberPlacement() {
        turnRollResolved = true;
        handleRollSeven();
    }

    public void onBuildTypeSelected(InfraType infraType) {
        if (!canTakeNormalPlayActions()) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.waitDiceActions"));
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
                view.showError(I18n.text("playerAction.error.waitDiceSelection"));
            }
            return;
        }

        if (selectedBuildType == null) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.selectInfraFirst"));
            }
            return;
        }

        if (!isLocationTypeValidForInfra(locationType)) {
            if (view != null) {
                String allowed = (selectedBuildType == InfraType.ROAD)
                        ? I18n.text("location.edges")
                        : I18n.text("location.nodes");
                view.showError(I18n.text("playerAction.error.invalidBuildLocation", UiText.infra(selectedBuildType), allowed));
            }
            return;
        }

        selectedLocationId = locationId;
        selectedLocationType = locationType;
    }

    public void onBuildConfirmed() {
        if (!canTakeNormalPlayActions()) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.waitDiceBuilding"));
            }
            return;
        }

        if (selectedBuildType == null || selectedLocationId < 0 || selectedLocationType == null) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.buildMissingSelection"));
            }
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        try {
            game.build(currentPlayer, selectedBuildType, selectedLocationId);
            game.updateLongestRoadBonus();
            if (boardController != null) {
                boardController.refreshBoard();
                boardController.setStatusMessage(
                        I18n.text(
                                "playerAction.status.buildComplete",
                                currentPlayer.getName(),
                                UiText.infra(selectedBuildType),
                                locationTypeLabel(selectedLocationType),
                                selectedLocationId
                        )
                );
            }
            if (view != null) {
                view.showSuccess(I18n.text("playerAction.success.build"));
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            announceWinnerIfGameOver();
            clearBuildState();
            update();
        } catch (IllegalStateException | IllegalArgumentException e) {
            if (view != null) {
                view.showError(UiText.exceptionMessage(e));
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
        return !game.isGameOver() && (game.phaseSetupCheck() || (!rollingForTurn && turnRollResolved));
    }

    private void announceWinnerIfGameOver() {
        Player winner = game.checkForWinner();
        if (winner != null && view != null) {
            view.showSuccess(I18n.text("playerAction.success.winner", winner.getName(), winner.getVictoryPoints()));
        }
    }

    public boolean isRollingForTurn() {
        return rollingForTurn;
    }

    public String getNormalPlayPrompt() {
        if (game.phaseSetupCheck()) {
            return "";
        }

        if (rollingForTurn) {
            return I18n.text("playerAction.prompt.rolling");
        }

        if (!turnRollResolved) {
            return I18n.text("playerAction.prompt.waitingForRoll");
        }

        if (awaitingRobberHex) {
            return I18n.text("playerAction.prompt.placeRobber");
        }

        return I18n.text("playerAction.prompt.chooseAction");
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
            boardController.setStatusMessage(I18n.text("playerAction.status.turnRolling", currentPlayer.getName()));
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

        if (rollTotal == 7) {
            if (diceRollView != null) {
                diceRollView.showRollResult(currentPlayer, lastDieOne, lastDieTwo,
                        I18n.text("playerAction.roll.robberActivates"), this::beginRobberPlacement);
            } else {
                beginRobberPlacement();
            }
        } else {
            turnRollResolved = true;
            game.getBoard().distributeResourcesOnRoll(rollTotal);

            if (diceRollView != null) {
                diceRollView.showRollResult(currentPlayer, lastDieOne, lastDieTwo,
                        I18n.text("playerAction.roll.distributed", lastDieOne, lastDieTwo, rollTotal));
            }
            if (boardController != null) {
                boardController.setStatusMessage(I18n.text("playerAction.status.rollDistributed", currentPlayer.getName(), rollTotal));
                boardController.refreshBoard();
            }
            if (statsController != null) {
                statsController.updateStats();
            }
            update();
        }
    }

    private String locationTypeLabel(LocationType locationType) {
        return locationType == LocationType.EDGE
                ? I18n.text("location.edge")
                : I18n.text("location.node");
    }
}
