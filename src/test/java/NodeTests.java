import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NodeTests {
    @Test
    public void GetNodeOccupant_UnoccupiedNode_Return0() {
        Node n = new Node(1);

        int expected = 0;
        int actual = n.getNodeOccupant();
        assertEquals(expected, actual);
    }

    @Test public void BuildSettlement_GetNodeOccupant_GetInfraType_Player1_Return1AndSettlement() {
        Node n = new Node(1);

        n.buildSettlement(1);
        int expected = 1;
        int actual = n.getNodeOccupant();
        assertEquals(expected, actual);

        assertEquals("settlement", n.getInfraType());
    }

    @Test public void BuildSettlement_UnsuccessfulBuild() {
        Node n = new Node(1);

        // player 1 builds a settlement
        n.buildSettlement(1);

        // player 2 tries to build a settlement
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildSettlement(2)
        );

        assertEquals("Cannot settle on an already-settled node.", exception.getMessage());
        assertEquals(1, n.getNodeOccupant());
    }

    @Test public void BuildCity_GetInfraType_SuccessfulUpgradeSettlementToCity() {
        Node n = new Node(1);

        n.buildSettlement(1);
        n.buildCity(1);

        assertEquals("city", n.getInfraType());
        assertEquals(1, n.getNodeOccupant());
    }

    @Test public void BuildCity_UnsuccessfulUpgradeCityToCity() {
        Node n = new Node(1);

        n.buildSettlement(1);
        n.buildCity(1);

        // try to build a city on a city
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildCity(1)
        );

        assertEquals("city", n.getInfraType());
        assertEquals(1, n.getNodeOccupant());
        assertEquals("Cannot upgrade a city further.", exception.getMessage());
    }
}
