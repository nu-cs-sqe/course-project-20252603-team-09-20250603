package domain;

import org.junit.jupiter.api.Assertions;
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

        Assertions.assertEquals("settlement", n.getInfraType());
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
        Assertions.assertEquals(1, n.getNodeOccupant());
    }

    @Test public void BuildCity_GetInfraType_SuccessfulUpgradeSettlementToCity() {
        Node n = new Node(1);

        n.buildSettlement(1);
        n.buildCity(1);

        Assertions.assertEquals("city", n.getInfraType());
        Assertions.assertEquals(1, n.getNodeOccupant());
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

        Assertions.assertEquals("city", n.getInfraType());
        Assertions.assertEquals(1, n.getNodeOccupant());
        assertEquals("Cannot upgrade a city further.", exception.getMessage());
    }

    @Test public void BuildCity_GetInfraType_UnsuccessfulUpdateEmptyNodeToCity() {
        Node n = new Node(1);

        // try to build a city on an unoccupied node
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildCity(1)
        );

        Assertions.assertEquals("", n.getInfraType());
        Assertions.assertEquals(0, n.getNodeOccupant());
        assertEquals("Cannot upgrade an unsettled node to city.", exception.getMessage());
    }

    @Test public void BuildCity_UnsuccessfulUpgradeOpponentToCity() {
        Node n = new Node(1);

        n.buildSettlement(1);
        n.buildCity(1);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildCity(2)
        );

        Assertions.assertEquals(1, n.getNodeOccupant());
        assertEquals("Cannot build a city on an already-settled node.", exception.getMessage());
    }
}
