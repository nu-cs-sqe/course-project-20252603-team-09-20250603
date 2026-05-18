package domain;

import java.util.List;
import java.util.Map;

public class PlacementValidator {
    private final Board board;

    public PlacementValidator(Board board) {
        this.board = board;
    }

    public void validateSettlementPlacement(Node targetNode) throws IllegalPlacementException {
        if (targetNode.getNodeOccupant() != null) {
            throw new IllegalPlacementException("Node already occupied.");
        }

        List<Hex> targetHexes = board.getHexesFromNode(targetNode);

        for (Map.Entry<Node, List<Hex>> entry : board.getNodeToHexesMap().entrySet()) {
            Node potentialNeighbor = entry.getKey();
            List<Hex> neighborHexes = entry.getValue();

            if (potentialNeighbor.equals(targetNode)) {
                continue;
            }

            if (sharesHex(targetHexes, neighborHexes)) {
                if (potentialNeighbor.getNodeOccupant() != null) {
                    throw new IllegalPlacementException("Violates distance rule: Adjacent node occupied.");
                }
            }
        }
    }

    private boolean sharesHex(List<Hex> list1, List<Hex> list2) {
        for (Hex h : list1) {
            if (list2.contains(h)) {
                return true;
            }
        }
        return false;
    }

    public void validateInitialRoad(int edgeId, Node settlementNode) throws IllegalPlacementException {
        if (edgeId != settlementNode.hashCode()) {
            throw new IllegalPlacementException("Road must connect to your settlement.");
        }
    }
}
