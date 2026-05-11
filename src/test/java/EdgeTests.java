import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EdgeTests {
    @Test
    public void GetEdgeOccupant_UnoccupiedEdge_Return0() {
        Edge e = new Edge(1);

        int expected = 0;
        int actual = e.getEdgeOccupant();
        assertEquals(expected, actual);
    }

    @Test
    public void BuildRoad_GetEdgeOccupant_Successful() {
        Edge e = new Edge(1);

        e.buildRoad(1);
        assertEquals(1, e.getEdgeOccupant());
    }

    @Test
    public void BuildRoad_Unsuccessful_AlreadyOccupied() {
        Edge e = new Edge(1);

        e.buildRoad(1);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> e.buildRoad(2)
        );

        assertEquals("Cannot build a road on an occupied edge.", exception.getMessage());
        assertEquals(1, e.getEdgeOccupant());
    }
}
