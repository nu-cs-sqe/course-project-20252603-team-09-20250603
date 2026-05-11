public class Edge {
    private int id;
    private int occupant;

    public Edge(int id) {
        this.id = id;
        this.occupant = 0;
    }

    public int getEdgeOccupant() {
        return occupant;
    }

    public void buildRoad(int playerId) {
        if (occupant != 0) {
            throw new IllegalStateException("Cannot build a road on an occupied edge.");
        }

        occupant = playerId;
    }
}
