// Class: TurnManager
// - Handles initial placement turns in snake order (1, 2, 3, 3, 2, 1)
// - Hardcoded lists for 3 or 4 players' order
// - "nextPlayer()": progresses to next turn
//   - Calling nextPlayer() for the first time goes to player 1 turn

package domain;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private int currentPlayerIndex = 0;
    private final int numPlayers;
    private final List<Integer> setupPlayerSequence;
    private boolean isSetupComplete = false;

    public TurnManager(int numPlayers) {
        this.numPlayers = numPlayers;

        if (numPlayers == 3) {
            this.setupPlayerSequence = new ArrayList<>(List.of(1, 2, 3, 3, 2, 1));
        } else{
            this.setupPlayerSequence = new ArrayList<>(List.of(1, 2, 3, 4, 4, 3, 2, 1));
        }
    }

    public boolean SetupStatus()
    {
        return isSetupComplete;
    }

    public void nextPlayer() {
        if (!setupPlayerSequence.isEmpty()) {
            currentPlayerIndex = setupPlayerSequence.remove(0);
        } else {
            isSetupComplete = true;
            currentPlayerIndex++;
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
}
