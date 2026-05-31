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

    // if not knight card, targetHexId = -1
    public void doDevCardAction(Player player, Board board, int targetHexId) {
        if (!this.isActive) {
            throw new IllegalActionException("Development cards cannot be played on the turn they were bought.");
        }

        switch (this.type) {
            case KNIGHT:
                player.incrementPlayedKnightCount();
                board.moveRobber(player, targetHexId);
                break;
            case ROAD_BUILDING:
                player.deductRoads(2);
                board.freeRoads(player, 2);
                break;
        }

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
}