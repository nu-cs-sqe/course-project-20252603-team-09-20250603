package domain;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "Domain objects intentionally share mutable Player references."
)

public class Edge {
    private final int id;
    private Player occupant;

    public Edge(int id) {
        this.id = id;
        this.occupant = null;
    }

    public int getId() {
        return id;
    }

    public Player getEdgeOccupant() {
        return occupant;
    }

    public void buildRoad(Player player) {
        if (occupant != null) {
            throw new IllegalStateException("Cannot build a road on an occupied edge.");
        }

        occupant = player;
    }
}
