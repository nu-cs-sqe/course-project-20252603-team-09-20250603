package ui;

import domain.Board;
import domain.Edge;
import domain.Hex;
import domain.Node;
import domain.Player;
import domain.ResourceType;

import java.util.List;
import java.util.Map;

public class BoardController {
    private final Board board;
    private final List<Player> players;
    private BoardView view;
    private PlayerActionController actionController;

    public BoardController(List<Player> players) {
        this.board = new Board();
        this.players = List.copyOf(players);
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(BoardView view) {
        this.view = view;
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void setActionController(PlayerActionController actionController) {
        this.actionController = actionController;
    }

    public void handleNodeSelected(int nodeId) {
        Node node = board.getNode(nodeId);
        Map<ResourceType, Integer> resources = board.getAdjacentResources(node);
        view.setStatusMessage("Selected node " + node.getId() + " | adjacent resources: " + resources);

        if (actionController != null) {
            actionController.onLocationSelected(nodeId, PlayerActionController.LocationType.NODE);
        }
    }

    public void handleEdgeSelected(int edgeId) {
        Edge edge = board.getEdge(edgeId);
        view.setStatusMessage(
                "Selected edge " + edge.getId()
                        + " | nodes " + edge.getNodeA().getId()
                        + " and " + edge.getNodeB().getId()
        );

        if (actionController != null) {
            actionController.onLocationSelected(edgeId, PlayerActionController.LocationType.EDGE);
        }
    }

    public void handleHexSelected(int hexId) {
        Hex hex = getHexById(hexId);
        view.setStatusMessage("Selected hex " + hex.getId() + " | resource: " + hex.getResourceType());
    }

    private Hex getHexById(int hexId) {
        for (Hex hex : board.getHexes()) {
            if (hex.getId() == hexId) {
                return hex;
            }
        }

        throw new IllegalArgumentException("Invalid hex ID.");
    }
}
