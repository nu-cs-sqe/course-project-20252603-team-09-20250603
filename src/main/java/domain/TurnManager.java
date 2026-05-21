package domain;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private int currentPlayerIndex = 0;
    private final int numPlayers;
    private final List<Integer> setupSequence;

    public TurnManager(int numPlayers) {
        this.numPlayers = numPlayers;

        if (numPlayers == 3) {
            this.setupSequence = new ArrayList<>(List.of(1, 2, 3, 3, 2, 1));
        } else{
            this.setupSequence = new ArrayList<>(List.of(1, 2, 3, 4, 4, 3, 2, 1));
        }
    }

    public void nextPlacement() {
        if (!setupSequence.isEmpty()) {
            currentPlayerIndex = setupSequence.remove(0);
        } else {
            currentPlayerIndex++;
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}