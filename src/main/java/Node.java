public class Node {
    private int id;
    private int occupant;
    private String infraType;

    public Node(int id) {
        this.id = id;
        this.occupant = 0;
        this.infraType = "";
    }

    public int getNodeOccupant() {
        return occupant;
    }

    public void buildSettlement(int playerId) {
        if (occupant == 0) {
            occupant = playerId;
            infraType = "settlement";
        } else {
            throw new IllegalStateException("Cannot settle on an already-settled node.");
        }
    }

    public String getInfraType() {
        return infraType;
    }
}
