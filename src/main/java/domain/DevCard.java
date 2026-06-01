package domain;

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
    public void doKnightAction(Player player, Board board, int targetHexId) {
        verifyCardIsPlayable(DevCardType.KNIGHT);

        player.incrementPlayedKnightCount();
        board.moveRobber(player, targetHexId);

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

        player.getResourceHand().addResource(choice1, 1);
        player.getResourceHand().addResource(choice2, 1);

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
                int countToSteal = opponent.getResourceHand().getResourceCount(targetType);

                if (countToSteal > 0) {
                    totalStolenAmount += countToSteal;
                    opponent.getResourceHand().setResourceCount(targetType, 0);
                }
            }
        }

        int currentBalance = player.getResourceHand().getResourceCount(targetType);
        player.getResourceHand().setResourceCount(targetType, currentBalance + totalStolenAmount);

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