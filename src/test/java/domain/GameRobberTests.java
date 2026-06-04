package domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameRobberTests {
    @Test
    void handleMoveRobber_RollIsNotSeven_DoesNotMoveRobber() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);
        game.handleMoveRobber(6, 2);

        assertTrue(board.getHex(1).getHasRobber());
        assertFalse(board.getHex(2).getHasRobber());
    }

    @Test
    void handleMoveRobber_RobberStillOnDesert_MovesRobber() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        game.handleMoveRobber(7, 1);

        assertTrue(board.getHex(1).getHasRobber());

    }

    @Test
    void handleMoveRobber_RobberStartsOnOneHex_MovesRobberToNewHex() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);
        game.handleMoveRobber(7, 2);

        assertFalse(board.getHex(1).getHasRobber());
        assertTrue(board.getHex(2).getHasRobber());

    }

    @Test
    void handleMoveRobber_SelectedHexAlreadyHasRobber_ThrowsException() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);

         assertThrows(IllegalStateException.class, () -> {
             game.handleMoveRobber(7, 1);
         });

        assertTrue(board.getHex(1).getHasRobber());
        assertEquals(1, countRobberHexes(board));

    }

    @Test
    void handleMoveRobber_RobberValidMove_EnsureOnlyOneHexHasRobberAfterMove() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);

        game.handleMoveRobber(7,2);

        assertEquals(1, countRobberHexes(board));

    }

    @Test
    void handleMoveRobber_RollIsSevenButInvalidHexId_ThrowsException() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        assertThrows(IllegalArgumentException.class, () -> {
            game.handleMoveRobber(7, 999);
        });

    }

    private int countRobberHexes(Board board) {
        int robberCount = 0;

        for (Hex hex : board.getHexes()) {
            if (hex.getHasRobber()) {
                robberCount++;
            }
        }

        return robberCount;
    }
}
