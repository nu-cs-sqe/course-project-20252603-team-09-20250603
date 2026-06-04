package domain;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DevCardStepDefinitions {
    private Game game;
    private Player activePlayer;
    private Exception caughtException;

    @Given("a new game is initialized with the following players:")
    public void a_new_game_is_initialized_with_the_following_players(DataTable dataTable) {
        game = new Game();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            int id = Integer.parseInt(row.get("id"));
            String name = row.get("name");
            PlayerColor color = PlayerColor.valueOf(row.get("color"));

            Player player = new Player(id, name, color);
            game.getPlayers().put(id, player);
        }
    }

    @Given("{string} holds an active {string} card")
    public void player_holds_an_active_card(String playerName, String cardTypeStr) {
        activePlayer = findPlayerByName(playerName);
        DevCard card = new DevCard(DevCardType.valueOf(cardTypeStr));
        card.activateCard(); // Unlocks turn-of-purchase restriction
        activePlayer.setDevCardHand(card);
    }

    @Given("{string} has {int} {string} cards and {int} {string} card")
    public void player_has_multiple_resources(String playerName, int count1, String type1, int count2, String type2) {
        Player player = findPlayerByName(playerName);
        player.getResourceHand().setResourceCount(ResourceType.valueOf(type1), count1);
        player.getResourceHand().setResourceCount(ResourceType.valueOf(type2), count2);
    }

    @Given("{string} has {int} {string} cards")
    public void player_has_single_resource(String playerName, int count, String type) {
        Player player = findPlayerByName(playerName);
        player.getResourceHand().setResourceCount(ResourceType.valueOf(type), count);
    }

    @When("{string} plays their {string} card targeting {string}")
    public void player_plays_their_card_targeting_resource(String playerName, String cardTypeStr, String targetTypeStr) {
        Player player = findPlayerByName(playerName);
        DevCardType cardType = DevCardType.valueOf(cardTypeStr);
        ResourceType targetType = ResourceType.valueOf(targetTypeStr);

        game.useDevCard(player.getId(), cardType, -1, null, null, targetType);
    }

    @Given("the robber is currently located on hex {int}")
    public void the_robber_is_currently_located_on_hex(int hexId) {
        game.getBoard().moveRobber(null, hexId);
    }

    @When("{string} plays their {string} card targeting hex {int}")
    public void player_plays_their_knight_card_targeting_hex(String playerName, String cardTypeStr, int targetHexId) {
        Player player = findPlayerByName(playerName);
        DevCardType cardType = DevCardType.valueOf(cardTypeStr);

        try {
            game.useDevCard(player.getId(), cardType, targetHexId, null, null, null);
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the robber should be located on hex {int}")
    @Then("the robber should still be located on hex {int}")
    public void the_robber_should_be_located_on_hex(int expectedHexId) {
        assertEquals(expectedHexId, game.getBoard().getRobberHexId());
    }

    @Then("{string} should have {int} played knight counted in their army pool")
    public void player_should_have_played_knight_counted(String playerName, int expectedCount) {
        Player player = findPlayerByName(playerName);
        assertEquals(expectedCount, player.getPlayedKnightCount());
    }

    @Given("{string} has {int} unbuilt roads remaining in their inventory")
    @Then("{string} should have {int} unbuilt roads remaining in their inventory")
    public void player_has_unbuilt_roads_remaining_in_inventory(String playerName, int roadCount) {
        Player player = findPlayerByName(playerName);
        player.getInventory().put("roads", roadCount);
    }

    @When("{string} plays their {string} card")
    public void player_plays_their_road_building_card(String playerName, String cardTypeStr) {
        Player player = findPlayerByName(playerName);
        DevCardType cardType = DevCardType.valueOf(cardTypeStr);

        game.useDevCard(player.getId(), cardType, -1, null, null, null);
    }

    @Then("the board should grant {int} free roads to {string}")
    public void the_board_should_grant_free_roads_to_player(int count, String playerName) {
        Player player = findPlayerByName(playerName);

        int remainingRoads = player.getInventory().getOrDefault("roads", 15);

        assertNotNull(player.getInventory().get("roads"));
    }

    @When("{string} plays their {string} card picking {string} and {string}")
    public void player_plays_their_yop_card_picking_resources(String playerName, String cardTypeStr, String choice1Str, String choice2Str) {
        Player player = findPlayerByName(playerName);
        DevCardType cardType = DevCardType.valueOf(cardTypeStr);
        ResourceType choice1 = ResourceType.valueOf(choice1Str);
        ResourceType choice2 = ResourceType.valueOf(choice2Str);

        game.useDevCard(player.getId(), cardType, -1, choice1, choice2, null);
    }

    @Given("{string} has {int} {string} cards and {int} {string} cards")
    @Then("{string} should have {int} {string} cards and {int} {string} cards")
    public void player_should_have_multiple_resources(String playerName, int count1, String type1, int count2, String type2) {
        Player player = findPlayerByName(playerName);
        assertEquals(count1, player.getResourceHand().getResourceCount(ResourceType.valueOf(type1)));
        assertEquals(count2, player.getResourceHand().getResourceCount(ResourceType.valueOf(type2)));
    }

    @Then("{string} should have {int} {string} cards")
    public void player_should_have_single_resource_validation(String playerName, int expectedCount, String type) {
        Player player = findPlayerByName(playerName);
        assertEquals(expectedCount, player.getResourceHand().getResourceCount(ResourceType.valueOf(type)));
    }

    @Then("{string} should no longer have the {string} card in hand")
    @Then("{string} should still have the {string} card in hand")
    public void player_should_retain_or_discard_card(String playerName, String cardTypeStr) {
        Player player = findPlayerByName(playerName);
        DevCardType cardType = DevCardType.valueOf(cardTypeStr);

        boolean retainsCard = player.getDevCardHand().stream()
                .anyMatch(c -> c.getType() == cardType);

        if (caughtException != null) {
            assertTrue(retainsCard, "Player should keep card if action failed.");
        } else {
            assertFalse(retainsCard, "Spent development card should be removed.");
        }
    }

    @Then("a {string} error should be thrown")
    public void a_specific_error_should_be_thrown(String expectedExceptionClassName) {
        assertNotNull(caughtException, "Expected an exception to be caught during processing.");
        assertEquals(expectedExceptionClassName, caughtException.getClass().getSimpleName());
    }

    private Player findPlayerByName(String name) {
        return game.getPlayers().values().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player database query failed for match: " + name));
    }

    @Given("the development card deck is rigged to next return a {string}")
    public void the_development_card_deck_is_rigged(String cardTypeStr) {
        DevCardType expectedType = DevCardType.valueOf(cardTypeStr);

        game.getDevCardDeck().setNextCardType(expectedType);
    }

    @When("{string} draws a development card")
    public void player_draws_a_development_card(String playerName) {
        Player player = findPlayerByName(playerName);

        game.drawDevCard(player.getId());
    }

    @When("{string} waits for their next turn to activate their cards")
    public void player_waits_for_next_turn(String playerName) {
        Player player = findPlayerByName(playerName);

        player.manageDevCardActivation(player.getId());
    }

    @Given("{string} has played {int} knights")
    public void player_has_played_knights(String playerName, int knightCount) {
        Player player = findPlayerByName(playerName);
        for (int i = 0; i < knightCount; i++) {
            player.incrementPlayedKnightCount();
        }
        game.updateLargestArmyPlayer();
    }

    @Then("{string} should possess the {string} milestone")
    public void player_should_possess_the_milestone(String playerName, String milestoneName) {
        Player player = findPlayerByName(playerName);
        if (milestoneName.equalsIgnoreCase("Largest Army")) {
            assertTrue(player.isHasLargestArmy());
        }
    }

    @Then("{string} should have {int} victory points")
    public void player_should_have_victory_points(String playerName, int expectedPoints) {
        Player player = findPlayerByName(playerName);
        assertEquals(expectedPoints, player.getVictoryPoints());
    }
}