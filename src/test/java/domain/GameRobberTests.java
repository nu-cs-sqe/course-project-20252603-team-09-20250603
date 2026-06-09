package domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameRobberTests {
    @Test
    void handleMoveRobber_RollIsNotSeven_DoesNotMoveRobber() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);
        game.handleMoveRobber(6, 2, 0, -1);

        assertTrue(board.getHex(1).getHasRobber());
        assertFalse(board.getHex(2).getHasRobber());
    }

    @Test
    void handleMoveRobber_RobberStillOnDesert_MovesRobber() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        game.handleMoveRobber(7, 1, 0, -1);

        assertTrue(board.getHex(1).getHasRobber());

    }

    @Test
    void handleMoveRobber_RobberStartsOnOneHex_MovesRobberToNewHex() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);
        game.handleMoveRobber(7, 2, 0, -1);

        assertFalse(board.getHex(1).getHasRobber());
        assertTrue(board.getHex(2).getHasRobber());

    }

    @Test
    void handleMoveRobber_SelectedHexAlreadyHasRobber_ThrowsException() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        // The robber starts on the desert (hex 9); moving it onto its own hex is illegal.
         assertThrows(IllegalStateException.class, () -> {
             game.handleMoveRobber(7, 9, 0, -1);
         });

        assertTrue(board.getHex(9).getHasRobber());
        assertEquals(1, countRobberHexes(board));

    }

    @Test
    void handleMoveRobber_RobberValidMove_EnsureOnlyOneHexHasRobberAfterMove() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        board.getHex(1).setHasRobber(true);

        game.handleMoveRobber(7, 2, 0, -1);

        assertEquals(1, countRobberHexes(board));

    }

    @Test
    void handleMoveRobber_RollIsSevenButInvalidHexId_ThrowsException() {
        Board board = new Board();
        Player player1 = new Player(0, "Bob", PlayerColor.RED);
        Game game = new Game(board, List.of(player1), new Dice(new Random()), new TurnManager(1));

        assertThrows(IllegalArgumentException.class, () -> {
            game.handleMoveRobber(7, 999, 0, -1);
        });

    }

    @Test
    void handleMoveRobber_OnSeven_StealsOneCardFromAdjacentVictim() {
        Board board = new Board(new Random(0));
        Player active = new Player(0, "Bob", PlayerColor.RED);
        Player victim = new Player(1, "Ann", PlayerColor.BLUE);

        // Victim owns a settlement on node 0, which borders hex 0.
        board.getNode(0).buildSettlement(victim);
        Map<ResourceType, Integer> hand = new HashMap<>();
        hand.put(ResourceType.ORE, 3);
        victim.addResources(hand);

        Game game = new Game(board, List.of(active, victim), new Dice(new Random()), new TurnManager(1));

        game.handleMoveRobber(7, 0, active.getId(), victim.getId());

        // Exactly one card moves from victim to active player.
        assertEquals(2, victim.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(1, active.getResources().getOrDefault(ResourceType.ORE, 0));
        assertTrue(board.getHex(0).getHasRobber());
    }

    @Test
    void handleMoveRobber_VictimNotOnRobberHex_ThrowsAndStealsNothing() {
        Board board = new Board(new Random(0));
        Player active = new Player(0, "Bob", PlayerColor.RED);
        Player victim = new Player(1, "Ann", PlayerColor.BLUE);

        // Victim has resources but no building on hex 0.
        Map<ResourceType, Integer> hand = new HashMap<>();
        hand.put(ResourceType.ORE, 3);
        victim.addResources(hand);

        Game game = new Game(board, List.of(active, victim), new Dice(new Random()), new TurnManager(1));

        assertThrows(IllegalArgumentException.class,
                () -> game.handleMoveRobber(7, 0, active.getId(), victim.getId()));

        assertEquals(3, victim.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(0, active.getResources().getOrDefault(ResourceType.ORE, 0));
        // Validation happens before the robber moves, so it stays on the desert.
        assertTrue(board.getHex(9).getHasRobber());
        assertFalse(board.getHex(0).getHasRobber());
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
