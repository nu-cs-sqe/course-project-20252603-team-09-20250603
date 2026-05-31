package domain;

public class DevCard {
    private final DevCardType type;
    private boolean isActive;

    public DevCard(DevCardType type) {
        this.type = type;
        this.isActive = false;
    }

    public DevCardType getType() { return this.type; }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void doDevCardAction(Player player, Board board) {
        if (!this.isActive) {
            throw new IllegalActionException("Development cards cannot be played on the turn they were bought.");
        }

        this.isActive = false;
    }
}