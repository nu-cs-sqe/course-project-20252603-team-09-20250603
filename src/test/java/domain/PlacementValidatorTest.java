package domain;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementValidatorTest {
    private Board board;
    private PlacementValidator validator;
    private Player mockPlayer;

    @Test // TC-PV-01
    void test_DistanceRuleFails() {
        board = new Board();
        validator = new PlacementValidator(board);
        mockPlayer = EasyMock.createMock(Player.class);

        Node node0 = null;
        Node node1 = null;

        for (Node n : board.getNodeToHexesMap().keySet()) {
            if (n.equals(new Node(0))) node0 = n;
            if (n.equals(new Node(1))) node1 = n;
        }

        node1.buildSettlement(mockPlayer);

        EasyMock.replay(mockPlayer);

        final Node targetNode = node0;
        assertThrows(IllegalPlacementException.class, () -> {
            validator.validateSettlementPlacement(targetNode);
        });

        EasyMock.verify(mockPlayer);
    }

    @Test // TC-PV-02
    @Disabled
    void test_DistanceRulePassesWhenNeighborsEmpty() {
    }

    @Test // TC-PV-03
    @Disabled
    void test_InitialRoadAdjacencySuccess() {
    }

    @Test // TC-PV-04
    @Disabled
    void test_InitialRoadFailsWhenNotConnectedToSettlement() {
    }
}
