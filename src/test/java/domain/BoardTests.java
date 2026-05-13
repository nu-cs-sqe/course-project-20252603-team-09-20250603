package domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

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


}
