package domain.logic;
import domain.models.Board;

import java.util.List;

public class PlacementValidator {
    private final Board board;

    public PlacementValidator(Board board) {
        this.board = board;
    }

    public void validateSettlementPlacement(int nodeId) {
        // In your design, Board has checkDistanceRule.
        // We use that to determine if the placement is valid.
        if (!board.checkDistanceRule(nodeId)) {
            throw new IllegalPlacementException("Placement violates the distance rule.");
        }
    }

    public void validateInitialRoad(int edgeId, int settlementNodeId) {
        // Fetch the two endpoints of the edge from the board
        List<Integer> endpoints = board.getRoadEndpoints(edgeId);

        // If the board returns null or the list doesn't contain our node, it's illegal
        if (endpoints == null || !endpoints.contains(settlementNodeId)) {
            throw new IllegalPlacementException("Road must be connected to the newly placed settlement.");
        }
    }
}
