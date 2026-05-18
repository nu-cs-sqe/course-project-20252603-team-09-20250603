package domain;

public class Node {
    private int id;
    private Player occupant;

    public Node(int id) {
        this.id = id;
        this.occupant = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Node)) {
            return false;
        }

        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public Player getNodeOccupant() {
        return occupant;
    }

    public void buildSettlement(Player player) {
        if (occupant == null) {
            occupant = player;
        } else {
            throw new IllegalStateException("Cannot settle on an already-settled node.");
        }
    }

}