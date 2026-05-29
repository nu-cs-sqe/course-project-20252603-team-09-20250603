package ui;

import domain.Game;
import domain.Player;
import domain.TurnManager;

public class PlayerActionController {
    private PlayerActionView view;
    private final Game game;
    private final TurnManager turnManager;
    private final java.util.List<Player> players;

    // We expect the setup sequence is: Settlement -> Road,
    // so we track which phase the current player is in right now.
    private boolean isPlacingSettlement = true;

    public PlayerActionController(java.util.List<Player> players, Game game, TurnManager turnManager) {
        this.players = players;
        this.game = game;
        this.turnManager = turnManager;
    }

    public void setView(PlayerActionView view) {
        this.view = view;
        updateView();
    }

    public void updateView() {
        int index = turnManager.getCurrentPlayerIndex();

        // TurnManager index is 1-based (e.g. 1, 2, 3...) from your array list setup
        // But java array lists are 0-based, so if index = 1, it's players.get(0).
        // Let's do a safe fallback in case index goes out of bounds.
        if (index > 0 && index <= players.size()) {
            Player currentPlayer = players.get(index - 1);
            view.renderCurrentPlayer(currentPlayer, isPlacingSettlement);
        } else {
             // Sequence empty -> setup is done!
            view.renderSetupComplete();
        }
    }

    public void handleNextActionDone() {
        if (isPlacingSettlement) {
            // They placed a settlement. Now they must place a road.
            isPlacingSettlement = false;
        } else {
            // They placed a road. Turn is over, next player!
            turnManager.nextPlayer();
            isPlacingSettlement = true; // reset for the next player
        }
        updateView();
    }
}

