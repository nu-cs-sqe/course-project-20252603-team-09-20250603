package domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
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



}
