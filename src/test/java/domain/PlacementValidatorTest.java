package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlacementValidatorTest {
    private Board board;
    private PlacementValidator validator;
    private Player mockPlayer;

    @Test // TC-PV-01
    void test_TargetNodeOccupiedFails() {
        board = new Board();
        validator = new PlacementValidator(board);
        mockPlayer = EasyMock.createMock(Player.class);

        Node node0 = null;

        for (Node n : board.getNodeToHexesMap().keySet()) {
            if (n.equals(new Node(0))) {
                node0 = n;
                break;
            }
        }

        assertNotNull(node0);
        node0.buildSettlement(mockPlayer); // Occupy the target node itself

        EasyMock.replay(mockPlayer);

        final Node targetNode = node0;
        assertThrows(IllegalPlacementException.class, () -> {
            validator.validateSettlementPlacement(targetNode);
        });

        EasyMock.verify(mockPlayer);
    }

    @Test // TC-PV-02
    void test_DistanceRuleFails() {
        board = new Board();
        validator = new PlacementValidator(board);
        mockPlayer = EasyMock.createMock(Player.class);

        Node node0 = null;
        Node node1 = null;

        for (Node n : board.getNodeToHexesMap().keySet()) {
            if (n.equals(new Node(0))) {
                node0 = n;
            }
            if (n.equals(new Node(1))) {
                node1 = n;
            }
        }

        assertNotNull(node1);
        node1.buildSettlement(mockPlayer); // Occupy the adjacent node

        EasyMock.replay(mockPlayer);

        final Node targetNode = node0;
        assertThrows(IllegalPlacementException.class, () -> {
            validator.validateSettlementPlacement(targetNode);
        });

        EasyMock.verify(mockPlayer);
    }

    @Test // TC-PV-03
    void test_DistanceRulePassesWhenNeighborsEmpty() {
        board = new Board();
        validator = new PlacementValidator(board);

        Node node0 = null;

        for (Node n : board.getNodeToHexesMap().keySet()) {
            if (n.equals(new Node(0))) {
                node0 = n;
                break;
            }
        }

        final Node targetNode = node0;
        assertDoesNotThrow(() -> {
            validator.validateSettlementPlacement(targetNode);
        });
    }

    @Test // TC-PV-04
    void test_InitialRoadAdjacencySuccess() {
        board = new Board();
        validator = new PlacementValidator(board);

        Node settlementNode = null;
        for (Node n : board.getNodeToHexesMap().keySet()) {
            if (n.equals(new Node(10))) {
                settlementNode = n;
                break;
            }
        }
        assertNotNull(settlementNode);

        int validEdgeId = -1;
        for (int i = 0; i < 72; i++) {
            try {
                Edge e = board.getEdge(i);
                if (e.getNodeA().equals(settlementNode) || e.getNodeB().equals(settlementNode)) {
                    validEdgeId = i;
                    break;
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        assertTrue(validEdgeId != -1, "Could not find a valid edge connected to the settlement.");

        final Node finalSettlementNode = settlementNode;
        final int finalValidEdgeId = validEdgeId;

        assertDoesNotThrow(() -> {
            validator.validateInitialRoad(finalValidEdgeId, finalSettlementNode);
        });
    }

    @Test // TC-PV-05
    void test_InitialRoadFailsWhenNotConnectedToSettlement() {
        board = new Board();
        validator = new PlacementValidator(board);

        Node settlementNode = null;
        for (Node n : board.getNodeToHexesMap().keySet()) {
            if (n.equals(new Node(10))) {
                settlementNode = n;
                break;
            }
        }
        assertNotNull(settlementNode);

        int validButDisconnectedEdgeId = -1;
        for (int i = 0; i < 72; i++) {
            try {
                Edge e = board.getEdge(i);
                if (!e.getNodeA().equals(settlementNode) && !e.getNodeB().equals(settlementNode)) {
                    validButDisconnectedEdgeId = i;
                    break;
                }
            } catch (IllegalArgumentException ignored) {
                // Ignore invalid IDs while searching
            }
        }

        assertTrue(validButDisconnectedEdgeId != -1, "Could not find a disconnected edge.");

        final Node finalSettlementNode = settlementNode;
        final int finalInvalidEdgeId = validButDisconnectedEdgeId;

        assertThrows(IllegalPlacementException.class, () -> {
            validator.validateInitialRoad(finalInvalidEdgeId, finalSettlementNode);
        });
    }
}