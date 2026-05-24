package domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HandleBuildStepDefinitions {
    private Game game;
    private Board board;
    private Player currentPlayer;
    private Dice dice;
    private TurnManager turnManager;

    private BuildType selectedBuildtype;
    private String selectedLocationType;
    private int selectedLocationId;

    private int startingInventoryAmount;

    @Given("a game with a current player")
    public void a_game_with_a_current_player() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("an initialized board")
    public void an_initialized_board() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("player chooses to build settlement")
    public void player_chooses_to_build_settlement() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("chooses to build at location node {int}")
    public void chooses_to_build_at_location_node(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the game validates that player has the resources needed to build settlement")
    public void the_game_validates_that_player_has_the_resources_needed_to_build_settlement() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the game validates that player has at least one settlement in their inventory")
    public void the_game_validates_that_player_has_at_least_one_settlement_in_their_inventory() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the game validates that node {int} is available for building settlement")
    public void the_game_validates_that_node_is_available_for_building_settlement(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the node {int} should be occupied by the player's settlement")
    public void the_node_should_be_occupied_by_the_player_s_settlement(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the player's inventory should decrease by one settlement")
    public void the_player_s_inventory_should_decrease_by_one_settlement() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the player's resources should decrease by the cost of building settlement")
    public void the_player_s_resources_should_decrease_by_the_cost_of_building_settlement() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("player chooses to build a city")
    public void player_chooses_to_build_a_city() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("chooses to build at node {int}")
    public void chooses_to_build_at_node(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the game validates that player has the resources needed to build city")
    public void the_game_validates_that_player_has_the_resources_needed_to_build_city() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the game validates that player has at least one city in their inventory")
    public void the_game_validates_that_player_has_at_least_one_city_in_their_inventory() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("the game validates that node {int} is occupied by the player's settlement")
    public void the_game_validates_that_node_is_occupied_by_the_player_s_settlement(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("node {int} should be occupied by the player's city")
    public void node_should_be_occupied_by_the_player_s_city(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the player's inventory should decrease by one city")
    public void the_player_s_inventory_should_decrease_by_one_city() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the player's resources should decrease by the cost of building a city")
    public void the_player_s_resources_should_decrease_by_the_cost_of_building_a_city() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
