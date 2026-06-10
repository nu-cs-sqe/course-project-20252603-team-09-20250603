// - Handles initial settlement/road validation
// - Loops through node/hex map to identify neighbors

package domain;

import java.util.List;

public class PlacementValidator {
    private final Board board;

    public PlacementValidator(Board board) {
        this.board = board;
    }

    public void validateSettlementPlacement(Node targetNode) throws IllegalPlacementException {

        if (targetNode.getNodeOccupant() != null) {
            throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_NODE_ALREADY_OCCUPIED);
        }

        List<Edge> allEdges = board.getEdges();

        for (Edge edge: allEdges){

                if (edge.getNodeA().equals(targetNode)){
                    if (edge.getNodeB().getNodeOccupant() != null) {
                        throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_ADJACENT_NODE_OCCUPIED);
                    }
                }
                if (edge.getNodeB().equals(targetNode)){
                    if (edge.getNodeA().getNodeOccupant() != null) {
                        throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_ADJACENT_NODE_OCCUPIED);
                    }
                }
            }
        }



    public void validateInitialRoad(int edgeId, Node settlementNode) throws IllegalPlacementException {
        Edge edge = board.getEdge(edgeId);
        if (!edge.getNodeA().equals(settlementNode) && !edge.getNodeB().equals(settlementNode)) {
            throw new IllegalPlacementException(DomainErrorKey.PLACEMENT_ROAD_MUST_CONNECT_TO_SETTLEMENT);
        }
    }
}
