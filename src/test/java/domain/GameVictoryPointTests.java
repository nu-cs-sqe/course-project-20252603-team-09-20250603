package domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameVictoryPointTests {
    @Test
    void buildRoad_SuccessfulBuild_DoesNotChangeVictoryPoints() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        player.addResources(roadCost());

        game.build(player, BuildType.ROAD, 1);

        assertEquals(0, player.getVictoryPoints());
    }

    @Test
    void buildSettlement_SuccessfulBuild_IncreasesVictoryPointsByOne() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        player.addResources(settlementCost());

        game.build(player, BuildType.SETTLEMENT, 1);

        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    void buildCity_SuccessfulUpgrade_IncreasesVictoryPointsByOneMore() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        player.addResources(settlementCost());
        game.build(player, BuildType.SETTLEMENT, 1);

        assertEquals(1, player.getVictoryPoints());

        player.addResources(cityCost());
        game.build(player, BuildType.CITY, 1);

        assertEquals(2, player.getVictoryPoints());
    }

    @Test
    void buildSettlement_NotEnoughResources_DoesNotChangeVictoryPoints() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        player.addVictoryPoints(1);

        assertThrows(IllegalStateException.class, () -> {
            game.build(player, BuildType.SETTLEMENT, 1);
        });

        assertEquals(1, player.getVictoryPoints());
    }

    @Test
    void buildCity_EmptyNode_DoesNotChangeVictoryPoints() {
        Board board = new Board();
        Player player = new Player(0, "Bob", PlayerColor.RED);
        Game game = createGame(board, player);

        player.addVictoryPoints(1);

        player.addResources(cityCost());

        assertThrows(IllegalStateException.class, () -> {
            game.build(player, BuildType.CITY, 1);
        });

        assertEquals(1, player.getVictoryPoints());
    }

    private Game createGame(Board board, Player player) {
        Dice dice = new Dice(new Random());
        TurnManager turnManager = new TurnManager(1);

        return new Game(board, List.of(player), dice, turnManager);
    }

    private Map<ResourceType, Integer> roadCost() {
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.BRICK, 1);
        resources.put(ResourceType.WOOD, 1);

        return resources;
    }

    private Map<ResourceType, Integer> settlementCost() {
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.BRICK, 1);
        resources.put(ResourceType.WOOD, 1);
        resources.put(ResourceType.SHEEP, 1);
        resources.put(ResourceType.WHEAT, 1);

        return resources;
    }

    private Map<ResourceType, Integer> cityCost() {
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.WHEAT, 2);
        resources.put(ResourceType.ORE, 3);

        return resources;
    }
}