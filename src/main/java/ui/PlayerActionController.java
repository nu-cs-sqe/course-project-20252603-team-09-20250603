package ui;

import domain.DevCard;
import domain.DevCardType;
import domain.Game;
import domain.IllegalActionException;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.ResourceType;
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
                        I18n.text("playerAction.status.devCardBought", currentPlayer.getName()));
            }
            if (view != null) {
                view.showSuccess(I18n.text("playerAction.success.devCardDrawn", UiText.devCard(drawn.getType())));
            }
            if (statsController != null) {
                statsController.updateStats();
            }
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

        Map<DevCardType, Integer> counts = countDevCards(currentPlayer);
        if (counts.isEmpty()) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.noDevCards"));
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
        update();
    }

    public void onUseDevCardConfirmed() {
        if (selectedDevCardType == null) {
            if (view != null) {
                view.showError(I18n.text("playerAction.error.selectDevCardFirst"));
            }
            return;
        }

        switch (selectedDevCardType) {
            case ROAD_BUILDING:
                executeUseDevCard(DevCardType.ROAD_BUILDING, -1, -1, null, null, null, I18n.text("playerAction.success.roadBuildingPlayed"));
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
                    view.showSuccess(I18n.text("playerAction.success.victoryPointAuto"));
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

        executeUseDevCard(DevCardType.YEAR_OF_PLENTY, -1, -1, first.get(), second.get(), null,
                I18n.text("playerAction.success.yearOfPlentyPlayed"));
    }

    private void playMonopoly() {
        if (view == null) {
            return;
        }

        Optional<ResourceType> choice = view.promptResource(I18n.text("playerAction.monopoly.choice"));
        if (choice.isEmpty()) {
            update();
            return;
        }

        executeUseDevCard(DevCardType.MONOPOLY, -1, -1, null, null, choice.get(), I18n.text("playerAction.success.monopolyPlayed"));
    }

    private void startKnightTargeting() {
        awaitingKnightHex = true;
        if (boardController != null) {
            boardController.setStatusMessage(I18n.text("playerAction.status.knightSelectHex"));
        }
        update();
    }

    public void onHexSelected(int hexId) {
        if (!awaitingKnightHex) {
            return;
        }
        awaitingKnightHex = false;

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

        executeUseDevCard(DevCardType.KNIGHT, hexId, victimId, null, null, null, I18n.text("playerAction.success.knightPlayed"));
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
                boardController.setStatusMessage(I18n.text("playerAction.status.devCardPlayed", currentPlayer.getName(), UiText.devCard(type)));
            }
            if (view != null) {
                view.showSuccess(successMessage);
            }
            if (statsController != null) {
                statsController.updateStats();
            }
        } catch (IllegalActionException | IllegalArgumentException | IllegalStateException e) {
            if (view != null) {
                view.showError(UiText.exceptionMessage(e));
            }
        }

        selectedDevCardType = null;
        awaitingKnightHex = false;
        update();
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
            return I18n.text("playerAction.prompt.rolling");
        }

        if (!turnRollResolved) {
            return I18n.text("playerAction.prompt.waitingForRoll");
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
        if (rollTotal != 7) {
            game.getBoard().distributeResourcesOnRoll(rollTotal);
        }

        rollingForTurn = false;
        turnRollResolved = true;

        if (diceRollView != null) {
            String message = (rollTotal == 7)
                    ? I18n.text("playerAction.roll.seven")
                    : I18n.text("playerAction.roll.distributed", lastDieOne, lastDieTwo, rollTotal);
            diceRollView.showRollResult(currentPlayer, lastDieOne, lastDieTwo, message);
        }

        if (boardController != null) {
            String message = (rollTotal == 7)
                    ? I18n.text("playerAction.status.rollSeven", currentPlayer.getName())
                    : I18n.text("playerAction.status.rollDistributed", currentPlayer.getName(), rollTotal);
            boardController.setStatusMessage(message);
            boardController.refreshBoard();
        }

        if (statsController != null) {
            statsController.updateStats();
        }

        update();
    }

    private String locationTypeLabel(LocationType locationType) {
        return locationType == LocationType.EDGE
                ? I18n.text("location.edge")
                : I18n.text("location.node");
    }
}
