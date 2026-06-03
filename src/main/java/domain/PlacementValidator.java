// - Handles initial settlement/road validation
// - Loops through node/hex map to identify neighbors

package domain;

import java.util.List;
import java.util.Map;

public class PlacementValidator {
    private final Board board;

    public PlacementValidator(Board board) {
        this.board = board;
    }

    public void validateSettlementPlacement(Node targetNode) throws IllegalPlacementException {
        // 1. Check that the node is not occupied - cannot occupy an already built upon node
        if (targetNode.getNodeOccupant() != null) {
            throw new IllegalPlacementException("Node already occupied.");
        }

        List<Edge> allEdges = board.getEdges();

        for (Edge edge: allEdges){

                if (edge.getNodeA().equals(targetNode)){
                    if (edge.getNodeB().getNodeOccupant() != null) {
                        throw new IllegalPlacementException("Distance Rule Violated: Adjacent Node occupied");
                    }
                }
                if (edge.getNodeB().equals(targetNode)){
                    if (edge.getNodeA().getNodeOccupant() != null) {
                        throw new IllegalPlacementException("Distance Rule Violated: Adjacent Node occupied");
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
        Edge edge = board.getEdge(edgeId);
        if (!edge.getNodeA().equals(settlementNode) && !edge.getNodeB().equals(settlementNode)) {
            throw new IllegalPlacementException("Road must connect to your settlement.");
        }
    }
}
