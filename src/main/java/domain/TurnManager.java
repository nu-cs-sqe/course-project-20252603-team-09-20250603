package domain;

public class TurnManager {
    private int currentPlayerIndex = 0;
    private int placementsCount = 0;
    private final int numPlayers;

    public TurnManager(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void nextPlacement() {
        placementsCount++;

        if (placementsCount < numPlayers) {
            currentPlayerIndex++;
        }
        else if (placementsCount > numPlayers && placementsCount < numPlayers * 2) {
            currentPlayerIndex--;
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}