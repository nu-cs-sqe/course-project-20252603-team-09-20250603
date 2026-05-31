package ui;

import domain.Player;
import java.util.List;

public class GameStatsController {
    private GameStatsView view;
    private List<Player> players;

    public GameStatsController(List<Player> players) {
        this.players = players;
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

