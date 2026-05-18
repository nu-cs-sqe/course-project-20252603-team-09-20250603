package domain;

import java.util.List;

public class TurnManager {
    private int currentPlayerIndex = 0;
    private int placementsCount = 0;
    private final int numPlayers;
    private final List<Integer> setupSequence;

    public TurnManager(int numPlayers) {
        this.numPlayers = numPlayers;

        if (numPlayers == 3) {
            this.setupSequence = List.of(1, 2, 3, 3, 2, 1);
        } else if (numPlayers == 4) {
            this.setupSequence = List.of(1, 2, 3, 4, 4, 3, 2, 1);
        } else {
            throw new IllegalArgumentException("Unsupported player count: " + numPlayers);
        }
    }

    public void nextPlacement() {
        placementsCount++;

        if (placementsCount < setupSequence.size()) {
            currentPlayerIndex = setupSequence.get(placementsCount);
        } else {
            currentPlayerIndex = (currentPlayerIndex % numPlayers) + 1;
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}