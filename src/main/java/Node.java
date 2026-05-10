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
        occupant = playerId;
        infraType = "settlement";
    }

    public String getInfraType() {
        return infraType;
    }
}
