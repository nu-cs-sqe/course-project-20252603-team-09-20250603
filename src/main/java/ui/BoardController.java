package ui;

import domain.Board;
import domain.Edge;
import domain.Hex;
import domain.Node;
import domain.InfraType;
import domain.Player;
import domain.ResourceType;

import java.util.List;
import java.util.Map;

public class BoardController {
    @FunctionalInterface
    private interface LocationSelectionHandler {
        void onLocationSelected(int locationId, PlayerActionController.LocationType locationType);
    }

    private final Board board;
    private final List<Player> players;
    private BoardView view;
    private LocationSelectionHandler actionSelectionHandler;

    public BoardController(List<Player> players) {
        this(new Board(), players);
    }

    public BoardController(Board board, List<Player> players) {
        this.board = board;
        this.players = List.copyOf(players);
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(BoardView view) {
        this.view = view;
    }

    public Board getBoard() {
        return board;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    private SetupGameController setupGameController;

    public void setSetupGameController(SetupGameController setupGameController) {
        this.setupGameController = setupGameController;
    }

    public void setActionController(PlayerActionController actionController) {
        this.actionSelectionHandler = actionController == null ? null : actionController::onLocationSelected;
    }

    public void handleNodeSelected(int nodeId) {
        Node node = board.getNode(nodeId);

        if (setupGameController != null && setupGameController.isInSetupPhase()) {
            setupGameController.handleInitialPlacement(nodeId, InfraType.SETTLEMENT);
            view.refreshBoard();
            return;
        }

        Map<ResourceType, Integer> resources = board.getAdjacentResources(node);
        view.setStatusMessage("Selected node " + node.getId() + " | adjacent resources: " + resources);

        if (actionSelectionHandler != null) {
            actionSelectionHandler.onLocationSelected(nodeId, PlayerActionController.LocationType.NODE);
        }
    }

    public void handleEdgeSelected(int edgeId) {
        Edge edge = board.getEdge(edgeId);

        if (setupGameController != null && setupGameController.isInSetupPhase()) {
            setupGameController.handleInitialPlacement(edgeId, InfraType.ROAD);
            view.refreshBoard();
            return;
        }

        view.setStatusMessage(
                "Selected edge " + edge.getId()
                        + " | nodes " + edge.getNodeA().getId()
                        + " and " + edge.getNodeB().getId()
        );

        if (actionSelectionHandler != null) {
            actionSelectionHandler.onLocationSelected(edgeId, PlayerActionController.LocationType.EDGE);
        }
    }

    public void handleHexSelected(int hexId) {
        Hex hex = getHexById(hexId);
        view.setStatusMessage("Selected hex " + hex.getId() + " | resource: " + hex.getResourceType());
    }

    public void clearSelection() {
        if (view != null) {
            view.clearSelection();
        }
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
