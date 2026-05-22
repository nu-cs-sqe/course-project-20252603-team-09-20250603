package domain;

import java.util.List;

public class Game {
    private Board board;
    private final List<Player> players;

    public Game(Board board, List<Player> players){
        this.board = board;
        this.players = players;
    }


}
