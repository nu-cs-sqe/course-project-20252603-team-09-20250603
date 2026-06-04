package domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLongestRoadTests {
    @Test
    void calculateLongestRoad_PlayerHasNoRoads_ReturnsZero() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        assertEquals(0, game.calculateLongestRoad(player));
    }

    @Test
    void calculateLongestRoad_PlayerHasOneRoad_ReturnsOne() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        board.getEdge(0).buildRoad(player);

        assertEquals(1, game.calculateLongestRoad(player));
    }

    @Test
    void calculateLongestRoad_PlayerHasThreeConnectedRoads_ReturnsThree() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        buildRoadBetweenNodes(board, player, 0, 1);
        buildRoadBetweenNodes(board, player, 1, 2);
        buildRoadBetweenNodes(board, player, 2, 3);

        assertEquals(3, game.calculateLongestRoad(player));
    }

    @Test
    void calculateLongestRoad_DifferentPlayersRoads_OnlyCountsSelectedPlayersRoads() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Player otherPlayer = new Player(1, "Jane", PlayerColor.BLUE);
        Game game = createGame(board, player, otherPlayer);

        buildRoadBetweenNodes(board, player, 0, 1);
        buildRoadBetweenNodes(board, player, 1, 2);
        buildRoadBetweenNodes(board, otherPlayer, 2, 3);

        assertEquals(2, game.calculateLongestRoad(player));
    }

    @Test
    void updateLongestRoadBonus_PlayerReachesFiveRoads_GainsTwoVictoryPoints() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        buildRoadBetweenNodes(board, player, 0, 1);
        buildRoadBetweenNodes(board, player, 1, 2);
        buildRoadBetweenNodes(board, player, 2, 3);
        buildRoadBetweenNodes(board, player, 3, 4);
        buildRoadBetweenNodes(board, player, 4, 5);

        game.updateLongestRoadBonus();

        assertEquals(2, player.getVictoryPoints());
    }

    @Test
    void updateLongestRoadBonus_NoPlayerHasFiveRoads_NoPointsAwarded() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        buildRoadBetweenNodes(board, player, 0, 1);
        buildRoadBetweenNodes(board, player, 1, 2);
        buildRoadBetweenNodes(board, player, 2, 3);

        game.updateLongestRoadBonus();

        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    void updateLongestRoadBonus_SamePlayerStillHasLongestRoad_DoesNotAwardAgain() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        buildRoadBetweenNodes(board, player, 0, 1);
        buildRoadBetweenNodes(board, player, 1, 2);
        buildRoadBetweenNodes(board, player, 2, 3);
        buildRoadBetweenNodes(board, player, 3, 4);
        buildRoadBetweenNodes(board, player, 4, 5);

        game.updateLongestRoadBonus();
        game.updateLongestRoadBonus();

        assertEquals(2, player.getVictoryPoints());
    }

    @Test
    void updateLongestRoadBonus_AnotherPlayerExceedsLongestRoad_BonusTransfers() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Player otherPlayer = new Player(1, "Jane", PlayerColor.BLUE);
        Game game = createGame(board, player, otherPlayer);

        buildRoadBetweenNodes(board, player, 0, 1);
        buildRoadBetweenNodes(board, player, 1, 2);
        buildRoadBetweenNodes(board, player, 2, 3);
        buildRoadBetweenNodes(board, player, 3, 4);
        buildRoadBetweenNodes(board, player, 4, 5);

        game.updateLongestRoadBonus();

        buildRoadBetweenNodes(board, otherPlayer, 7, 8);
        buildRoadBetweenNodes(board, otherPlayer, 8, 9);
        buildRoadBetweenNodes(board, otherPlayer, 9, 10);
        buildRoadBetweenNodes(board, otherPlayer, 10, 11);
        buildRoadBetweenNodes(board, otherPlayer, 11, 12);
        buildRoadBetweenNodes(board, otherPlayer, 12, 13);

        game.updateLongestRoadBonus();

        assertEquals(0, player.getVictoryPoints());
        assertEquals(2, otherPlayer.getVictoryPoints());
    }

    private Game createGame(Board board, Player player) {
        return createGame(board, player, null);
    }

    private Game createGame(Board board, Player player, Player otherPlayer) {
        Dice dice = new Dice(new Random());
        TurnManager turnManager = new TurnManager(1);

        if (otherPlayer == null) {
            return new Game(board, List.of(player), dice, turnManager);
        }

        return new Game(board, List.of(player, otherPlayer), dice, turnManager);
    }

    private void buildRoadBetweenNodes(Board board, Player player, int nodeAId, int nodeBId) {
        Edge edge = findEdgeBetweenNodes(board, nodeAId, nodeBId);
        edge.buildRoad(player);
    }

    private Edge findEdgeBetweenNodes(Board board, int nodeAId, int nodeBId) {
        Node nodeA = board.getNode(nodeAId);
        Node nodeB = board.getNode(nodeBId);

        for (Edge edge : board.getEdges()) {
            boolean connectsForward = edge.getNodeA().equals(nodeA) && edge.getNodeB().equals(nodeB);
            boolean connectsBackward = edge.getNodeA().equals(nodeB) && edge.getNodeB().equals(nodeA);

            if (connectsForward || connectsBackward) {
                return edge;
            }
        }

        throw new IllegalArgumentException("No edge exists between those nodes.");
    }
}