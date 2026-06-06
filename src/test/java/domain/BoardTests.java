package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTests {
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void getHexesFromNode_NodeZero_ReturnsOneHex() {
        Node node0 = new Node(0);

        List<Hex> hexList = board.getHexesFromNode(node0);

        assertEquals(1, hexList.size());
        assertEquals(0, hexList.get(0).getId());
    }

    @Test
    public void getHexesFromNode_NodeNine_ReturnsThreeAdjacentHexes() {
        Node node9 = new Node(9);

        List<Hex> hexes = board.getHexesFromNode(node9);

        assertEquals(3, hexes.size());

        List<Integer> hexIds = hexes.stream()
                .map(Hex::getId)
                .collect(Collectors.toList());

        assertTrue(hexIds.contains(3));
        assertTrue(hexIds.contains(0));
        assertTrue(hexIds.contains(4));
    }

    @Test
    public void getHexesFromNode_NullNode_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> board.getHexesFromNode(null)
        );

        assertEquals("The node object is null", exception.getMessage());
    }

    @Test
    public void getHexesFromNode_InvalidNodeId_ThrowsIllegalStateException() {
        Node nodeNegative = new Node(-1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getHexesFromNode(nodeNegative)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void getHexesFromNode_InvalidNodeId2_ThrowsIllegalStateException() {
        Node node54 = new Node(54);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getHexesFromNode(node54)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void getHexesFromNode_NodeMax_ReturnsOneHex() {
        Node node0 = new Node(53);

        List<Hex> hexes = board.getHexesFromNode(node0);

        assertEquals(1, hexes.size());
        assertEquals(18, hexes.get(0).getId());
    }

    @Test
    public void getAdjacentResources_NodeMin_ReturnsOneResource() {
        Node node0 = new Node(0);

        Map<ResourceType, Integer> resources = board.getAdjacentResources(node0);

        assertEquals(1, resources.size());
        assertEquals(1, resources.get(ResourceType.BRICK));
    }

    @Test
    public void getAdjacentResources_NodeMax_ReturnsOneResource() {
        Node node0 = new Node(53);

        Map<ResourceType, Integer> resources = board.getAdjacentResources(node0);

        assertEquals(1, resources.size());
        assertEquals(1, resources.get(ResourceType.ORE));
    }

    @Test
    public void getAdjacentResources_NegativeNodeId_ThrowsIllegalStateException() {
        Node nodeNegative = new Node(-1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getAdjacentResources(nodeNegative)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void getAdjacentResources_InvalidNodeId_ThrowsIllegalStateException() {
        Node node54 = new Node(54);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getAdjacentResources(node54)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void getAdjacentResources_NodeTwo_ReturnsOneBrickAndOneWood() {
        Node node2 = new Node(2);
        Map<ResourceType, Integer> resources = board.getAdjacentResources(node2);

        assertEquals(2, resources.size());

        assertEquals(1, resources.get(ResourceType.BRICK));
        assertEquals(1, resources.get(ResourceType.WOOD));
    }

    @Test
    public void getAdjacentResources_NodeTen_ReturnsOneBrickAndTwoWood() {

        Node node10 = new Node(10);

        Map<ResourceType, Integer> resources =
                board.getAdjacentResources(node10);

        assertEquals(2, resources.size());

        assertEquals(1, resources.get(ResourceType.BRICK));
        assertEquals(2, resources.get(ResourceType.WOOD));
    }

    @Test
    public void getAdjacentResources_NodeFortyFour_ReturnsOneBrickOneWheatAndOneOre() {

        Node node44 = new Node(44);

        Map<ResourceType, Integer> resources =
                board.getAdjacentResources(node44);

        assertEquals(3, resources.size());

        assertEquals(1, resources.get(ResourceType.BRICK));
        assertEquals(1, resources.get(ResourceType.WHEAT));
        assertEquals(1, resources.get(ResourceType.ORE));
    }

    @Test
    public void getAdjacentResources_NodeNineteen_ReturnsOneWoodOneWheatAndOneOre() {

        Node node19 = new Node(19);

        Map<ResourceType, Integer> resources =
                board.getAdjacentResources(node19);

        assertEquals(3, resources.size());

        assertEquals(1, resources.get(ResourceType.WOOD));
        assertEquals(1, resources.get(ResourceType.WHEAT));
        assertEquals(1, resources.get(ResourceType.ORE));
    }

    @Test
    public void getAdjacentResources_MockBoard_ReturnsThreeSheep() {
        Board mockBoard = EasyMock.partialMockBuilder(Board.class)
                .addMockedMethod("getHexesFromNode", Node.class)
                .createMock();


        Node node40 = new Node(40);

        Hex hex1 = new Hex(1);
        Hex hex2 = new Hex(2);
        Hex hex3 = new Hex(3);

        hex1.setResourceType(ResourceType.SHEEP);
        hex2.setResourceType(ResourceType.SHEEP);
        hex3.setResourceType(ResourceType.SHEEP);

        List<Hex> hexes = List.of(hex1, hex2, hex3);

        EasyMock.expect(mockBoard.getHexesFromNode(node40)).andReturn(hexes).once();

        EasyMock.replay(mockBoard);

        Map<ResourceType, Integer> resources =
                mockBoard.getAdjacentResources(node40);

        assertEquals(1, resources.size());

        assertEquals(3, resources.get(ResourceType.SHEEP));

        EasyMock.verify(mockBoard);
    }

    @Test
    public void getAdjacentResources_NodeThirtyTwo_ReturnsOneDesertOneSheepAndOneBrick() {

        Node node32 = new Node(32);

        Map<ResourceType, Integer> resources =
                board.getAdjacentResources(node32);

        assertEquals(3, resources.size());

        assertEquals(1, resources.get(ResourceType.DESERT));
        assertEquals(1, resources.get(ResourceType.SHEEP));
        assertEquals(1, resources.get(ResourceType.BRICK));
    }

    // not a unit test, just a sanity check on edge generation
    @Test
    public void buildEdges_Creates72Edges() {

        Board board = new Board();
        List<Edge> edges = board.getEdges();

        assertEquals(72, edges.size());
    }

    @Test
    void getEdgesConnectedToNode_NullNode_ThrowsException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getEdgesConnectedToNode(null);
        });
    }

    @Test
    void getEdgesConnectedToNode_NodeNotOnBoard_ThrowsException() {
        Board board = new Board();
        Node invalidNode = new Node(999);

        assertThrows(IllegalStateException.class, () -> {
            board.getEdgesConnectedToNode(invalidNode);
        });
    }

    @Test
    void getEdgesConnectedToNode_ValidNode_ReturnsConnectedEdges() {
        Board board = new Board();
        Node node = board.getNode(0);

        List<Edge> connectedEdges = board.getEdgesConnectedToNode(node);

        for (Edge edge : connectedEdges) {
            boolean edgeContainsNode =
                    edge.getNodeA().equals(node) || edge.getNodeB().equals(node);

            assertTrue(edgeContainsNode);
        }
    }

    @Test
    void getEdgesConnectedToNode_BoundaryNode_ReturnsExpectedNumberOfEdges() {
        Board board = new Board();
        Node node = board.getNode(0);

        List<Edge> connectedEdges = board.getEdgesConnectedToNode(node);

        assertEquals(2, connectedEdges.size());
    }

    @Test
    void getEdge_FirstValidEdgeId_ReturnsEdge() {
        Board board = new Board();

        Edge edge = board.getEdge(0);

        assertEquals(0, edge.getId());
    }

    @Test
    void getEdge_LastValidEdgeId_ReturnsEdge() {
        Board board = new Board();
        int lastValidEdgeId = board.getEdges().size() - 1;

        Edge edge = board.getEdge(lastValidEdgeId);

        assertEquals(lastValidEdgeId, edge.getId());
    }

    @Test
    void getEdge_NegativeEdgeId_ThrowsException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getEdge(-1);
        });
    }

    @Test
    void getEdge_EdgeIdEqualToNumberOfEdges_ThrowsException() {
        Board board = new Board();
        int invalidEdgeId = board.getEdges().size();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getEdge(invalidEdgeId);
        });
    }

    @Test
    void getHex_FirstValidHexId_ReturnsHex() {
        Board board = new Board();

        Hex hex = board.getHex(0);

        assertEquals(0, hex.getId());
    }

    @Test
    void getHex_LastValidHexId_ReturnsHex() {
        Board board = new Board();
        int lastValidHexId = board.getHexes().size() - 1;

        Hex hex = board.getHex(lastValidHexId);

        assertEquals(lastValidHexId, hex.getId());
    }

    @Test
    void getHex_NegativeHexId_ThrowsException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getHex(-1);
        });
    }

    @Test
    void getHex_HexIdEqualToNumberOfHexes_ThrowsException() {
        Board board = new Board();
        int invalidHexId = board.getHexes().size();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getHex(invalidHexId);
        });
    }

    @Test
    void getNode_FirstValidNodeId_ReturnsNode() {
        Board board = new Board();

        Node node = board.getNode(0);

        assertEquals(0, node.getId());
    }

    @Test
    void getNode_LastValidNodeId_ReturnsNode() {
        Board board = new Board();
        int lastValidNodeId = board.getNodes().size() - 1;

        Node node = board.getNode(lastValidNodeId);

        assertEquals(lastValidNodeId, node.getId());
    }

    @Test
    void getNode_NegativeNodeId_ThrowsException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getNode(-1);
        });
    }

    @Test
    void getNode_NodeIdEqualToNumberOfNodes_ThrowsException() {
        Board board = new Board();
        int invalidNodeId = board.getNodes().size();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getNode(invalidNodeId);
        });
    }

    @Test
    public void distributeResourcesOnRoll_minRollSettlement_receivesOneWheat() {
        Board board = new Board();
        Player player = new Player(1, "Alice", PlayerColor.RED);

        board.getNode(23).buildSettlement(player);

        board.distributeResourcesOnRoll(2);

        Map<ResourceType, Integer> expected = new HashMap<>();
        expected.put(ResourceType.WHEAT, 1);

        assertEquals(expected, player.getResources());
    }

    @Test
    public void distributeResourcesOnRoll_maxRollCity_receivesTwoBrick() {
        Board board = new Board();
        Player player = new Player(1, "Alice", PlayerColor.RED);

        board.getNode(42).buildSettlement(player);
        board.getNode(42).buildCity(player);

        board.distributeResourcesOnRoll(12);

        Map<ResourceType, Integer> expected = new HashMap<>();
        expected.put(ResourceType.BRICK, 2);

        assertEquals(expected, player.getResources());
    }

    @Test
    public void distributeResourcesOnRoll_rollOne_throwsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.distributeResourcesOnRoll(1);
        });
    }

    @Test
    public void distributeResourcesOnRoll_rollThirteen_throwsIllegalArgumentException() {
        Board board = new Board();

        assertThrows(IllegalArgumentException.class, () -> {
            board.distributeResourcesOnRoll(13);
        });
    }

    @Test
    public void distributeResourcesOnRoll_rollSeven_distributesNoResources() {
        Board board = new Board();
        Player player = new Player(1, "Alice", PlayerColor.RED);

        board.getNode(23).buildSettlement(player);

        board.distributeResourcesOnRoll(7);

        Map<ResourceType, Integer> expected = new HashMap<>();

        assertEquals(expected, player.getResources());
    }

    @Test
    public void distributeResourcesOnRoll_matchingHexNoOccupiedNodes_distributesNoResources() {
        Board board = new Board();
        Player player = new Player(1, "Alice", PlayerColor.RED);

        board.distributeResourcesOnRoll(2);

        Map<ResourceType, Integer> expected = new HashMap<>();

        assertEquals(expected, player.getResources());
    }

    @Test
    public void distributeResourcesOnRoll_multipleProducingHexes_distributesFromAllMatchingHexes() {
        Board board = new Board();
        Player playerOne = new Player(1, "Alice", PlayerColor.RED);
        Player playerTwo = new Player(2, "Bob", PlayerColor.BLUE);

        board.getNode(5).buildSettlement(playerOne);

        board.getNode(48).buildSettlement(playerTwo);
        board.getNode(48).buildCity(playerTwo);

        board.distributeResourcesOnRoll(6);

        Map<ResourceType, Integer> expectedOne = new HashMap<>();
        expectedOne.put(ResourceType.SHEEP, 1);

        Map<ResourceType, Integer> expectedTwo = new HashMap<>();
        expectedTwo.put(ResourceType.WOOD, 2);

        assertEquals(expectedOne, playerOne.getResources());
        assertEquals(expectedTwo, playerTwo.getResources());
    }

    @Test
    public void distributeResourcesOnRoll_multipleOccupiedNodes_allEligiblePlayersReceiveResources() {
        Board board = new Board();
        Player playerOne = new Player(1, "Alice", PlayerColor.RED);
        Player playerTwo = new Player(2, "Bob", PlayerColor.BLUE);

        board.getNode(51).buildSettlement(playerOne);
        board.getNode(44).buildSettlement(playerTwo);

        board.distributeResourcesOnRoll(8);

        Map<ResourceType, Integer> expectedOne = new HashMap<>();
        expectedOne.put(ResourceType.ORE, 1);

        Map<ResourceType, Integer> expectedTwo = new HashMap<>();
        expectedTwo.put(ResourceType.ORE, 1);

        assertEquals(expectedOne, playerOne.getResources());
        assertEquals(expectedTwo, playerTwo.getResources());
    }

    @Test
    public void distributeResourcesOnRoll_matchingHexHasRobber_doesNotProduceFromRobberHex() {
        Board board = new Board();
        Player playerOne = new Player(1, "Alice", PlayerColor.RED);
        Player playerTwo = new Player(2, "Bob", PlayerColor.BLUE);

        board.getHex(9).setHasRobber(false);
        board.getHex(8).setHasRobber(true);

        board.getNode(29).buildSettlement(playerOne);
        board.getNode(29).buildCity(playerOne);

        board.getNode(52).buildSettlement(playerTwo);
        board.getNode(52).buildCity(playerTwo);

        board.distributeResourcesOnRoll(8);

        Map<ResourceType, Integer> expectedOne = new HashMap<>();

        Map<ResourceType, Integer> expectedTwo = new HashMap<>();
        expectedTwo.put(ResourceType.ORE, 2);

        assertEquals(expectedOne, playerOne.getResources());
        assertEquals(expectedTwo, playerTwo.getResources());
    }

}
