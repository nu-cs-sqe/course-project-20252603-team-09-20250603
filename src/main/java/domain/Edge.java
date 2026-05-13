package domain;

public class Edge {
    private int id;
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
}
