package domain.logic;

public class TurnManager {
    private int currentPlayerIndex = 0;
    private int placementsCount = 0;
    private final int numPlayers;

    public TurnManager(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void nextPlacement() {
        // Increment count BEFORE logic to track how many houses were placed
        placementsCount++;

        // Move to next player
        // EXCEPT: At the end of the first round (placementsCount == numPlayers)
        // the index stays the same for the snake draft reversal.
        if (placementsCount < numPlayers) {
            currentPlayerIndex++;
        }
        else if (placementsCount > numPlayers && placementsCount < numPlayers * 2) {
            // After the peak of the snake, we start moving backward
            currentPlayerIndex--;
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}