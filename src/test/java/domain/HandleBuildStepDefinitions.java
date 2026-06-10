package domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ui.PlayerActionController;
import ui.PlayerActionController.LocationType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HandleBuildStepDefinitions {
    private Game game;
    private Board board;
    private Player currentPlayer;
    private Dice dice;
    private TurnManager turnManager;
    private Player otherPlayer;

    private PlayerActionController controller;

    private Map<String, Integer> startingInventory;
    private Map<ResourceType, Integer> startingResources;

    @Given("a game with a current player")
    public void a_game_with_a_current_player() {
        currentPlayer = new Player(0, "Bob", PlayerColor.RED);
        dice = new Dice(new Random());
        turnManager = new TurnManager(3);
    }

    @Given("an initialized board")
    public void an_initialized_board() {
        board = new Board();
        game = new Game(board, List.of(currentPlayer), dice, turnManager);
        game.setCurrPhase(GamePhase.NORMAL_PLAY);

        controller = new PlayerActionController(List.of(currentPlayer), game, turnManager);
    }


    @Given("the player has the resources to build {word}")
    public void the_player_has_the_resources_to_build(String infraTypeText) {
        Map<ResourceType, Integer> cost = getBuildCost(toInfraType(infraTypeText));
        currentPlayer.addResources(cost);
        assertTrue(currentPlayer.hasResources(cost));
    }

    @Given("the player lacks the resources to build {word}")
    public void the_player_lacks_the_resources_to_build(String infraTypeText) {
        Map<ResourceType, Integer> cost = getBuildCost(toInfraType(infraTypeText));
        assertFalse(currentPlayer.hasResources(cost));
    }

    @Given("the player has at least one {word} in their inventory")
    public void the_player_has_at_least_one_in_their_inventory(String infraTypeText) {
        String inventoryKey = getInventoryKey(toInfraType(infraTypeText));
        assertTrue(currentPlayer.getInventory().get(inventoryKey) > 0);
    }

    @Given("the player has no {word} in their inventory")
    public void the_player_has_no_in_their_inventory(String infraTypeText) {
        String inventoryKey = getInventoryKey(toInfraType(infraTypeText));
        while (currentPlayer.getInventory().get(inventoryKey) > 0) {
            currentPlayer.useInventoryItem(inventoryKey);
        }
        assertEquals(0, (int) currentPlayer.getInventory().get(inventoryKey));
    }

    @Given("{word} {int} is occupied by another player")
    public void location_is_occupied_by_another_player(String locationType, Integer locationId) {
        otherPlayer = new Player(1, "Other Player", PlayerColor.BLUE);

        if (toLocationType(locationType) == LocationType.EDGE) {
            Edge edge = board.getEdge(locationId);
            edge.buildRoad(otherPlayer);
            assertEquals(otherPlayer, edge.getEdgeOccupant());
        } else {
            Node node = board.getNode(locationId);
            node.buildSettlement(otherPlayer);
            assertEquals(otherPlayer, node.getNodeOccupant());
        }
    }

    @Given("node {int} already has the player's settlement")
    public void node_already_has_the_players_settlement(Integer locationId) {
        Node node = board.getNode(locationId);
        node.buildSettlement(currentPlayer);
        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.SETTLEMENT, node.getInfraType());
    }

    @Given("node {int} already has the player's city")
    public void node_already_has_the_players_city(Integer locationId) {
        Node node = board.getNode(locationId);
        node.buildSettlement(currentPlayer);
        node.buildCity(currentPlayer);
        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.CITY, node.getInfraType());
    }

    @Given("the player has a settlement at an endpoint of edge {int}")
    public void the_player_has_a_settlement_at_an_endpoint_of_edge(Integer edgeId) {
        Node endpoint = board.getEdge(edgeId).getNodeA();
        endpoint.buildSettlement(currentPlayer);
        assertEquals(currentPlayer, endpoint.getNodeOccupant());
    }

    @Given("node {int} is empty")
    public void node_is_empty(Integer locationId) {
        Node node = board.getNode(locationId);
        assertNull(node.getNodeOccupant());
        assertNull(node.getInfraType());
    }

    @When("the player clicks Build")
    public void the_player_clicks_build() {
        controller.onActionClicked(PlayerAction.BUILD);
    }

    @When("the player selects {word}")
    public void the_player_selects(String infraTypeText) {
        controller.onBuildTypeSelected(toInfraType(infraTypeText));
    }

    @When("the player clicks {word} {int}")
    public void the_player_clicks_location(String locationType, Integer locationId) {
        controller.onLocationSelected(locationId, toLocationType(locationType));
    }

    @When("the player confirms the build")
    public void the_player_confirms_the_build() {
        startingInventory = currentPlayer.getInventory();
        startingResources = currentPlayer.getResources();
        controller.onBuildConfirmed();
    }

    @Then("{word} {int} should be occupied by the player's {word}")
    public void location_should_be_occupied_by_the_players(
            String locationType, Integer locationId, String infraTypeText) {
        InfraType infraType = toInfraType(infraTypeText);

        if (infraType == InfraType.ROAD) {
            assertEquals(currentPlayer, board.getEdge(locationId).getEdgeOccupant());
        } else {
            Node node = board.getNode(locationId);
            assertEquals(currentPlayer, node.getNodeOccupant());
            assertEquals(infraType, node.getInfraType());
        }
    }

    @Then("the player's inventory should decrease by one {word}")
    public void the_players_inventory_should_decrease_by_one(String infraTypeText) {
        String inventoryKey = getInventoryKey(toInfraType(infraTypeText));
        assertEquals(startingInventory.get(inventoryKey) - 1,
                (int) currentPlayer.getInventory().get(inventoryKey));
    }

    @Then("the player's resources should decrease by the cost of building {word}")
    public void the_players_resources_should_decrease_by_the_cost(String infraTypeText) {
        Map<ResourceType, Integer> cost = getBuildCost(toInfraType(infraTypeText));
        Map<ResourceType, Integer> actual = currentPlayer.getResources();

        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            int expected = startingResources.getOrDefault(entry.getKey(), 0) - entry.getValue();
            assertEquals(expected, (int) actual.getOrDefault(entry.getKey(), 0));
        }
    }

    @Then("the build should be rejected")
    public void the_build_should_be_rejected() {
        assertEquals(startingInventory, currentPlayer.getInventory());
        assertEquals(startingResources, currentPlayer.getResources());
    }

    @Then("{word} {int} should not be occupied by the player's {word}")
    public void location_should_not_be_occupied_by_the_players(
            String locationType, Integer locationId, String infraTypeText) {
        InfraType infraType = toInfraType(infraTypeText);

        if (infraType == InfraType.ROAD) {
            assertNotEquals(currentPlayer, board.getEdge(locationId).getEdgeOccupant());
        } else {
            assertNotEquals(currentPlayer, board.getNode(locationId).getNodeOccupant());
        }
    }

    @Then("{word} {int} should remain occupied by the other player")
    public void location_should_remain_occupied_by_the_other_player(String locationType, Integer locationId) {
        if (toLocationType(locationType) == LocationType.EDGE) {
            assertEquals(otherPlayer, board.getEdge(locationId).getEdgeOccupant());
        } else {
            assertEquals(otherPlayer, board.getNode(locationId).getNodeOccupant());
        }
    }

    @Then("node {int} should remain unoccupied")
    public void node_should_remain_unoccupied(Integer locationId) {
        Node node = board.getNode(locationId);
        assertNull(node.getNodeOccupant());
        assertNull(node.getInfraType());
    }

    @Then("the player's inventory should remain unchanged")
    public void the_players_inventory_should_remain_unchanged() {
        assertEquals(startingInventory, currentPlayer.getInventory());
    }

    @Then("the player's resources should remain unchanged")
    public void the_players_resources_should_remain_unchanged() {
        assertEquals(startingResources, currentPlayer.getResources());
    }

    private LocationType toLocationType(String locationType) {
        if (locationType.equalsIgnoreCase("edge")) {
            return LocationType.EDGE;
        } else if (locationType.equalsIgnoreCase("node")) {
            return LocationType.NODE;
        }
        throw new IllegalArgumentException("Invalid location type: " + locationType);
    }

    private InfraType toInfraType(String infraTypeText) {
        if (infraTypeText.equalsIgnoreCase("road")) {
            return InfraType.ROAD;
        } else if (infraTypeText.equalsIgnoreCase("settlement")) {
            return InfraType.SETTLEMENT;
        } else if (infraTypeText.equalsIgnoreCase("city")) {
            return InfraType.CITY;
        }
        throw new IllegalArgumentException("Invalid build type: " + infraTypeText);
    }

    private String getInventoryKey(InfraType infraType) {
        switch (infraType) {
            case ROAD:
                return "roads";
            case SETTLEMENT:
                return "settlements";
            case CITY:
                return "cities";
            default:
                throw new IllegalArgumentException("Invalid build type.");
        }
    }

    private Map<ResourceType, Integer> getBuildCost(InfraType infraType) {
        Map<ResourceType, Integer> cost = new HashMap<>();

        switch (infraType) {
            case ROAD:
                cost.put(ResourceType.BRICK, 1);
                cost.put(ResourceType.WOOD, 1);
                break;
            case SETTLEMENT:
                cost.put(ResourceType.BRICK, 1);
                cost.put(ResourceType.WOOD, 1);
                cost.put(ResourceType.SHEEP, 1);
                cost.put(ResourceType.WHEAT, 1);
                break;
            case CITY:
                cost.put(ResourceType.WHEAT, 2);
                cost.put(ResourceType.ORE, 3);
                break;
            default:
                throw new IllegalArgumentException("Invalid build type.");
        }

        return cost;
    }
}
