package domain;

import java.util.Objects;

public class Node {
    private int id;
    private Player occupant;
    private String infraType;

    public Node(int id) {
        this.id = id;
        this.occupant = null;
        this.infraType = "";
    }

    public Player getNodeOccupant() {
        return occupant;
    }

    public void buildSettlement(Player player) {
        if (occupant == null) {
            occupant = player;
            infraType = "settlement";
        } else {
            throw new IllegalStateException("Cannot settle on an already-settled node.");
        }
    }

    public void buildCity(Player player) {
        if (occupant != null && occupant != player) {
            throw new IllegalStateException("Cannot build a city on an already-settled node.");
        }

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
