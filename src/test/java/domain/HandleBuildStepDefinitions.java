package domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HandleBuildStepDefinitions {
    private Game game;
    private Board board;
    private Player currentPlayer;
    private Dice dice;
    private TurnManager turnManager;

    private BuildType selectedBuildtype;
    private int selectedLocationId;

    private int startingInventoryAmount;

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
    }

    @When("player chooses to build {word}")
    public void player_chooses_to_build_infrastructure(String buildType ) {
        selectedBuildtype = toBuildType(buildType);
    }

    @When("chooses to build at location {word} {int}")
    public void chooses_to_build_at_a_location(String locationType, Integer locationId) {
        selectedLocationId = locationId;
    }

    @When("the game validates that player has the resources needed to build {word}")
    public void the_game_validates_that_player_has_the_resources_needed_to_build(String buildTypeText) {
        //BuildType buildType = toBuildType(buildTypeText);

        // TODO: Implement once resources are added to Player.
        // For now, this step documents the expected validation.
        // Later, this should give/check the player has:
        // ROAD: 1 brick, 1 wood
        // SETTLEMENT: 1 brick, 1 wood, 1 sheep, 1 wheat
        // CITY: 2 wheat, 3 ore
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
            assertNull(node.getNodeOccupant());
        }

        game.build(currentPlayer, selectedBuildtype, selectedLocationId);
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
        //BuildType buildType = toBuildType(buildTypeText);

        // TODO: Implement once resources are added to Player.
        // Later, this should check that the correct resources were deducted.
    }

    @When("player chooses to build a city")
    public void player_chooses_to_build_a_city() {
        player_chooses_to_build_infrastructure("city");
    }

    @When("chooses to build at node {int}")
    public void chooses_to_build_at_node(Integer locationId) {
        selectedLocationId = locationId;
    }

    @When("the game validates that node {int} is occupied by the player's settlement")
    public void the_game_validates_that_node_is_occupied_by_the_player_s_settlement(Integer locationId) {
        Node node = board.getNode(locationId);

        node.buildSettlement(currentPlayer);

        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.SETTLEMENT, node.getInfraType());

        game.build(currentPlayer, selectedBuildtype, selectedLocationId);
    }

    @Then("node {int} should be occupied by the player's city")
    public void node_should_be_occupied_by_the_player_s_city(Integer locationId) {
        Node node = board.getNode(locationId);
        assertEquals(currentPlayer, node.getNodeOccupant());
        assertEquals(InfraType.CITY, node.getInfraType());
    }

    @Then("the player's resources should decrease by the cost of building a city")
    public void the_player_s_resources_should_decrease_by_the_cost_of_building_a_city() {
        the_player_s_resources_should_decrease_by_the_cost_of_building("city");
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

}
