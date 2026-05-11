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
}
