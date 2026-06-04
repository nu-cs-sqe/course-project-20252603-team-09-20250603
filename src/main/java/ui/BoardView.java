package ui;

import domain.Board;
import domain.Edge;
import domain.Hex;
import domain.Node;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.Map;

public class BoardView extends BorderPane {
    private static final String STYLESHEET = "/ui/board-view.css";
    private static final String BOARD_IMAGE = "/ui/catan-board.png";
    private static final double BOARD_WIDTH = 900.0;
    private static final double BOARD_HEIGHT = 733.0;
    private static final double HEX_RADIUS = 78.0;
    private static final double[] HEX_VERTEX_ANGLES = {-150.0, -90.0, -30.0, 30.0, 90.0, 150.0};

    private final BoardController controller;
    private final Label statusLabel;
    private final Map<Integer, BoardPoint> hexCenters;
    private final Map<Integer, BoardPoint> nodePositions;

    private Circle selectedNode;
    private Line selectedEdge;
    private Polygon selectedHex;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public BoardView(BoardController controller) {
        this.controller = controller;
        controller.setView(this);

        this.hexCenters = buildHexCenters();
        this.nodePositions = buildNodePositions();
        this.statusLabel = new Label("Board ready. Select a node or edge.");

        getStyleClass().add("board-view");
        getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
        setPadding(new Insets(12.0));

        Label titleLabel = new Label("Catan Board");
        titleLabel.getStyleClass().add("board-title");
        setTop(titleLabel);

        ScrollPane scrollPane = new ScrollPane(buildBoardPane(controller.getBoard()));
        scrollPane.getStyleClass().add("board-scroll");
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        setCenter(scrollPane);

        statusLabel.getStyleClass().add("board-status");
        setBottom(statusLabel);
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    private Pane buildBoardPane(Board board) {
        Pane boardPane = new Pane();
        boardPane.getStyleClass().add("board-pane");
        boardPane.setMinSize(BOARD_WIDTH, BOARD_HEIGHT);
        boardPane.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        boardPane.setMaxSize(BOARD_WIDTH, BOARD_HEIGHT);

        Image boardImage = new Image(getClass().getResource(BOARD_IMAGE).toExternalForm());
        ImageView imageView = new ImageView(boardImage);
        imageView.setFitWidth(BOARD_WIDTH);
        imageView.setFitHeight(BOARD_HEIGHT);
        imageView.setPreserveRatio(false);
        boardPane.getChildren().add(imageView);

        drawHexes(boardPane, board);
        drawEdges(boardPane, board);
        drawNodes(boardPane, board);

        return boardPane;
    }

    private void drawHexes(Pane boardPane, Board board) {
        for (Hex hex : board.getHexes()) {
            Polygon hexShape = createHexShape(hex.getId());
            hexShape.getStyleClass().add("hex-overlay");
            Tooltip.install(hexShape, new Tooltip("Hex " + hex.getId() + " (" + hex.getResourceType() + ")"));
            hexShape.setOnMouseClicked(event -> {
                selectHex(hexShape);
                controller.handleHexSelected(hex.getId());
                event.consume();
            });
            boardPane.getChildren().add(hexShape);
        }
    }

    private void drawEdges(Pane boardPane, Board board) {
        for (Edge edge : board.getEdges()) {
            BoardPoint start = nodePositions.get(edge.getNodeA().getId());
            BoardPoint end = nodePositions.get(edge.getNodeB().getId());

            Line line = new Line(start.x, start.y, end.x, end.y);
            line.getStyleClass().add("edge-overlay");
            Tooltip.install(line, new Tooltip(
                    "Edge " + edge.getId()
                            + " (" + edge.getNodeA().getId()
                            + "-" + edge.getNodeB().getId() + ")"
            ));
            line.setOnMouseClicked(event -> {
                selectEdge(line);
                controller.handleEdgeSelected(edge.getId());
                event.consume();
            });
            boardPane.getChildren().add(line);
        }
    }

    private void drawNodes(Pane boardPane, Board board) {
        for (Node node : board.getNodes()) {
            BoardPoint point = nodePositions.get(node.getId());
            Circle circle = new Circle(point.x, point.y, 8.0);
            circle.getStyleClass().add("node-overlay");
            Tooltip.install(circle, new Tooltip("Node " + node.getId()));
            circle.setOnMouseClicked(event -> {
                selectNode(circle);
                controller.handleNodeSelected(node.getId());
                event.consume();
            });
            boardPane.getChildren().add(circle);
        }
    }

    private Polygon createHexShape(int hexId) {
        BoardPoint center = hexCenters.get(hexId);
        Polygon polygon = new Polygon();

        for (double angle : HEX_VERTEX_ANGLES) {
            double radians = Math.toRadians(angle);
            polygon.getPoints().add(center.x + HEX_RADIUS * Math.cos(radians));
            polygon.getPoints().add(center.y + HEX_RADIUS * Math.sin(radians));
        }

        return polygon;
    }

    private Map<Integer, BoardPoint> buildHexCenters() {
        Map<Integer, BoardPoint> centers = new HashMap<>();

        centers.put(0, new BoardPoint(315.0, 112.0));
        centers.put(1, new BoardPoint(451.0, 112.0));
        centers.put(2, new BoardPoint(587.0, 112.0));
        centers.put(3, new BoardPoint(247.0, 229.0));
        centers.put(4, new BoardPoint(383.0, 229.0));
        centers.put(5, new BoardPoint(519.0, 229.0));
        centers.put(6, new BoardPoint(655.0, 229.0));
        centers.put(7, new BoardPoint(179.0, 346.0));
        centers.put(8, new BoardPoint(315.0, 346.0));
        centers.put(9, new BoardPoint(451.0, 346.0));
        centers.put(10, new BoardPoint(587.0, 346.0));
        centers.put(11, new BoardPoint(723.0, 346.0));
        centers.put(12, new BoardPoint(247.0, 463.0));
        centers.put(13, new BoardPoint(383.0, 463.0));
        centers.put(14, new BoardPoint(519.0, 463.0));
        centers.put(15, new BoardPoint(655.0, 463.0));
        centers.put(16, new BoardPoint(315.0, 580.0));
        centers.put(17, new BoardPoint(451.0, 580.0));
        centers.put(18, new BoardPoint(587.0, 580.0));

        return centers;
    }

    private Map<Integer, BoardPoint> buildNodePositions() {
        Map<Integer, BoardPoint> positions = new HashMap<>();

        putHexNodes(positions, 0, 0, 1, 2, 10, 9, 8);
        putHexNodes(positions, 1, 2, 3, 4, 12, 11, 10);
        putHexNodes(positions, 2, 4, 5, 6, 14, 13, 12);
        putHexNodes(positions, 3, 7, 8, 9, 19, 18, 17);
        putHexNodes(positions, 4, 9, 10, 11, 21, 20, 19);
        putHexNodes(positions, 5, 11, 12, 13, 23, 22, 21);
        putHexNodes(positions, 6, 13, 14, 15, 25, 24, 23);
        putHexNodes(positions, 7, 16, 17, 18, 29, 28, 27);
        putHexNodes(positions, 8, 18, 19, 20, 31, 30, 29);
        putHexNodes(positions, 9, 20, 21, 22, 33, 32, 31);
        putHexNodes(positions, 10, 22, 23, 24, 35, 34, 33);
        putHexNodes(positions, 11, 24, 25, 26, 37, 36, 35);
        putHexNodes(positions, 12, 28, 29, 30, 40, 39, 38);
        putHexNodes(positions, 13, 30, 31, 32, 42, 41, 40);
        putHexNodes(positions, 14, 32, 33, 34, 44, 43, 42);
        putHexNodes(positions, 15, 34, 35, 36, 46, 45, 44);
        putHexNodes(positions, 16, 39, 40, 41, 49, 48, 47);
        putHexNodes(positions, 17, 41, 42, 43, 51, 50, 49);
        putHexNodes(positions, 18, 43, 44, 45, 53, 52, 51);

        return positions;
    }

    private void putHexNodes(Map<Integer, BoardPoint> positions, int hexId, int node0, int node1,
            int node2, int node3, int node4, int node5) {
        int[] nodeIds = {node0, node1, node2, node3, node4, node5};

        for (int vertexIndex = 0; vertexIndex < nodeIds.length; vertexIndex++) {
            positions.putIfAbsent(nodeIds[vertexIndex], getHexVertex(hexId, vertexIndex));
        }
    }

    private BoardPoint getHexVertex(int hexId, int vertexIndex) {
        BoardPoint center = hexCenters.get(hexId);
        double radians = Math.toRadians(HEX_VERTEX_ANGLES[vertexIndex]);

        return new BoardPoint(
                center.x + HEX_RADIUS * Math.cos(radians),
                center.y + HEX_RADIUS * Math.sin(radians)
        );
    }

    private void selectNode(Circle circle) {
        if (selectedNode != null) {
            selectedNode.getStyleClass().remove("selected-node");
        }

        selectedNode = circle;
        selectedNode.getStyleClass().add("selected-node");
    }

    private void selectEdge(Line line) {
        if (selectedEdge != null) {
            selectedEdge.getStyleClass().remove("selected-edge");
        }

        selectedEdge = line;
        selectedEdge.getStyleClass().add("selected-edge");
    }

    private void selectHex(Polygon polygon) {
        if (selectedHex != null) {
            selectedHex.getStyleClass().remove("selected-hex");
        }

        selectedHex = polygon;
        selectedHex.getStyleClass().add("selected-hex");
    }

    private static final class BoardPoint {
        private final double x;
        private final double y;

        private BoardPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
