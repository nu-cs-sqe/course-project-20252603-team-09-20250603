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

    public void doDevCardAction(Player player, Board board, int targetHexId) {
        if (!this.isActive) {
            throw new IllegalActionException("Development cards cannot be played on the turn they were bought.");
        }

        player.incrementPlayedKnightCount();
        board.moveRobber(player, targetHexId);

        this.isActive = false;
    }
}