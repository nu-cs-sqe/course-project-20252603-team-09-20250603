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
    private final MainView mainView;

    public BoardController(MainView mainView, List<Player> players) {
        this.mainView = mainView;
        this.board = new Board();
        this.players = List.copyOf(players);
    }

    public void setView(BoardView view) {
        this.view = view;
    }

    public Board getBoard() {
        return board;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void handleNodeSelected(int nodeId) {
        Node node = board.getNode(nodeId);
        Map<ResourceType, Integer> resources = board.getAdjacentResources(node);
        view.setStatusMessage(I18n.text("board.status.node", node.getId(), resources));
    }

    public void handleEdgeSelected(int edgeId) {
        Edge edge = board.getEdge(edgeId);
        view.setStatusMessage(I18n.text("board.status.edge", edge.getId(), edge.getNodeA().getId(), edge.getNodeB().getId()));
    }

    public void handleHexSelected(int hexId) {
        Hex hex = getHexById(hexId);
        view.setStatusMessage(I18n.text("board.status.hex", hex.getId(), hex.getResourceType()));
    }

    public void handleExitToWelcome() {
        if (mainView != null) {
            mainView.showWelcomeView();
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
