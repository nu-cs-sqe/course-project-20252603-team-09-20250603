import java.util.Objects;

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

    public void buildCity(int playerId) {
        if (Objects.equals(infraType, "city")) {
            throw new IllegalStateException("Cannot upgrade a city further.");
        } else if (Objects.equals(infraType, "")) {
            throw new IllegalStateException("Cannot upgrade an unsettled node to city.");
        }
        infraType = "city";
    }

    public String getInfraType() {
        return infraType;
    }
}
