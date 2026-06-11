package domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ui.SetupGameController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class InitialPlacementStepDefinitions {
    private static final String SETTLEMENTS = "settlements";
    private static final String ROADS = "roads";

    private static final int[] SNAKE_NODES = {0, 6, 27, 37, 47, 53};

    private Board board;
    private Game game;
    private TurnManager turnManager;
    private Dice dice;
    private SetupGameController setupController;

    private List<Player> players;
    private Player otherPlayer;

    private Player actor;
    private int lastEdgeId;

    private Map<String, Integer> baselineInventory;
    private Map<ResourceType, Integer> baselineResources;
    private int baselineVictoryPoints;

    private Map<Integer, List<Integer>> settlementNodesByPlayerId;

    @Given("a setup game with {int} players")
    public void a_setup_game_with_players(Integer numPlayers) {
        PlayerColor[] colors = {PlayerColor.RED, PlayerColor.BLUE, PlayerColor.ORANGE, PlayerColor.WHITE};

        players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i, "Player " + (i + 1), colors[i]));
        }

        dice = new Dice(new Random());
        turnManager = new TurnManager(numPlayers);
        settlementNodesByPlayerId = new HashMap<>();
    }

    @Given("an initialized setup board")
    public void an_initialized_setup_board() {
        board = new Board();
        game = new Game(board, players, dice, turnManager);
        setupController = new SetupGameController(game, turnManager);
    }

    @Given("it is the current player's turn to place a settlement")
    public void it_is_the_current_players_turn_to_place_a_settlement() {
        assertTrue(game.phaseSetupCheck());
    }

    @Given("the current player has placed a settlement at node {int}")
    public void the_current_player_has_placed_a_settlement_at_node(Integer nodeId) {
        Player player = currentPlayer();
        setupController.handleInitialPlacement(nodeId, InfraType.SETTLEMENT);
        recordSettlement(player, nodeId);
    }

    @Given("node {int} is already occupied by another player's settlement")
    public void node_is_already_occupied_by_another_players_settlement(Integer nodeId) {
        otherPlayer = new Player(99, "Other Player", PlayerColor.WHITE);
        board.getNode(nodeId).buildSettlement(otherPlayer);

        assertEquals(otherPlayer, board.getNode(nodeId).getNodeOccupant());
    }

    @Given("the game is no longer in the setup phase")
    public void the_game_is_no_longer_in_the_setup_phase() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        assertFalse(game.phaseSetupCheck());
    }

    @When("the current player places a settlement at node {int}")
    public void the_current_player_places_a_settlement_at_node(Integer nodeId) {
        captureBaseline(currentPlayer());
        setupController.handleInitialPlacement(nodeId, InfraType.SETTLEMENT);
    }

    @When("the current player places a road on an edge connected to node {int}")
    public void the_current_player_places_a_road_connected_to_node(Integer nodeId) {
        captureBaseline(currentPlayer());
        lastEdgeId = firstEdgeConnectedTo(nodeId).getId();
        setupController.handleInitialPlacement(lastEdgeId, InfraType.ROAD);
    }

    @When("the current player places a road on an edge not connected to node {int}")
    public void the_current_player_places_a_road_not_connected_to_node(Integer nodeId) {
        captureBaseline(currentPlayer());
        lastEdgeId = firstEdgeNotConnectedTo(nodeId).getId();
        setupController.handleInitialPlacement(lastEdgeId, InfraType.ROAD);
    }

    @When("the current player attempts a road at node {int} while a settlement is expected")
    public void the_current_player_attempts_a_road_while_a_settlement_is_expected(Integer nodeId) {
        captureBaseline(currentPlayer());
        lastEdgeId = firstEdgeConnectedTo(nodeId).getId();
        setupController.handleInitialPlacement(lastEdgeId, InfraType.ROAD);
    }

    @When("the current player attempts a settlement at node {int} while a road is expected")
    public void the_current_player_attempts_a_settlement_while_a_road_is_expected(Integer nodeId) {
        captureBaseline(currentPlayer());
        setupController.handleInitialPlacement(nodeId, InfraType.SETTLEMENT);
    }

    @When("all players complete their initial settlements and roads in snake order")
    public void all_players_complete_their_initial_settlements_and_roads_in_snake_order() {
        for (int nodeId : SNAKE_NODES) {
            Player player = currentPlayer();
            setupController.handleInitialPlacement(nodeId, InfraType.SETTLEMENT);
            recordSettlement(player, nodeId);
            setupController.handleInitialPlacement(firstEdgeConnectedTo(nodeId).getId(), InfraType.ROAD);
        }
    }

    @Then("node {int} should be occupied by the current player's settlement")
    public void node_should_be_occupied_by_the_current_players_settlement(Integer nodeId) {
        Node node = board.getNode(nodeId);
        assertEquals(actor, node.getNodeOccupant());
        assertEquals(InfraType.SETTLEMENT, node.getInfraType());
    }

    @Then("the current player's settlement inventory should decrease by one")
    public void the_current_players_settlement_inventory_should_decrease_by_one() {
        assertEquals(baselineInventory.get(SETTLEMENTS) - 1, (int) actor.getInventory().get(SETTLEMENTS));
    }

    @Then("the current player's resources should remain unchanged")
    public void the_current_players_resources_should_remain_unchanged() {
        assertEquals(baselineResources, actor.getResources());
    }

    @Then("the current player's victory points should increase by one")
    public void the_current_players_victory_points_should_increase_by_one() {
        assertEquals(baselineVictoryPoints + 1, actor.getVictoryPoints());
    }

    @Then("the connected edge should be occupied by the current player's road")
    public void the_connected_edge_should_be_occupied_by_the_current_players_road() {
        assertEquals(actor, board.getEdge(lastEdgeId).getEdgeOccupant());
    }

    @Then("the current player's road inventory should decrease by one")
    public void the_current_players_road_inventory_should_decrease_by_one() {
        assertEquals(baselineInventory.get(ROADS) - 1, (int) actor.getInventory().get(ROADS));
    }

    @Then("it should become player {int}'s turn")
    public void it_should_become_player_turn(Integer playerNumber) {
        assertEquals(playerNumber.intValue(), turnManager.getCurrentPlayerIndex());
    }

    @Then("it should still be player {int}'s turn")
    public void it_should_still_be_player_turn(Integer playerNumber) {
        assertEquals(playerNumber.intValue(), turnManager.getCurrentPlayerIndex());
    }

    @Then("the game should prevent the placement")
    public void the_game_should_prevent_the_placement() {
        assertEquals(baselineInventory, actor.getInventory());
        assertEquals(baselineResources, actor.getResources());
    }

    @Then("the placement should be ignored")
    public void the_placement_should_be_ignored() {
        assertEquals(baselineInventory, actor.getInventory());
        assertEquals(baselineResources, actor.getResources());
    }

    @Then("the connected edge should not be occupied by the current player's road")
    public void the_connected_edge_should_not_be_occupied_by_the_current_players_road() {
        assertNotEquals(actor, board.getEdge(lastEdgeId).getEdgeOccupant());
    }

    @Then("that disconnected edge should not be occupied by the current player's road")
    public void that_disconnected_edge_should_not_be_occupied_by_the_current_players_road() {
        assertNotEquals(actor, board.getEdge(lastEdgeId).getEdgeOccupant());
    }

    @Then("the current player's road inventory should remain unchanged")
    public void the_current_players_road_inventory_should_remain_unchanged() {
        assertEquals(baselineInventory.get(ROADS), actor.getInventory().get(ROADS));
    }

    @Then("node {int} should still be occupied by the other player")
    public void node_should_still_be_occupied_by_the_other_player(Integer nodeId) {
        assertEquals(otherPlayer, board.getNode(nodeId).getNodeOccupant());
    }

    @Then("the current player's settlement inventory should remain unchanged")
    public void the_current_players_settlement_inventory_should_remain_unchanged() {
        assertEquals(baselineInventory.get(SETTLEMENTS), actor.getInventory().get(SETTLEMENTS));
    }

    @Then("the current player's victory points should remain unchanged")
    public void the_current_players_victory_points_should_remain_unchanged() {
        assertEquals(baselineVictoryPoints, actor.getVictoryPoints());
    }

    @Then("node {int} should not be occupied by the current player's settlement")
    public void node_should_not_be_occupied_by_the_current_players_settlement(Integer nodeId) {
        assertNotEquals(actor, board.getNode(nodeId).getNodeOccupant());
    }

    @Then("every player should have two settlements and two roads on the board")
    public void every_player_should_have_two_settlements_and_two_roads_on_the_board() {
        for (Player player : players) {
            int settlements = 0;
            for (Node node : board.getNodes()) {
                if (player.equals(node.getNodeOccupant()) && node.getInfraType() == InfraType.SETTLEMENT) {
                    settlements++;
                }
            }

            int roads = 0;
            for (Edge edge : board.getEdges()) {
                if (player.equals(edge.getEdgeOccupant())) {
                    roads++;
                }
            }

            assertEquals(2, settlements, "settlements for " + player.getName());
            assertEquals(2, roads, "roads for " + player.getName());
        }
    }

    @Then("each player's victory points should equal two")
    public void each_players_victory_points_should_equal_two() {
        for (Player player : players) {
            assertEquals(2, player.getVictoryPoints());
        }
    }

    @Then("the game should advance to the normal play phase")
    public void the_game_should_advance_to_the_normal_play_phase() {
        assertFalse(game.phaseSetupCheck());
    }

    @Then("each player should receive the resources adjacent to their second settlement")
    public void each_player_should_receive_the_resources_adjacent_to_their_second_settlement() {
        for (Player player : players) {
            List<Integer> placedNodes = settlementNodesByPlayerId.get(player.getId());
            assertNotNull(placedNodes, "no settlements recorded for " + player.getName());
            assertEquals(2, placedNodes.size(), "expected two settlements for " + player.getName());

            int secondSettlementNode = placedNodes.get(1);
            Map<ResourceType, Integer> expected = board.getAdjacentResources(board.getNode(secondSettlementNode));

            assertEquals(expected, player.getResources());
        }
    }

    private Player currentPlayer() {
        return game.getPlayer(turnManager.getCurrentPlayerIndex() - 1);
    }

    private void captureBaseline(Player player) {
        actor = player;
        baselineInventory = player.getInventory();
        baselineResources = player.getResources();
        baselineVictoryPoints = player.getVictoryPoints();
    }

    private void recordSettlement(Player player, int nodeId) {
        settlementNodesByPlayerId.computeIfAbsent(player.getId(), key -> new ArrayList<>()).add(nodeId);
    }

    private Edge firstEdgeConnectedTo(int nodeId) {
        for (Edge edge : board.getEdges()) {
            if (edge.getNodeA().getId() == nodeId || edge.getNodeB().getId() == nodeId) {
                return edge;
            }
        }
        throw new IllegalStateException("No edge connected to node " + nodeId);
    }

    private Edge firstEdgeNotConnectedTo(int nodeId) {
        for (Edge edge : board.getEdges()) {
            if (edge.getNodeA().getId() != nodeId && edge.getNodeB().getId() != nodeId) {
                return edge;
            }
        }
        throw new IllegalStateException("No edge disconnected from node " + nodeId);
    }
}
