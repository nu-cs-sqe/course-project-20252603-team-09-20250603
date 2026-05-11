import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
