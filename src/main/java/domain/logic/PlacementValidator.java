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
}
