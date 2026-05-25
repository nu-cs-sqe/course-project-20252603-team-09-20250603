package domain;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTests {
    @Test
    void getName_NoSpaces_ReturnsCorrectly(){
        Player player = new Player(0,"Bob", PlayerColor.RED);

        assertEquals("Bob", player.getName());
    }

    @Test
    void getName_WithSpaces_ReturnsCorrectly(){
        Player player = new Player(0,"Jane Doe", PlayerColor.RED);

        assertEquals("Jane Doe", player.getName());
    }

    @Test
    void getColor_Red_ReturnsCorrectly(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertEquals(PlayerColor.RED, player.getColor());
    }

    @Test
    void getColor_BLUE_ReturnsCorrectly(){
        Player player = new Player(1, "Jane Doe", PlayerColor.BLUE);

        assertEquals(PlayerColor.BLUE, player.getColor());
    }

    @Test
    void getStartingInventory_ReturnsCorrectly(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertEquals(15, player.getInventory().get("roads"));
        assertEquals(5, player.getInventory().get("settlements"));
        assertEquals(4, player.getInventory().get("cities"));
    }

    @Test
    void getStartingNewPlayerVictoryPoints_ReturnsZero(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    void getInventory_AfterUsingRoad_ReturnsUpdatedRoadCount(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("roads");

        assertEquals(14, player.getInventory().get("roads"));
        assertEquals(5, player.getInventory().get("settlements"));
        assertEquals(4, player.getInventory().get("cities"));
    }

    @Test
    void getInventory_AfterUsingSettlement_ReturnsUpdatedSettlementCount() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("settlements");

        assertEquals(15, player.getInventory().get("roads"));
        assertEquals(4, player.getInventory().get("settlements"));
        assertEquals(4, player.getInventory().get("cities"));
    }

    @Test
    void getInventory_AfterUsingCity_ReturnsUpdatedCityCount() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("cities");

        assertEquals(15, player.getInventory().get("roads"));
        assertEquals(5, player.getInventory().get("settlements"));
        assertEquals(3, player.getInventory().get("cities"));
    }

    @Test
    void getInventory_AfterUsingOneOfEach_ReturnsUpdatedInventory() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("roads");
        player.useInventoryItem("settlements");
        player.useInventoryItem("cities");

        assertEquals(14, player.getInventory().get("roads"));
        assertEquals(4, player.getInventory().get("settlements"));
        assertEquals(3, player.getInventory().get("cities"));
    }

    @Test
    void getInventory_ModifyingReturnedMap_DoesNotChangePlayerInventory() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<String, Integer> inventoryCopy = player.getInventory();
        inventoryCopy.put("roads", 0);
        inventoryCopy.put("settlements", 0);
        inventoryCopy.put("cities", 0);

        assertEquals(15, player.getInventory().get("roads"));
        assertEquals(5, player.getInventory().get("settlements"));
        assertEquals(4, player.getInventory().get("cities"));
    }

    @Test
    void useInventoryItem_Road_DecreasesRoadInventoryByOne() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("roads");

        assertEquals(14, player.getInventory().get("roads"));
    }

    @Test
    void useInventoryItem_Settlement_DecreasesSettlementInventoryByOne() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("settlements");

        assertEquals(4, player.getInventory().get("settlements"));
    }

    @Test
    void useInventoryItem_City_DecreasesCityInventoryByOne() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("cities");

        assertEquals(3, player.getInventory().get("cities"));
    }

    @Test
    void useInventoryItem_WhenOneItemRemaining_DecreasesToZero() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("cities");
        player.useInventoryItem("cities");
        player.useInventoryItem("cities");
        player.useInventoryItem("cities");

        assertEquals(0, player.getInventory().get("cities"));
    }

    @Test
    void useInventoryItem_WhenNoItemsRemaining_ThrowsException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.useInventoryItem("cities");
        player.useInventoryItem("cities");
        player.useInventoryItem("cities");
        player.useInventoryItem("cities");

        assertThrows(IllegalStateException.class, () -> player.useInventoryItem("cities"));

        assertEquals(0, player.getInventory().get("cities"));
    }

    @Test
    void useInventoryItem_InvalidItem_ThrowsException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertThrows(IllegalStateException.class, () -> {
            player.useInventoryItem("ships");
        });

        assertEquals(15, player.getInventory().get("roads"));
        assertEquals(5, player.getInventory().get("settlements"));
        assertEquals(4, player.getInventory().get("cities"));
    }




}
