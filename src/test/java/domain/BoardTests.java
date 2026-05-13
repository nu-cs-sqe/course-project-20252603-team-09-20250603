package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
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
    public void GetHexesFromNode_NodeNine_ReturnsThreeAdjacentHexes() {
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
    public void GetHexesFromNode_NullNode_ThrowsIllegalStateException() {
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
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


}
