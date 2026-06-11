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
        node0.buildSettlement(mockPlayer);

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
        node1.buildSettlement(mockPlayer);

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
            }
        }

        assertTrue(validButDisconnectedEdgeId != -1, "Could not find a disconnected edge.");

        final Node finalSettlementNode = settlementNode;
        final int finalInvalidEdgeId = validButDisconnectedEdgeId;

        assertThrows(IllegalPlacementException.class, () -> {
            validator.validateInitialRoad(finalInvalidEdgeId, finalSettlementNode);
        });
    }

    @Test // TC-PV-06
    void test_RegularRoadFailsWhenEdgeOccupied() {
        board = new Board();
        validator = new PlacementValidator(board);
        Player player = new Player(0, "Player", PlayerColor.RED);
        Player other = new Player(1, "Other", PlayerColor.BLUE);

        board.getEdge(0).buildRoad(other);

        assertThrows(IllegalPlacementException.class,
                () -> validator.validateRegularRoad(0, player));
    }

    @Test // TC-PV-07
    void test_RegularRoadSucceedsWhenConnectedToOwnSettlement() {
        board = new Board();
        validator = new PlacementValidator(board);
        Player player = new Player(0, "Player", PlayerColor.RED);

        board.getEdge(0).getNodeA().buildSettlement(player);

        assertDoesNotThrow(() -> validator.validateRegularRoad(0, player));
    }

    @Test // TC-PV-08
    void test_RegularRoadSucceedsWhenConnectedToOwnRoad() {
        board = new Board();
        validator = new PlacementValidator(board);
        Player player = new Player(0, "Player", PlayerColor.RED);

        Edge neighbour = findNeighbourEdge(board.getEdge(0));
        assertNotNull(neighbour, "Expected an edge sharing an endpoint with edge 0.");
        neighbour.buildRoad(player);

        assertDoesNotThrow(() -> validator.validateRegularRoad(0, player));
    }

    @Test // TC-PV-09
    void test_RegularRoadFailsWhenDisconnected() {
        board = new Board();
        validator = new PlacementValidator(board);
        Player player = new Player(0, "Player", PlayerColor.RED);

        assertThrows(IllegalPlacementException.class,
                () -> validator.validateRegularRoad(0, player));
    }

    @Test // TC-PV-10
    void test_RegularRoadFailsWhenConnectedOnlyToOpponent() {
        board = new Board();
        validator = new PlacementValidator(board);
        Player player = new Player(0, "Player", PlayerColor.RED);
        Player other = new Player(1, "Other", PlayerColor.BLUE);

        board.getEdge(0).getNodeA().buildSettlement(other);

        assertThrows(IllegalPlacementException.class,
                () -> validator.validateRegularRoad(0, player));
    }

    private Edge findNeighbourEdge(Edge target) {
        for (Edge edge : board.getEdgesConnectedToNode(target.getNodeA())) {
            if (edge.getId() != target.getId()) {
                return edge;
            }
        }
        for (Edge edge : board.getEdgesConnectedToNode(target.getNodeB())) {
            if (edge.getId() != target.getId()) {
                return edge;
            }
        }
        return null;
    }
}