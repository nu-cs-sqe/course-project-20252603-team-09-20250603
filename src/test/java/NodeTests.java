import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeTests {
    @Test
    public void getNodeOccupant_UnoccupiedNode_Return0() {
        Node n = new Node(1);

        int expected = 0;
        int actual = n.getNodeOccupant();
        assertEquals(expected, actual);
    }

    @Test public void buildSettlement_getNodeOccupant_getInfraType_Player1_Return1AndSettlement() {
        Node n = new Node(1);

        n.buildSettlement(1);
        int expected = 1;
        int actual = n.getNodeOccupant();
        assertEquals(expected, actual);

        assertEquals("settlement", n.getInfraType());
    }
}
