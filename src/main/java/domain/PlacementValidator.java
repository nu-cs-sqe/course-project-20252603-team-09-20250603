package domain;

import java.util.List;

public class PlacementValidator {
    private final Board board;

    public PlacementValidator(Board board) {
        this.board = board;
    }

    public void validateSettlementPlacement(int nodeId) {
        if (!board.checkDistanceRule(nodeId)) {
            throw new IllegalPlacementException("Placement violates the distance rule.");
        }
    }

    public void validateInitialRoad(int edgeId, int settlementNodeId) {
        List<Integer> endpoints = board.getRoadEndpoints(edgeId);

        if (endpoints == null || !endpoints.contains(settlementNodeId)) {
            throw new IllegalPlacementException("Road must be connected to the newly placed settlement.");
        }
    }
}
