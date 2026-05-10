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
}
