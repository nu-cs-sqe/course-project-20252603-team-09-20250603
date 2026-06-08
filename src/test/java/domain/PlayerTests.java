package domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void addResources_emptyHand_addOneWood_returnsWoodOne() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.WOOD, 1);

        player.addResources(resources);

        Map<ResourceType, Integer> expected = new HashMap<>();
        expected.put(ResourceType.WOOD, 1);

        assertEquals(expected, player.getResources());
    }

    @Test
    public void addResources_emptyHand_addNineteenBrick_returnsBrickNineteen() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.BRICK, 19);

        player.addResources(resources);

        Map<ResourceType, Integer> expected = new HashMap<>();
        expected.put(ResourceType.BRICK, 19);

        assertEquals(expected, player.getResources());
    }

    @Test
    public void addResources_emptyHand_addSheepAndWheat_returnsSheepOneAndWheatOne() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.SHEEP, 1);
        resources.put(ResourceType.WHEAT, 1);

        player.addResources(resources);

        Map<ResourceType, Integer> expected = new HashMap<>();
        expected.put(ResourceType.SHEEP, 1);
        expected.put(ResourceType.WHEAT, 1);

        assertEquals(expected, player.getResources());
    }

    @Test
    public void addResources_emptyHand_addDesert_returnsEmptyHand() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.DESERT, 1);

        player.addResources(resources);

        Map<ResourceType, Integer> expected = new HashMap<>();

        assertEquals(expected, player.getResources());
    }

    @Test
    public void addResources_existingOre_addSheepAndOre_returnsSheepOneAndOreTwo() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> startingResources = new HashMap<>();
        startingResources.put(ResourceType.ORE, 1);

        player.addResources(startingResources);

        Map<ResourceType, Integer> expectedBefore = new HashMap<>();
        expectedBefore.put(ResourceType.ORE, 1);

        assertEquals(expectedBefore, player.getResources());

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.SHEEP, 1);
        resourcesToAdd.put(ResourceType.ORE, 1);

        player.addResources(resourcesToAdd);

        Map<ResourceType, Integer> expectedAfter = new HashMap<>();
        expectedAfter.put(ResourceType.SHEEP, 1);
        expectedAfter.put(ResourceType.ORE, 2);

        assertEquals(expectedAfter, player.getResources());
    }

    @Test
    public void addResources_existingBrick_addDesert_returnsBrickTwo() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> startingResources = new HashMap<>();
        startingResources.put(ResourceType.BRICK, 2);

        player.addResources(startingResources);

        Map<ResourceType, Integer> expectedBefore = new HashMap<>();
        expectedBefore.put(ResourceType.BRICK, 2);

        assertEquals(expectedBefore, player.getResources());

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.DESERT, 1);

        player.addResources(resourcesToAdd);

        Map<ResourceType, Integer> expectedAfter = new HashMap<>();
        expectedAfter.put(ResourceType.BRICK, 2);

        assertEquals(expectedAfter, player.getResources());
    }


    @Test
    public void addResources_existingWood_addWoodOreAndBrick_returnsWoodFourOreOneAndBrickTwo() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> startingResources = new HashMap<>();
        startingResources.put(ResourceType.WOOD, 2);

        player.addResources(startingResources);

        Map<ResourceType, Integer> expectedBefore = new HashMap<>();
        expectedBefore.put(ResourceType.WOOD, 2);

        assertEquals(expectedBefore, player.getResources());

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 2);
        resourcesToAdd.put(ResourceType.ORE, 1);
        resourcesToAdd.put(ResourceType.BRICK, 2);

        player.addResources(resourcesToAdd);

        Map<ResourceType, Integer> expectedAfter = new HashMap<>();
        expectedAfter.put(ResourceType.WOOD, 4);
        expectedAfter.put(ResourceType.ORE, 1);
        expectedAfter.put(ResourceType.BRICK, 2);

        assertEquals(expectedAfter, player.getResources());
    }

    @Test
    public void addResources_existingBrick_addDesert_returnsBrickOne() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> startingResources = new HashMap<>();
        startingResources.put(ResourceType.BRICK, 1);

        player.addResources(startingResources);

        Map<ResourceType, Integer> expectedBefore = new HashMap<>();
        expectedBefore.put(ResourceType.BRICK, 1);

        assertEquals(expectedBefore, player.getResources());

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.DESERT, 1);

        player.addResources(resourcesToAdd);

        Map<ResourceType, Integer> expectedAfter = new HashMap<>();
        expectedAfter.put(ResourceType.BRICK, 1);

        assertEquals(expectedAfter, player.getResources());
    }
    @Test
    public void addResources_existingBrick_addNull_throwsIllegalArgumentException() {
        Player player = new Player(1, "Alice", PlayerColor.RED);

        Map<ResourceType, Integer> startingResources = new HashMap<>();
        startingResources.put(ResourceType.BRICK, 1);

        player.addResources(startingResources);

        Map<ResourceType, Integer> expectedBefore = new HashMap<>();
        expectedBefore.put(ResourceType.BRICK, 1);

        assertEquals(expectedBefore, player.getResources());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> player.addResources(null)
        );

        assertEquals("resources cannot be null", exception.getMessage());
    }

    @Test
    public void hasResources_playerHasExactRequired_returnTrue() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(cost);

        assertTrue(player.hasResources(cost));
    }

    @Test
    public void hasResources_playerHasMoreThanRequired_returnTrue() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 2);
        resourcesToAdd.put(ResourceType.BRICK, 3);
        resourcesToAdd.put(ResourceType.ORE, 2);
        resourcesToAdd.put(ResourceType.SHEEP, 2);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(resourcesToAdd);

        assertTrue(player.hasResources(cost));
    }

    @Test
    public void hasResources_playerHasMissingFromRequired_returnFalse() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 2);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(resourcesToAdd);

        assertFalse(player.hasResources(cost));
    }

    @Test
    public void hasResources_playerHasLessThanRequired_returnFalse() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 1);
        resourcesToAdd.put(ResourceType.BRICK, 1);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 2);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(resourcesToAdd);

        assertFalse(player.hasResources(cost));
    }

    @Test
    public void hasResources_costIsEmpty_returnTrue() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 1);
        resourcesToAdd.put(ResourceType.BRICK, 1);

        Map<ResourceType, Integer> cost = new HashMap<>();

        player.addResources(resourcesToAdd);

        assertTrue(player.hasResources(cost));
    }

    @Test
    public void hasResources_costIsNUll_returnTrue() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> {
            player.hasResources(null);
        });
    }

    @Test
    public void useResources_playerHasExactRequired_resourcesDecreaseToZero() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(cost);
        player.useResources(cost);

        assertEquals(0, player.getResources().getOrDefault(ResourceType.BRICK, 0));
        assertEquals(0, player.getResources().getOrDefault(ResourceType.WOOD, 0));

    }

    @Test
    public void useResources_playerHasMoreThanRequired_resourcesDecreaseCorrectly() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 2);
        resourcesToAdd.put(ResourceType.BRICK, 3);
        resourcesToAdd.put(ResourceType.ORE, 2);
        resourcesToAdd.put(ResourceType.SHEEP, 2);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 1);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(resourcesToAdd);
        player.useResources(cost);

        assertEquals(2, player.getResources().getOrDefault(ResourceType.BRICK, 0));
        assertEquals(1, player.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertEquals(2, player.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(2, player.getResources().getOrDefault(ResourceType.SHEEP, 0));

    }

    @Test
    public void useResources_playerHasMissingFromRequired_resourcesUnchanged_returnException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 2);
        cost.put(ResourceType.BRICK, 1);

        player.addResources(resourcesToAdd);
        Map<ResourceType, Integer> startingResources = player.getResources();

        assertThrows(IllegalStateException.class, () -> player.useResources(cost));

        assertEquals(startingResources, player.getResources());
    }

    @Test
    public void useResources_playerHasLessThanRequired_resourcesUnchanged_returnException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 2);

        player.addResources(resourcesToAdd);
        Map<ResourceType, Integer> startingResources = player.getResources();

        assertThrows(IllegalStateException.class, () -> player.useResources(cost));

        assertEquals(startingResources, player.getResources());
    }

    @Test
    public void useResources_costMapEmpty_resourcesUnchanged() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        Map<ResourceType, Integer> resourcesToAdd = new HashMap<>();
        resourcesToAdd.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> cost = new HashMap<>();

        player.addResources(resourcesToAdd);
        Map<ResourceType, Integer> startingResources = player.getResources();
        player.useResources(cost);

        assertEquals(startingResources, player.getResources());
    }

    @Test
    void useResources_NullCostMap_ThrowsException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> {
            player.useResources(null);
        });
    }

    @Test
    void addVictoryPoints_OnePoint_IncreasesVictoryPoints() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(1);

        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    void addVictoryPoints_PlayerAlreadyHasPoint_AddsCorrectly() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(1);
        player.addVictoryPoints(1);

        assertEquals(2, player.getVictoryPoints());
    }

    @Test
    void addVictoryPoints_ZeroPoints_DoesNotChangeVictoryPoints() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(0);

        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    void addVictoryPoints_NegativePoints_ThrowsException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> {
            player.addVictoryPoints(-1);
        });
    }

    @Test
    void removeVictoryPoints_PlayerHasTwoPointsAndLosesOne_DecreasesToOne() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(2);
        player.removeVictoryPoints(1);

        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    void removeVictoryPoints_PlayerHasTwoPointsAndLosesTwo_DecreasesToZero() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(2);
        player.removeVictoryPoints(2);

        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    void removeVictoryPoints_RemoveZero_PointsStaySame() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(2);
        player.removeVictoryPoints(0);

        assertEquals(2, player.getVictoryPoints());
    }

    @Test
    void removeVictoryPoints_NegativePoints_ThrowsException() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertThrows(IllegalArgumentException.class, () -> {
            player.removeVictoryPoints(-1);
        });
    }

    @Test
    void removeVictoryPoints_RemoveMoreThanPlayerHas_PlayerHasZero() {
        Player player = new Player(0, "Bob", PlayerColor.RED);

        player.addVictoryPoints(1);

        player.removeVictoryPoints(2);

        assertEquals(0, player.getVictoryPoints());
    }



}
