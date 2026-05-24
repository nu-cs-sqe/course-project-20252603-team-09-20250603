package domain;

public class Edge {
    private final int id;
    private Node nodeA;
    private Node nodeB;
    private Player occupant;

    public Edge(int id) {
        this.id = id;
        this.nodeA = null;
        this.nodeB = null;
        this.occupant = null;
    }

    public int getId() {
        return id;
    }

    public Node getNodeA() {
        return nodeA;
    }

    public void setNodeA(Node nodeA) {
        this.nodeA = nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    public void setNodeB(Node nodeB) {
        this.nodeB = nodeB;
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
