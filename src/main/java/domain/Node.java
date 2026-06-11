package domain;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(
        value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "Domain objects intentionally share mutable Player references."
)

public class Node {
    private int id;
    private Player occupant;
    private InfraType infraType;

    public Node(int id) {
        this.id = id;
        this.occupant = null;
        this.infraType = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)){
            return false;
        }

        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public int getId() {
        return id;
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
            throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_CITY_ON_OPPONENT_NODE);
        }

        if (infraType == InfraType.CITY) {
            throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_CITY_ALREADY_UPGRADED);
        } else if (infraType == null) {
            throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_CITY_REQUIRES_SETTLEMENT);
        }

        infraType = InfraType.CITY;
    }

    public InfraType getInfraType() {
        return infraType;
    }
}
