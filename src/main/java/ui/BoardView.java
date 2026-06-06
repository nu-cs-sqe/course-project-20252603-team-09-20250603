package ui;

import domain.Board;
import domain.Edge;
import domain.Hex;
import domain.Node;
import domain.Player;

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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class BoardView extends BorderPane {
    private static final String STYLESHEET = "/ui/board-view.css";
    private static final String BOARD_IMAGE = "/ui/CATAN-BOARD.PNG";
    private static final double IMAGE_WIDTH = 1392.0;
    private static final double IMAGE_HEIGHT = 1130.0;
    /** Shown size in the UI (image + overlays are scaled down together). */
    private static final double BOARD_WIDTH = 880.0;
    private static final double BOARD_HEIGHT = IMAGE_HEIGHT * (BOARD_WIDTH / IMAGE_WIDTH);
    private static final double DISPLAY_SCALE = BOARD_WIDTH / IMAGE_WIDTH;

    // --- Hex grid geometry in SOURCE-IMAGE pixels. Tune these three to align the overlay. ---
    /** Center of the board = the desert (hex 9), in source-image pixels. */
    private static final double HEX_CENTER_X = 700.0;
    private static final double HEX_CENTER_Y = 528.0;
    /** Horizontal spacing between adjacent hex centers in the same row. */
    private static final double HEX_DX = 224.0;
    /** Derived: vertical row spacing and hex radius for a regular pointy-top hex. */
    private static final double HEX_DY = HEX_DX * 0.755;
    private static final double HEX_SIZE = HEX_DX / Math.sqrt(3.0);
    /** Extra vertical offset per row (top-to-bottom), in source-image px. Negative = up, positive = down.
     *  Lets each row be fine-tuned independently on top of the uniform spacing. */
    private static final double[] ROW_NUDGE_Y = {4.0, -6.0, 0.0, 14.0, 28.0};
    /** Targeted fine-tune for the node band between the top two hex rows (nodes 7-15). */
    private static final double BAND2_NODE_UP = 14.0;       // source-image px to move up
    private static final double BAND2_NODE_INWARD = 0.95;   // scale toward center (<1 = inward)

    private static final double HEX_RADIUS_X = HEX_SIZE * DISPLAY_SCALE;
    private static final double HEX_RADIUS_Y = HEX_SIZE * DISPLAY_SCALE;
    private static final double NODE_RADIUS = 6.0;
    private static final double[] HEX_VERTEX_ANGLES = {-150.0, -90.0, -30.0, 30.0, 90.0, 150.0};

    private final BoardController controller;
    private final Label statusLabel;
    private final Map<Integer, BoardPoint> hexCenters;
    private final Map<Integer, BoardPoint> nodePositions;
    private final Map<Integer, Line> edgeShapes;
    private final Map<Integer, Polygon> settlementShapes;

    private Circle selectedNode;
    private Line selectedEdge;
    private Polygon selectedHex;

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public BoardView(BoardController controller) {
        this.controller = controller;
        controller.setView(this);

        this.hexCenters = buildHexCenters();
        this.nodePositions = buildNodePositions();
        this.edgeShapes = new HashMap<>();
        this.settlementShapes = new HashMap<>();
        this.statusLabel = new Label("Board ready. Select a node or edge.");

        getStyleClass().add("board-view");
        URL stylesheetUrl = getClass().getResource(STYLESHEET);
        if (stylesheetUrl != null) {
            getStylesheets().add(stylesheetUrl.toExternalForm());
        }
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

    public void clearSelection() {
        if (selectedNode != null) {
            selectedNode.getStyleClass().remove("selected-node");
            selectedNode = null;
        }
        if (selectedEdge != null) {
            selectedEdge.getStyleClass().remove("selected-edge");
            selectedEdge = null;
        }
        if (selectedHex != null) {
            selectedHex.getStyleClass().remove("selected-hex");
            selectedHex = null;
        }
    }

    public void refreshBoard() {
        Board board = controller.getBoard();

        for (Edge edge : board.getEdges()) {
            Line line = edgeShapes.get(edge.getId());

            if (line != null) {
                updateRoadStyle(line, edge.getEdgeOccupant());
            }
        }

        for (Node node : board.getNodes()) {
            Polygon settlement = settlementShapes.get(node.getId());

            if (settlement != null) {
                updateSettlementStyle(settlement, node.getNodeOccupant());
            }
        }
    }

    private Pane buildBoardPane(Board board) {
        Pane boardPane = new Pane();
        boardPane.getStyleClass().add("board-pane");
        boardPane.setMinSize(BOARD_WIDTH, BOARD_HEIGHT);
        boardPane.setPrefSize(BOARD_WIDTH, BOARD_HEIGHT);
        boardPane.setMaxSize(BOARD_WIDTH, BOARD_HEIGHT);

        URL boardImageUrl = getClass().getResource(BOARD_IMAGE);
        if (boardImageUrl == null) {
            throw new IllegalStateException("Missing board image resource: " + BOARD_IMAGE);
        }
        Image boardImage = new Image(boardImageUrl.toExternalForm());
        ImageView imageView = new ImageView(boardImage);
        imageView.setFitWidth(BOARD_WIDTH);
        imageView.setFitHeight(BOARD_HEIGHT);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
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
            edgeShapes.put(edge.getId(), line);
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
            Circle circle = new Circle(point.x, point.y, NODE_RADIUS);
            circle.getStyleClass().add("node-overlay");
            Tooltip.install(circle, new Tooltip("Node " + node.getId()));
            circle.setOnMouseClicked(event -> {
                selectNode(circle);
                controller.handleNodeSelected(node.getId());
                event.consume();
            });
            boardPane.getChildren().add(circle);

            Polygon settlement = createSettlementShape(point);
            settlement.setVisible(false);
            settlement.setMouseTransparent(true);
            settlementShapes.put(node.getId(), settlement);
            boardPane.getChildren().add(settlement);
        }
    }

    private Polygon createSettlementShape(BoardPoint point) {
        Polygon settlement = new Polygon();
        double s = DISPLAY_SCALE;
        settlement.getPoints().addAll(
                point.x, point.y - 16.0 * s,
                point.x + 12.0 * s, point.y - 5.0 * s,
                point.x + 9.0 * s, point.y + 11.0 * s,
                point.x - 9.0 * s, point.y + 11.0 * s,
                point.x - 12.0 * s, point.y - 5.0 * s
        );

        settlement.setStyle("-fx-fill: transparent; -fx-stroke: transparent;");
        return settlement;
    }

    private Polygon createHexShape(int hexId) {
        BoardPoint center = hexCenters.get(hexId);
        Polygon polygon = new Polygon();

        for (double angle : HEX_VERTEX_ANGLES) {
            double radians = Math.toRadians(angle);
            polygon.getPoints().add(center.x + HEX_RADIUS_X * Math.cos(radians));
            polygon.getPoints().add(center.y + HEX_RADIUS_Y * Math.sin(radians));
        }

        return polygon;
    }

    private Map<Integer, BoardPoint> buildHexCenters() {
        Map<Integer, BoardPoint> centers = new HashMap<>();

        // Rows of 3,4,5,4,3 pointy-top hexes, centered on (HEX_CENTER_X, HEX_CENTER_Y).
        // Hex ids are assigned row by row (0..18), identical to the original ordering.
        int[] rowCounts = {3, 4, 5, 4, 3};
        int hexId = 0;

        for (int row = 0; row < rowCounts.length; row++) {
            int count = rowCounts[row];
            double rowY = HEX_CENTER_Y + (row - 2) * HEX_DY + ROW_NUDGE_Y[row];
            double rowStartX = HEX_CENTER_X - (count - 1) * HEX_DX / 2.0;

            for (int col = 0; col < count; col++) {
                double x = rowStartX + col * HEX_DX;
                centers.put(hexId, toDisplayPoint(x, rowY));
                hexId++;
            }
        }

        return centers;
    }

    private BoardPoint toDisplayPoint(double layoutX, double layoutY) {
        return new BoardPoint(layoutX * DISPLAY_SCALE, layoutY * DISPLAY_SCALE);
    }

    /** Shifts the given nodes up and horizontally inward toward the board center (display space). */
    private void nudgeNodesUpAndInward(Map<Integer, BoardPoint> positions, int... nodeIds) {
        double centerX = HEX_CENTER_X * DISPLAY_SCALE;
        double up = BAND2_NODE_UP * DISPLAY_SCALE;

        for (int nodeId : nodeIds) {
            BoardPoint p = positions.get(nodeId);
            if (p == null) {
                continue;
            }
            double newX = centerX + (p.x - centerX) * BAND2_NODE_INWARD;
            positions.put(nodeId, new BoardPoint(newX, p.y - up));
        }
    }

    /** Shifts a single node by (dx, dy) in display space. +x = right, +y = down. */
    private void offsetNode(Map<Integer, BoardPoint> positions, int nodeId, double dx, double dy) {
        BoardPoint p = positions.get(nodeId);
        if (p == null) {
            return;
        }
        positions.put(nodeId, new BoardPoint(p.x + dx, p.y + dy));
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

        nudgeNodesUpAndInward(positions, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        // Per-node fine-tuning (display px): +x = right, +y = down.
        offsetNode(positions, 7, 0.0, 15.0);    // down
        offsetNode(positions, 15, 0.0, 15.0);   // down
        offsetNode(positions, 0, 15.0, 0.0);    // inward (right)
        offsetNode(positions, 6, -15.0, 0.0);   // inward (left)

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
                center.x + HEX_RADIUS_X * Math.cos(radians),
                center.y + HEX_RADIUS_Y * Math.sin(radians)
        );
    }

    private void updateRoadStyle(Line line, Player occupant) {
        if (occupant == null) {
            line.setStyle("");
            return;
        }

        line.setStyle("-fx-stroke: " + getPlayerColor(occupant)
                + "; -fx-stroke-width: " + (10 * DISPLAY_SCALE) + "; -fx-stroke-line-cap: round;");
    }

    private void updateSettlementStyle(Polygon settlement, Player occupant) {
        if (occupant == null) {
            settlement.setVisible(false);
            settlement.setStyle("-fx-fill: transparent; -fx-stroke: transparent;");
            return;
        }
//board.getadjacentresources, player.addresources
        settlement.setVisible(true);
        settlement.setStyle("-fx-fill: " + getPlayerColor(occupant)
                + "; -fx-stroke: #1f1a13; -fx-stroke-width: 2;");
    }

    private String getPlayerColor(Player player) {
        switch (player.getColor()) {
            case RED:
                return "#d62828";
            case BLUE:
                return "#1d4ed8";
            case ORANGE:
                return "#f97316";
            case WHITE:
                return "#f8fafc";
            default:
                return "#6b7280";
        }
    }

    private void selectNode(Circle circle) {
        if (selectedNode != null) {
            selectedNode.getStyleClass().remove("selected-node");
        }
        if (selectedEdge != null) {
            selectedEdge.getStyleClass().remove("selected-edge");
            selectedEdge = null;
        }

        selectedNode = circle;
        selectedNode.getStyleClass().add("selected-node");
    }

    private void selectEdge(Line line) {
        if (selectedEdge != null) {
            selectedEdge.getStyleClass().remove("selected-edge");
        }
        if (selectedNode != null) {
            selectedNode.getStyleClass().remove("selected-node");
            selectedNode = null;
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
