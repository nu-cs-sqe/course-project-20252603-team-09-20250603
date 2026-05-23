package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests {
    @Test
    public void getEdgeOccupant_UnoccupiedEdge_ReturnNull() {
        Edge e = new Edge(1);

        assertNull(e.getEdgeOccupant());
    }

    @Test
    public void buildRoad_GetEdgeOccupant_Successful() {
        Edge e = new Edge(1);
        Player mockPlayer = EasyMock.createMock(Player.class);

        e.buildRoad(mockPlayer);
        assertSame(mockPlayer, e.getEdgeOccupant());
    }

    @Test
    public void buildRoad_Unsuccessful_AlreadyOccupied() {
        Edge e = new Edge(1);
        Player mockPlayer1 = EasyMock.createMock(Player.class);
        Player mockPlayer2 = EasyMock.createMock(Player.class);

        e.buildRoad(mockPlayer1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> e.buildRoad(mockPlayer2)
        );

        assertEquals("Cannot build a road on an occupied edge.", exception.getMessage());
        assertSame(mockPlayer1, e.getEdgeOccupant());
    }
}
