package domain;

public class Edge {
    private final int id;
    private Player occupant;

    public Edge(int id) {
        this.id = id;
        this.occupant = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Edge)) {
            return false;
        }

        Edge edge = (Edge) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
