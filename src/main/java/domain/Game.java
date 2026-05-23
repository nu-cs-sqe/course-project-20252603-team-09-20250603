package domain;

import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private final TurnManager turnManager;

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager){
        this.board = board;
        this.players = players;
        this.dice = dice;
        this.turnManager = turnManager;
    }
}
