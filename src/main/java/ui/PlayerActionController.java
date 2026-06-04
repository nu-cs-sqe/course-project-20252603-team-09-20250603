package ui;

import domain.Game;
import domain.Player;
import domain.TurnManager;

import java.util.ArrayList;

public class PlayerActionController {
    private PlayerActionView view;
    private final Game game;
    private final TurnManager turnManager;
    private final java.util.List<Player> players;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public PlayerActionController(java.util.List<Player> players, Game game, TurnManager turnManager) {
        this.players = new ArrayList<>(players);
        this.game = game;
        this.turnManager = turnManager;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(PlayerActionView view) {
        this.view = view;
    }

    public void refreshSetupTurn(boolean waitingForRoad) {
        if (view == null || !game.phaseSetupCheck()) {
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        view.renderSetupTurn(currentPlayer, waitingForRoad);
    }

    public void onSetupFinished() {
        refreshNormalPlayView();
    }

    private void refreshNormalPlayView() {
        if (view == null) {
            return;
        }

        view.renderNormalPlay(getCurrentPlayer());
    }

    private Player getCurrentPlayer() {
        int index = turnManager.getCurrentPlayerIndex();
        if (index > 0 && index <= players.size()) {
            return players.get(index - 1);
        }
        return players.get(0);
    }
}
