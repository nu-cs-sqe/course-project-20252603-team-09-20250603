package ui;

import domain.Player;
import java.util.List;
import java.util.ArrayList;

@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
public class GameStatsController {
    private GameStatsView view;
    private List<Player> players;

    public GameStatsController(List<Player> players) {
        this.players = new ArrayList<>(players);
    }

    public void setView(GameStatsView view) {
        this.view = view;
        updateStats();
    }

    public void updateStats() {
        if (view != null) {
            view.renderStats(players);
        }
    }
}

