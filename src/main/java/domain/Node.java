package domain;

import java.util.Objects;

public class Node {
    private int id;
    private Player occupant;
    private InfraType infraType;

    public Node(int id) {
        this.id = id;
        this.occupant = null;
        this.infraType = null;
    }

    public Player getNodeOccupant() {
        return occupant;
    }

    public void buildSettlement(Player player) {
        if (occupant == null) {
            occupant = player;
            infraType = InfraType.SETTLEMENT;
        } else {
            throw new IllegalStateException("Cannot settle on an already-settled node.");
        }
    }

    public void buildCity(Player player) {
        if (occupant != null && occupant != player) {
            throw new IllegalStateException("Cannot build a city on an already-settled node.");
        }

        if (infraType == InfraType.CITY) {
            throw new IllegalStateException("Cannot upgrade a city further.");
        } else if (infraType == null) {
            throw new IllegalStateException("Cannot upgrade an unsettled node to city.");
        }

        infraType = InfraType.CITY;
    }

    public InfraType getInfraType() {
        return infraType;
    }
}
