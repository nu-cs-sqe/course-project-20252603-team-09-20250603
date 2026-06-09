package domain;

import java.util.HashMap;
import java.util.Map;

public class DevCard {
    private final DevCardType type;
    private boolean isActive;

    public DevCard(DevCardType type) {
        this.type = type;
        this.isActive = false;
    }

    public DevCardType getType() {
        return this.type;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void activateCard() {
        this.isActive = true;
    }
    public void doKnightAction(Player player, Board board, int targetHexId, Player victim) {
        verifyCardIsPlayable(DevCardType.KNIGHT);

        player.incrementPlayedKnightCount();
        board.moveRobberAndSteal(player, targetHexId, victim);

        this.isActive = false;
    }

    public void doRoadBuildingAction(Player player, Board board) {
        verifyCardIsPlayable(DevCardType.ROAD_BUILDING);

        player.deductRoads(2);
        board.freeRoads(player, 2);

        this.isActive = false;
    }

    public void doYearOfPlentyAction(Player player, Board board, ResourceType choice1, ResourceType choice2) {
        if (!this.isActive) {
            throw new IllegalActionException("Cannot play a Dev Card on the turn it was bought!");
        }
        if (this.type != DevCardType.YEAR_OF_PLENTY) {
            throw new IllegalStateException("This card is not a Year of Plenty card!");
        }

        Map<ResourceType, Integer> chosenResources = new HashMap<>();
        chosenResources.put(choice1, chosenResources.getOrDefault(choice1, 0) + 1);
        chosenResources.put(choice2, chosenResources.getOrDefault(choice2, 0) + 1);

        player.addResources(chosenResources);

        this.isActive = false;
    }

    public void doMonopolyAction(Player player, java.util.List<Player> gamePlayers, Board board, ResourceType targetType) {
        if (!this.isActive) {
            throw new IllegalActionException("Cannot play a Dev Card on the turn it was bought!");
        }
        if (this.type != DevCardType.MONOPOLY) {
            throw new IllegalStateException("Card structural mismatch error: Card is not a Monopoly card.");
        }

        int totalStolenAmount = 0;

        for (Player opponent : gamePlayers) {
            if (opponent.getId() != player.getId()) {
                int countToSteal = opponent.getResources().getOrDefault(targetType, 0);

                if (countToSteal > 0) {
                    totalStolenAmount += countToSteal;

                    Map<ResourceType, Integer> resourceCost = new HashMap<>();
                    resourceCost.put(targetType, countToSteal);
                    opponent.useResources(resourceCost);
                }
            }
        }

        if (totalStolenAmount > 0) {
            Map<ResourceType, Integer> stolenResources = new HashMap<>();
            stolenResources.put(targetType, totalStolenAmount);
            player.addResources(stolenResources);
        }

        this.isActive = false;
    }

    private void verifyCardIsPlayable(DevCardType expectedType) {
        if (!this.isActive) {
            throw new IllegalActionException("Development cards cannot be played on the turn they were bought.");
        }
        if (this.type != expectedType) {
            throw new IllegalStateException("Card type structural mismatch error.");
        }
    }
}