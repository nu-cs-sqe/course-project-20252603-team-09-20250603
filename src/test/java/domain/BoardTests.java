package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    public void GetHexesFromNode_NodeZero_ReturnsOneHex() {
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
    public void GetHexesFromNode_NullNode_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> board.getHexesFromNode(null)
        );

        assertEquals("The node object is null", exception.getMessage());
    }

    @Test
    public void GetHexesFromNode_InvalidNodeId_ThrowsIllegalStateException() {
        Node nodeNegative = new Node(-1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getHexesFromNode(nodeNegative)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void GetHexesFromNode_InvalidNodeId2_ThrowsIllegalStateException() {
        Node node54 = new Node(54);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getHexesFromNode(node54)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void GetHexesFromNode_NodeMax_ReturnsOneHex() {
        Node node0 = new Node(53);

        List<Hex> hexes = board.getHexesFromNode(node0);

        assertEquals(1, hexes.size());
        assertEquals(18, hexes.get(0).getId());
    }

    @Test
    public void GetAdjacentResources_NodeMin_ReturnsOneResource() {
        Node node0 = new Node(0);

        Map<ResourceType, Integer> resources = board.getAdjacentResources(node0);

        assertEquals(1, resources.size());
        assertEquals(1, resources.get(ResourceType.BRICK));
    }

    @Test
    public void GetAdjacentResources_NodeMax_ReturnsOneResource() {
        Node node0 = new Node(53);

        Map<ResourceType, Integer> resources = board.getAdjacentResources(node0);

        assertEquals(1, resources.size());
        assertEquals(1, resources.get(ResourceType.ORE));
    }

    @Test
    public void GetAdjacentResources_NegativeNodeId_ThrowsIllegalStateException() {
        Node nodeNegative = new Node(-1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> board.getAdjacentResources(nodeNegative)
        );

        assertEquals("The node object is not valid", exception.getMessage());
    }

    @Test
    public void GetAdjacentResources_InvalidNodeId_ThrowsIllegalStateException() {
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
}
