package domain;
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

        EasyMock.expect(mockBoard.checkDistanceRule(targetNode)).andReturn(false);

        EasyMock.replay(mockBoard);

        assertThrows(IllegalPlacementException.class, () ->
                validator.validateSettlementPlacement(targetNode));

        EasyMock.verify(mockBoard);
    }

    @Test // TC-PV-02
    void test_DistanceRulePassesWhenNeighborsEmpty() {
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);
        int targetNode = 10;

        EasyMock.expect(mockBoard.checkDistanceRule(targetNode)).andReturn(true);

        EasyMock.replay(mockBoard);

        assertDoesNotThrow(() -> validator.validateSettlementPlacement(targetNode),
                "Should not throw an exception when the distance rule is satisfied.");

        EasyMock.verify(mockBoard);
    }

    @Test // TC-PV-03
    void test_InitialRoadAdjacencySuccess() {
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);

        int edgeId = 10;
        int settlementNodeId = 5;
        List<Integer> validEndpoints = Arrays.asList(5, 6);

        EasyMock.expect(mockBoard.getRoadEndpoints(edgeId)).andReturn(validEndpoints);

        EasyMock.replay(mockBoard);

        assertDoesNotThrow(() -> validator.validateInitialRoad(edgeId, settlementNodeId),
                "Should not throw an exception when the road is connected to the settlement.");

        EasyMock.verify(mockBoard);
    }

    @Test // TC-PV-04
    void test_InitialRoadFailsWhenNotConnectedToSettlement() {
        mockBoard = EasyMock.createMock(Board.class);
        validator = new PlacementValidator(mockBoard);

        int edgeId = 10;
        int settlementNodeId = 99;

        List<Integer> validEndpoints = Arrays.asList(5, 6);

        EasyMock.expect(mockBoard.getRoadEndpoints(edgeId)).andReturn(validEndpoints);

        EasyMock.replay(mockBoard);

        assertThrows(IllegalPlacementException.class, () ->
                        validator.validateInitialRoad(edgeId, settlementNodeId),
                "Expected validateInitialRoad to throw an exception for disconnected road.");

        EasyMock.verify(mockBoard);
    }
}
