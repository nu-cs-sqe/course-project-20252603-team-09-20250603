package domain.logic;
import domain.models.Board;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementValidatorTest {
    private Board mockBoard;
    private PlacementValidator validator;

    @Test // TC-PV-01
    void test_DistanceRuleFails() {
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);

        int targetNode = 5;

        // Expectation: The board says the distance rule is violated (false)
        EasyMock.expect(mockBoard.checkDistanceRule(targetNode)).andReturn(false);

        EasyMock.replay(mockBoard);

        assertThrows(IllegalPlacementException.class, () ->
                validator.validateSettlementPlacement(targetNode));

        EasyMock.verify(mockBoard);
    }

    @Test // TC-PV-02
    void test_DistanceRulePassesWhenNeighborsEmpty() {
        // 1. Arrange
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);
        int targetNode = 10;

        // We expect the board to say the distance rule is satisfied (true)
        EasyMock.expect(mockBoard.checkDistanceRule(targetNode)).andReturn(true);

        // 2. Replay
        EasyMock.replay(mockBoard);

        // 3. Act & Assert
        // assertDoesNotThrow verifies that the code runs without an exception
        assertDoesNotThrow(() -> validator.validateSettlementPlacement(targetNode),
                "Should not throw an exception when the distance rule is satisfied.");

        // 4. Verify
        EasyMock.verify(mockBoard);
    }

    @Test // TC-PV-03
    void test_InitialRoadAdjacencySuccess() {
        // 1. Arrange
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);

        int edgeId = 10;
        int settlementNodeId = 5;
        // Simulate that Edge 10 connects Node 5 and Node 6
        List<Integer> validEndpoints = Arrays.asList(5, 6);

        // Expectation: Validator asks for endpoints, Board returns 5 and 6
        EasyMock.expect(mockBoard.getRoadEndpoints(edgeId)).andReturn(validEndpoints);

        // 2. Replay
        EasyMock.replay(mockBoard);

        // 3. Act & Assert
        // Since settlementNodeId (5) is in the endpoints list, this should pass
        assertDoesNotThrow(() -> validator.validateInitialRoad(edgeId, settlementNodeId),
                "Should not throw an exception when the road is connected to the settlement.");

        // 4. Verify
        EasyMock.verify(mockBoard);
    }

    @Test // TC-PV-04
    void test_InitialRoadFailsWhenNotConnectedToSettlement() {
        // 1. Arrange: Setup mock and target IDs
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);

        int edgeId = 10;
        int settlementNodeId = 99; // A node ID that is nowhere near Edge 10

        // In our mock board, Edge 10 only connects nodes 5 and 6
        List<Integer> validEndpoints = Arrays.asList(5, 6);

        // Tell the mock to return [5, 6] when asked about Edge 10
        EasyMock.expect(mockBoard.getRoadEndpoints(edgeId)).andReturn(validEndpoints);

        // 2. Replay: Start "Live Mode"
        EasyMock.replay(mockBoard);

        // 3. Act & Assert: Verify that the exception is thrown
        // Since 99 is not in the list [5, 6], this should fail
        assertThrows(IllegalPlacementException.class, () ->
                        validator.validateInitialRoad(edgeId, settlementNodeId),
                "Expected validateInitialRoad to throw an exception for disconnected road.");

        // 4. Verify: Ensure the board was actually consulted
        EasyMock.verify(mockBoard);
    }
}
