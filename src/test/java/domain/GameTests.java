package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    private Game game;
    private Player player0;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player0 = new Player(0, "Ben", PlayerColor.RED);
        player1 = new Player(1, "Benny", PlayerColor.BLUE);
        player2 = new Player(2, "Benji", PlayerColor.ORANGE);

        game = new Game(
                new Board(),
                List.of(player0, player1, player2),
                new Dice(new Random()),
                new TurnManager(3)
        );
    }

    @Test
    public void getPlayer_FirstPlayer_ReturnsCorrectPlayer() {
        assertEquals(player0, game.getPlayer(0));
    }

    @Test
    public void getPlayer_LastPlayer_ReturnsCorrectPlayer() {
        assertEquals(player2, game.getPlayer(2));
    }

    @Test
    public void getPlayer_MiddlePlayer_ReturnsCorrectPlayer() {
        assertEquals(player1, game.getPlayer(1));
    }

    @Test
    public void getPlayer_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> game.getPlayer(99));
    }


    @Test
    public void phaseSetupCheck_WhenSetup_ReturnsTrue() {
        game.setCurrPhase(GamePhase.SETUP);
        assertTrue(game.phaseSetupCheck());
    }

    @Test
    public void phaseSetupCheck_WhenNormalPlay_ReturnsFalse() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        assertFalse(game.phaseSetupCheck());
    }

    @Test
    public void phaseSetupCheck_WhenGameOver_ReturnsFalse() {
        game.setCurrPhase(GamePhase.GAME_OVER);
        assertFalse(game.phaseSetupCheck());
    }

    @Test // TC-G-08
    public void advancePhase_WhenSetup_BecomesNormalPlayAndDistributesResources() {
        game.setCurrPhase(GamePhase.SETUP);
        placeSetupSettlements(player0, 0, 10);

        game.advancePhase();

        assertFalse(game.phaseSetupCheck());
        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(10)),
                player0.getResources()
        );
    }

    @Test
    public void advancePhase_WhenNormalPlay_BecomesGameOver() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        game.advancePhase();
        assertFalse(game.phaseSetupCheck());
        assertDoesNotThrow(() -> game.advancePhase());
        assertFalse(game.phaseSetupCheck());
    }

    @Test
    public void advancePhase_WhenGameOver_IsNoOp() {
        game.setCurrPhase(GamePhase.GAME_OVER);
        assertDoesNotThrow(() -> game.advancePhase());
        assertFalse(game.phaseSetupCheck());
    }

    @Test
    public void distributeSetupResources_UsesOnlySecondSetupSettlement() {
        game.setCurrPhase(GamePhase.SETUP);
        placeSetupSettlements(player0, 0, 10);

        game.distributeSetupResources();

        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(10)),
                player0.getResources()
        );
    }

    @Test // TC-G-12
    public void distributeSetupResources_OnlyFirstSettlement_GivesNoResources() {
        game.setCurrPhase(GamePhase.SETUP);
        game.build(player0, InfraType.SETTLEMENT, 0);

        game.distributeSetupResources();

        assertTrue(player0.getResources().isEmpty());
    }

    @Test // TC-G-13
    public void distributeSetupResources_ExcludesFirstSettlementResources() {
        game.setCurrPhase(GamePhase.SETUP);
        placeSetupSettlements(player0, 0, 10);

        game.distributeSetupResources();

        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(10)),
                player0.getResources()
        );
        assertNotEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(0)),
                player0.getResources()
        );
    }

    @Test // TC-G-14
    public void distributeSetupResources_MultiplePlayers_EachReceivesOwnSecondSettlementResources() {
        game.setCurrPhase(GamePhase.SETUP);

        int[] player0Nodes = placeSetupSettlements(player0);
        int[] player1Nodes = placeSetupSettlements(player1);
        int[] player2Nodes = placeSetupSettlements(player2);

        game.distributeSetupResources();

        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(player0Nodes[1])),
                player0.getResources()
        );
        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(player1Nodes[1])),
                player1.getResources()
        );
        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(player2Nodes[1])),
                player2.getResources()
        );
    }

    @Test // TC-G-15
    public void distributeSetupResources_NoSecondSettlements_AllHandsRemainEmpty() {
        game.distributeSetupResources();

        assertTrue(player0.getResources().isEmpty());
        assertTrue(player1.getResources().isEmpty());
        assertTrue(player2.getResources().isEmpty());
    }

    @Test // TC-G-16
    public void distributeSetupResources_DesertHexAdjacent_DoesNotAddDesertToHand() {
        game.setCurrPhase(GamePhase.SETUP);
        game.build(player0, InfraType.SETTLEMENT, findValidSettlementNode());
        int desertNodeId = findNodeAdjacentToDesert();
        game.build(player0, InfraType.SETTLEMENT, desertNodeId);

        game.distributeSetupResources();

        assertFalse(player0.getResources().containsKey(ResourceType.DESERT));
    }

    // --- build() placement, inventory & resource rules ---
    // (mirrors handle_build.feature, as JUnit so pitest can see it and kill the build() mutants)

    @Test // TC-GB-01: a valid, connected road is actually placed (kills removed Edge::buildRoad)
    public void build_validConnectedRoad_placesRoadForPlayer() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(roadCost());
        game.getBoard().getEdge(1).getNodeA().buildSettlement(player0);

        game.build(player0, InfraType.ROAD, 1);

        assertEquals(player0, game.getBoard().getEdge(1).getEdgeOccupant());
    }

    @Test // TC-GB-02: a successful build consumes one inventory item (kills removed useInventoryItem)
    public void build_validRoad_decrementsRoadInventory() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(roadCost());
        game.getBoard().getEdge(1).getNodeA().buildSettlement(player0);
        int before = player0.getInventory().get("roads");

        game.build(player0, InfraType.ROAD, 1);

        assertEquals(before - 1, (int) player0.getInventory().get("roads"));
    }

    @Test // TC-GB-03: a successful build deducts the resource cost (kills removed useResources)
    public void build_validRoad_deductsResourceCost() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(roadCost());
        game.getBoard().getEdge(1).getNodeA().buildSettlement(player0);

        game.build(player0, InfraType.ROAD, 1);

        Map<ResourceType, Integer> resources = player0.getResources();
        assertEquals(0, (int) resources.getOrDefault(ResourceType.BRICK, 0));
        assertEquals(0, (int) resources.getOrDefault(ResourceType.WOOD, 0));
    }

    @Test // TC-GB-04: zero inventory is rejected before placement (kills the "<= 0" boundary)
    public void build_zeroRoadInventory_rejectedBeforePlacement() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(roadCost());
        game.getBoard().getEdge(1).getNodeA().buildSettlement(player0);
        drainInventory(player0, "roads");

        assertThrows(IllegalStateException.class, () -> game.build(player0, InfraType.ROAD, 1));
        assertNull(game.getBoard().getEdge(1).getEdgeOccupant());
    }

    @Test // TC-GB-05: insufficient resources rejected before placement (kills resource conditional)
    public void build_noResources_rejectedBeforePlacement() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        game.getBoard().getEdge(1).getNodeA().buildSettlement(player0); // connected, but no resources

        assertThrows(IllegalStateException.class, () -> game.build(player0, InfraType.ROAD, 1));
        assertNull(game.getBoard().getEdge(1).getEdgeOccupant());
    }

    @Test // TC-GB-06: regular road not connected to own network is rejected (kills removed validateRegularRoad)
    public void build_roadNotConnectedToOwnNetwork_rejected() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(roadCost());

        assertThrows(IllegalStateException.class, () -> game.build(player0, InfraType.ROAD, 1));
        assertNull(game.getBoard().getEdge(1).getEdgeOccupant());
    }

    @Test // TC-GB-07: settlement violating the distance rule is rejected (kills removed validateSettlementPlacement)
    public void build_settlementViolatesDistanceRule_rejectedAndNotPlaced() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(settlementCost());
        Node target = game.getBoard().getNode(0);
        adjacentNode(game.getBoard(), target).buildSettlement(player1);

        assertThrows(IllegalStateException.class, () -> game.build(player0, InfraType.SETTLEMENT, 0));
        assertNull(target.getNodeOccupant());
    }

    @Test // TC-GB-08: setup road must connect to the just-placed settlement (kills removed validateInitialRoad)
    public void build_setupInitialRoad_unconnectedRejected_connectedSucceeds() {
        // game defaults to GamePhase.SETUP
        game.build(player0, InfraType.SETTLEMENT, 0);

        int unconnected = edgeNotTouchingNode(game.getBoard(), 0);
        assertThrows(IllegalStateException.class, () -> game.build(player0, InfraType.ROAD, unconnected));
        assertNull(game.getBoard().getEdge(unconnected).getEdgeOccupant());

        Edge connected = game.getBoard().getEdgesConnectedToNode(game.getBoard().getNode(0)).get(0);
        game.build(player0, InfraType.ROAD, connected.getId());
        assertEquals(player0, connected.getEdgeOccupant());
    }

    @Test
    public void build_NullInfraType_ThrowsIllegalArgumentException() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> game.build(player0, null, 0));
        assertEquals("Build type cannot be null", ex.getMessage());
    }

    @Test
    public void build_SetupRoadBeforeAnySettlement_ThrowsIllegalStateException() {
        int someEdge = 0;

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> game.build(player0, InfraType.ROAD, someEdge));

        assertEquals("You must build a settlement before building a road during setup.", ex.getMessage());
        assertNull(game.getBoard().getEdge(someEdge).getEdgeOccupant());
    }

    @Test
    public void build_CityOnEmptyNode_ThrowsIllegalStateException() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        player0.addResources(cityCost());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> game.build(player0, InfraType.CITY, 0));

        assertEquals("Cannot upgrade an unsettled node to city.", ex.getMessage());
        assertNull(game.getBoard().getNode(0).getInfraType());
    }

    private int[] placeSetupSettlements(Player player) {
        int firstNodeId = findValidSettlementNode();
        game.build(player, InfraType.SETTLEMENT, firstNodeId);
        int secondNodeId = findValidSettlementNode();
        game.build(player, InfraType.SETTLEMENT, secondNodeId);
        return new int[] {firstNodeId, secondNodeId};
    }

    private void placeSetupSettlements(Player player, int firstNodeId, int secondNodeId) {
        game.build(player, InfraType.SETTLEMENT, firstNodeId);
        game.build(player, InfraType.SETTLEMENT, secondNodeId);
    }

    private int findValidSettlementNode() {
        PlacementValidator validator = new PlacementValidator(game.getBoard());
        for (Node node : game.getBoard().getNodes()) {
            try {
                validator.validateSettlementPlacement(node);
                return node.getId();
            } catch (IllegalPlacementException ignored) {
                // try next node
            }
        }
        throw new IllegalStateException("No valid settlement node found.");
    }

    private int findNodeAdjacentToDesert() {
        PlacementValidator validator = new PlacementValidator(game.getBoard());
        for (Node node : game.getBoard().getNodes()) {
            boolean touchesDesert = false;
            for (Hex hex : game.getBoard().getHexesFromNode(node)) {
                if (hex.getResourceType() == ResourceType.DESERT) {
                    touchesDesert = true;
                    break;
                }
            }
            if (!touchesDesert) {
                continue;
            }
            try {
                validator.validateSettlementPlacement(node);
                return node.getId();
            } catch (IllegalPlacementException ignored) {
                // try next node
            }
        }
        throw new IllegalStateException("No valid desert-adjacent settlement node found.");
    }

    private void drainInventory(Player player, String inventoryKey) {
        while (player.getInventory().get(inventoryKey) > 0) {
            player.useInventoryItem(inventoryKey);
        }
    }

    private Node adjacentNode(Board board, Node node) {
        Edge edge = board.getEdgesConnectedToNode(node).get(0);
        return edge.getNodeA().equals(node) ? edge.getNodeB() : edge.getNodeA();
    }

    private int edgeNotTouchingNode(Board board, int nodeId) {
        for (Edge edge : board.getEdges()) {
            if (edge.getNodeA().getId() != nodeId && edge.getNodeB().getId() != nodeId) {
                return edge.getId();
            }
        }
        throw new IllegalStateException("No edge disconnected from node " + nodeId);
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
