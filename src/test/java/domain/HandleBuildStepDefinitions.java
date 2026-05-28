package domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ui.GameController;

import java.io.StringReader;
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
    private PlacementValidator placementValidator;
    private Player otherPlayer;
    private Exception thrownException;

    private int selectedOptionNumber;
    private int selectedLocationId;
    private int startingInventoryAmount;
    private Map<String, Integer> startingInventory;
    private Map<ResourceType, Integer> startingResources;

    @Given("a game with a current player")
    public void a_game_with_a_current_player() {
        currentPlayer = new Player(0, "Bob", PlayerColor.RED);
        dice = new Dice(new Random());
        turnManager = new TurnManager(3);
        thrownException = null;
    }

    @Given("an initialized board")
    public void an_initialized_board() {
        board = new Board();
        placementValidator = new PlacementValidator(board);
        game = new Game(board, List.of(currentPlayer), dice, turnManager);
    }

    @When("player chooses build option {int}")
    public void player_chooses_to_build_infrastructure(Integer optionNumber) {
        selectedOptionNumber = optionNumber;
        startingInventory = currentPlayer.getInventory();
    }

    @When("enters to build at {word} {int}")
    public void chooses_to_build_at_a_location(String locationType, Integer locationId) {
        selectedLocationId = locationId;
    }

    @When("the game validates that player has the resources needed to build {word}")
    public void the_game_validates_that_player_has_the_resources_needed_to_build(String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);
        Map<ResourceType, Integer> cost = getBuildCost(buildType);

        currentPlayer.addResources(cost);
        startingResources = currentPlayer.getResources();

        assertTrue(currentPlayer.hasResources(cost));

    }

    @When("the game validates that player has at least one {word} in their inventory")
    public void the_game_validates_that_player_has_enough_in_their_inventory(String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);
        String inventoryKey = getInventoryKey(buildType);

        startingInventoryAmount = currentPlayer.getInventory().get(inventoryKey);

        assertTrue(startingInventoryAmount > 0);
    }

    @When("the game validates that {word} {int} is available for building {word}")
    public void the_game_validates_that_location_is_available_for_building(String locationType, Integer locationId, String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);

        if (buildType == BuildType.ROAD){
            Edge edge = board.getEdge(locationId);
            assertNotNull(edge);
            assertNull(edge.getEdgeOccupant());
        }else if(buildType == BuildType.SETTLEMENT) {
            Node node = board.getNode(locationId);
            assertNotNull(node);

            assertDoesNotThrow(() -> placementValidator.validateSettlementPlacement(node));

            assertNull(node.getNodeOccupant());
        }

        tryRunControllerHandleBuild();
    }

    @When("the game validates that node {int} is occupied by another player's settlement")
    public void the_game_validates_that_node_is_occupied_by_another_players_settlement(Integer locationId) {
        otherPlayer = new Player(1, "Other Player", PlayerColor.BLUE);

        Node node = board.getNode(locationId);
        node.buildSettlement(otherPlayer);

        assertEquals(otherPlayer, node.getNodeOccupant());
        assertEquals(InfraType.SETTLEMENT, node.getInfraType());

        startingInventoryAmount = getCurrentInventoryAmount();
        tryRunControllerHandleBuild();
    }

    @Then("the {word} {int} should be occupied by the player's {word}")
    public void the_node_should_be_occupied_by_the_player_s_infrastructure(String locationType, Integer locationId, String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);

        if (buildType == BuildType.ROAD){
            Edge edge = board.getEdge(locationId);
            assertEquals(currentPlayer, edge.getEdgeOccupant());
        }else if(buildType == BuildType.SETTLEMENT) {
            Node node = board.getNode(locationId);
            assertEquals(currentPlayer, node.getNodeOccupant());
            assertEquals(InfraType.SETTLEMENT, node.getInfraType());
        }else if(buildType == BuildType.CITY){
            Node node = board.getNode(locationId);
            assertEquals(currentPlayer, node.getNodeOccupant());
            assertEquals(InfraType.CITY, node.getInfraType());
        }
    }

    @Then("the player's inventory should decrease by one {word}")
    public void the_player_s_inventory_should_decrease_by_one(String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);
        String inventoryKey = getInventoryKey(buildType);

        int expectedInventoryAmount = startingInventoryAmount - 1;
        int actualInventoryAmount = currentPlayer.getInventory().get(inventoryKey);

        assertEquals(expectedInventoryAmount, actualInventoryAmount);
    }

    @Then("the player's resources should decrease by the cost of building {word}")
    public void the_player_s_resources_should_decrease_by_the_cost_of_building(String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);
        Map<ResourceType, Integer> cost = getBuildCost(buildType);
        Map<ResourceType, Integer> actualResources = currentPlayer.getResources();

        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            ResourceType resource = entry.getKey();
            int costAmount = entry.getValue();

            int expectedAmount = startingResources.getOrDefault(resource, 0) - costAmount;
            int actualAmount = actualResources.getOrDefault(resource, 0);

            assertEquals(expectedAmount, actualAmount);
        }
    }

    @When("the game validates that node {int} is occupied by the player's settlement")
    public void the_game_validates_that_node_is_occupied_by_the_player_s_settlement(Integer locationId) {
        Node node = board.getNode(locationId);

        node.buildSettlement(currentPlayer);

        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.SETTLEMENT, node.getInfraType());

        runControllerHandleBuild();
    }

    @Then("node {int} should be occupied by the player's city")
    public void node_should_be_occupied_by_the_player_s_city(Integer locationId) {
        Node node = board.getNode(locationId);
        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.CITY, node.getInfraType());
    }

    @When("the game validates that player does not have any {word} in their inventory")
    public void the_game_validates_that_player_does_not_have_any_in_their_inventory(String buildTypeText) {
        BuildType buildType = toBuildType(buildTypeText);
        String inventoryKey = getInventoryKey(buildType);

        while (currentPlayer.getInventory().get(inventoryKey) > 0) {
            currentPlayer.useInventoryItem(inventoryKey);
        }

        assertEquals(0, currentPlayer.getInventory().get(inventoryKey));
        startingInventory = currentPlayer.getInventory();
    }

    @When("the game validates that {word} {int} is occupied by another player")
    public void the_game_validates_that_location_is_occupied_by_another_player(
            String locationType,
            Integer locationId
    ) {
        otherPlayer = new Player(1, "Other Player", PlayerColor.BLUE);

        if (locationType.equals("edge")) {
            Edge edge = board.getEdge(locationId);
            edge.buildRoad(otherPlayer);
            assertEquals(otherPlayer, edge.getEdgeOccupant());
        } else if (locationType.equals("node")) {
            Node node = board.getNode(locationId);
            node.buildSettlement(otherPlayer);
            assertEquals(otherPlayer, node.getNodeOccupant());
            assertEquals(InfraType.SETTLEMENT, node.getInfraType());
        }

        startingInventoryAmount = getCurrentInventoryAmount();
        tryRunControllerHandleBuild();
    }

    @When("node {int} is occupied by another player")
    public void node_is_occupied_by_another_player(Integer locationId) {
        otherPlayer = new Player(1, "Other Player", PlayerColor.BLUE);

        Node node = board.getNode(locationId);
        node.buildSettlement(otherPlayer);

        assertEquals(otherPlayer, node.getNodeOccupant());
        assertEquals(InfraType.SETTLEMENT, node.getInfraType());

        startingInventoryAmount = getCurrentInventoryAmount();
        tryRunControllerHandleBuild();
    }

    @When("the game validates that node {int} is not occupied by any player's settlement")
    public void the_game_validates_that_node_is_not_occupied_by_any_players_settlement(Integer locationId) {
        Node node = board.getNode(locationId);

        assertNull(node.getNodeOccupant());
        assertNull(node.getInfraType());

        startingInventoryAmount = getCurrentInventoryAmount();
        tryRunControllerHandleBuild();
    }

    @When("the game validates that node {int} is occupied by the player's city")
    public void the_game_validates_that_node_is_occupied_by_the_players_city(Integer locationId) {
        Node node = board.getNode(locationId);

        node.buildSettlement(currentPlayer);
        node.buildCity(currentPlayer);

        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.CITY, node.getInfraType());

        startingInventoryAmount = getCurrentInventoryAmount();
        tryRunControllerHandleBuild();
    }

    @Then("the game should prevent the player from building")
    public void the_game_should_prevent_the_player_from_building() {
        if (thrownException == null) {
            tryRunControllerHandleBuild();
        }

        assertNotNull(thrownException);
    }

    @Then("{word} {int} should not be occupied by the player's {word}")
    public void the_location_should_not_be_occupied_by_the_players_build_type(
            String locationType,
            Integer locationId,
            String buildTypeText
    ) {
        BuildType buildType = toBuildType(buildTypeText);

        if (buildType == BuildType.ROAD) {
            Edge edge = board.getEdge(locationId);
            assertNotEquals(currentPlayer, edge.getEdgeOccupant());
        } else {
            Node node = board.getNode(locationId);
            assertNotEquals(currentPlayer, node.getNodeOccupant());
        }
    }

    @Then("{word} {int} should remain occupied by the other player")
    public void location_should_remain_occupied_by_the_other_player(String locationType, Integer locationId) {
        if (locationType.equals("edge")) {
            Edge edge = board.getEdge(locationId);
            assertEquals(otherPlayer, edge.getEdgeOccupant());
        } else if (locationType.equals("node")) {
            Node node = board.getNode(locationId);
            assertEquals(otherPlayer, node.getNodeOccupant());
        }
    }

    @Then("node {int} should remain unoccupied")
    public void node_should_remain_unoccupied(Integer locationId) {
        Node node = board.getNode(locationId);

        assertNull(node.getNodeOccupant());
        assertNull(node.getInfraType());
    }

    @Then("node {int} should remain occupied by the player's city")
    public void node_should_remain_occupied_by_the_players_city(Integer locationId) {
        Node node = board.getNode(locationId);

        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.CITY, node.getInfraType());
    }

    @Then("the player's inventory should remain unchanged")
    public void the_players_inventory_should_remain_unchanged() {
        assertEquals(startingInventory, currentPlayer.getInventory());
    }

    @Then("the player's resources should remain unchanged")
    public void the_players_resources_should_remain_unchanged() {
        assertEquals(startingResources, currentPlayer.getResources());
    }

    private void runControllerHandleBuild() {
        String input = selectedOptionNumber + "\n" + selectedLocationId + "\n";

        GameController controller = new GameController(game, new StringReader(input));
        controller.handleBuild(currentPlayer);
    }

    private void tryRunControllerHandleBuild() {
        try {
            runControllerHandleBuild();
        } catch (Exception e) {
            thrownException = e;
        }
    }

    private int getCurrentInventoryAmount() {
        BuildType buildType = toBuildType(selectedOptionNumber);
        String inventoryKey = getInventoryKey(buildType);

        return currentPlayer.getInventory().get(inventoryKey);
    }

    private BuildType toBuildType(String buildTypeText) {
        if (buildTypeText.equalsIgnoreCase("road")) {
            return BuildType.ROAD;
        } else if (buildTypeText.equalsIgnoreCase("settlement")) {
            return BuildType.SETTLEMENT;
        } else if (buildTypeText.equalsIgnoreCase("city")) {
            return BuildType.CITY;
        }

        throw new IllegalArgumentException("Invalid build type: " + buildTypeText);
    }

    private BuildType toBuildType(int optionNumber) {
        switch (optionNumber) {
            case 1:
                return BuildType.ROAD;
            case 2:
                return BuildType.SETTLEMENT;
            case 3:
                return BuildType.CITY;
            default:
                throw new IllegalArgumentException("Invalid build option.");
        }
    }

    private String getInventoryKey(BuildType buildType) {
        switch (buildType) {
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

    private Map<ResourceType, Integer> getBuildCost(BuildType buildType) {
        Map<ResourceType, Integer> cost = new HashMap<>();

        switch (buildType) {
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
