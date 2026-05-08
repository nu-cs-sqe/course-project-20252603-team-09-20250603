package domain.logic;
import domain.models.Board;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
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
}
