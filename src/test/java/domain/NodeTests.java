package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTests {
    @Test
    public void GetNodeOccupant_UnoccupiedNode_Return0() {
        Node n = new Node(1);

        assertNull(n.getNodeOccupant());
    }

    @Test public void BuildSettlement_GetNodeOccupant_GetInfraType_SuccessfulReturnPlayerAndSettlement() {
        Node n = new Node(1);
        Player mockPlayer = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer);

        n.buildSettlement(mockPlayer);
        Player actual = n.getNodeOccupant();
        assertSame(mockPlayer, actual);

        assertEquals(InfraType.SETTLEMENT, n.getInfraType());
    }

    @Test public void BuildSettlement_UnsuccessfulBuild() {
        Node n = new Node(1);
        Player mockPlayer1 = EasyMock.createMock(Player.class);
        Player mockPlayer2 = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer1);
        EasyMock.replay(mockPlayer2);

        // player 1 builds a settlement
        n.buildSettlement(mockPlayer1);

        // player 2 tries to build a settlement
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildSettlement(mockPlayer2)
        );

        assertEquals("Cannot settle on an already-settled node.", exception.getMessage());
        assertSame(mockPlayer1, n.getNodeOccupant());
    }

    @Test public void BuildCity_GetInfraType_SuccessfulUpgradeSettlementToCity() {
        Node n = new Node(1);
        Player mockPlayer = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer);

        n.buildSettlement(mockPlayer);
        n.buildCity(mockPlayer);

        assertEquals(InfraType.CITY, n.getInfraType());
        assertSame(mockPlayer, n.getNodeOccupant());
    }

    @Test public void BuildCity_UnsuccessfulUpgradeCityToCity() {
        Node n = new Node(1);
        Player mockPlayer = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer);

        n.buildSettlement(mockPlayer);
        n.buildCity(mockPlayer);

        // try to build a city on a city
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildCity(mockPlayer)
        );

        assertEquals(InfraType.CITY, n.getInfraType());
        assertSame(mockPlayer, n.getNodeOccupant());
        assertEquals("Cannot upgrade a city further.", exception.getMessage());
    }

    @Test public void BuildCity_GetInfraType_UnsuccessfulUpdateEmptyNodeToCity() {
        Node n = new Node(1);
        Player mockPlayer = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer);

        // try to build a city on an unoccupied node
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildCity(mockPlayer)
        );

        assertNull(n.getInfraType());
        assertNull(n.getNodeOccupant());
        assertEquals("Cannot upgrade an unsettled node to city.", exception.getMessage());
    }

    @Test public void BuildCity_UnsuccessfulUpgradeOpponentToCity() {
        Node n = new Node(1);
        Player mockPlayer1 = EasyMock.createMock(Player.class);
        Player mockPlayer2 = EasyMock.createMock(Player.class);

        EasyMock.replay(mockPlayer1);
        EasyMock.replay(mockPlayer2);

        n.buildSettlement(mockPlayer1);
        n.buildCity(mockPlayer1);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> n.buildCity(mockPlayer2)
        );

        assertEquals(mockPlayer1, n.getNodeOccupant());
        assertEquals("Cannot build a city on an already-settled node.", exception.getMessage());
    }
}
