package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private static final int NUM_HEXES = 19;
    private static final int NUM_NODES = 54;

    private List<Hex> hexes;
    private List<Node> nodes;
    private Map<Node, List<Hex>> nodeToHexes;

    public Board() {
        this.hexes = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.nodeToHexes = new HashMap<>();

        initializeBoard();
    }

    private void initializeBoard() {
        for (int id = 0; id < NUM_HEXES; id++) {
            Hex hex = new Hex(id);
            hexes.add(hex);
        }
        for (int id = 0; id < NUM_NODES; id++) {
            Node node = new Node(id);
            nodes.add(node);
        }

        buildNodeToHexes();

    }

    private void buildNodeToHexes() {
        nodeToHexes.put(nodes.get(0), List.of(hexes.get(0)));
        nodeToHexes.put(nodes.get(1), List.of(hexes.get(0)));
        nodeToHexes.put(nodes.get(2), List.of(hexes.get(0), hexes.get(1)));
        nodeToHexes.put(nodes.get(3), List.of(hexes.get(1)));
        nodeToHexes.put(nodes.get(4), List.of(hexes.get(1), hexes.get(2)));
        nodeToHexes.put(nodes.get(5), List.of(hexes.get(2)));
        nodeToHexes.put(nodes.get(6), List.of(hexes.get(2)));
        nodeToHexes.put(nodes.get(7), List.of(hexes.get(3)));
        nodeToHexes.put(nodes.get(8), List.of(hexes.get(0), hexes.get(3)));
        nodeToHexes.put(nodes.get(9), List.of(hexes.get(0), hexes.get(3), hexes.get(4)));
        nodeToHexes.put(nodes.get(10), List.of(hexes.get(0), hexes.get(1), hexes.get(4)));
        nodeToHexes.put(nodes.get(11), List.of(hexes.get(1), hexes.get(4), hexes.get(5)));
        nodeToHexes.put(nodes.get(12), List.of(hexes.get(1), hexes.get(2), hexes.get(5)));
        nodeToHexes.put(nodes.get(13), List.of(hexes.get(2), hexes.get(5), hexes.get(6)));
        nodeToHexes.put(nodes.get(14), List.of(hexes.get(2), hexes.get(6)));
        nodeToHexes.put(nodes.get(15), List.of(hexes.get(6)));
        nodeToHexes.put(nodes.get(16), List.of(hexes.get(7)));
        nodeToHexes.put(nodes.get(17), List.of(hexes.get(3), hexes.get(7)));
        nodeToHexes.put(nodes.get(18), List.of(hexes.get(3), hexes.get(7), hexes.get(8)));
        nodeToHexes.put(nodes.get(19), List.of(hexes.get(3), hexes.get(4), hexes.get(8)));
        nodeToHexes.put(nodes.get(20), List.of(hexes.get(4), hexes.get(8), hexes.get(9)));
        nodeToHexes.put(nodes.get(21), List.of(hexes.get(4), hexes.get(5), hexes.get(9)));
        nodeToHexes.put(nodes.get(22), List.of(hexes.get(5), hexes.get(9), hexes.get(10)));
        nodeToHexes.put(nodes.get(23), List.of(hexes.get(5), hexes.get(6), hexes.get(10)));
        nodeToHexes.put(nodes.get(24), List.of(hexes.get(6), hexes.get(10), hexes.get(11)));
        nodeToHexes.put(nodes.get(25), List.of(hexes.get(6), hexes.get(11)));
        nodeToHexes.put(nodes.get(26), List.of(hexes.get(11)));
        nodeToHexes.put(nodes.get(27), List.of(hexes.get(7)));
        nodeToHexes.put(nodes.get(28), List.of(hexes.get(7), hexes.get(12)));
        nodeToHexes.put(nodes.get(29), List.of(hexes.get(7), hexes.get(8), hexes.get(12)));
        nodeToHexes.put(nodes.get(30), List.of(hexes.get(8), hexes.get(12), hexes.get(13)));
        nodeToHexes.put(nodes.get(31), List.of(hexes.get(8), hexes.get(9), hexes.get(13)));
        nodeToHexes.put(nodes.get(32), List.of(hexes.get(9), hexes.get(13), hexes.get(14)));
        nodeToHexes.put(nodes.get(33), List.of(hexes.get(9), hexes.get(10), hexes.get(14)));
        nodeToHexes.put(nodes.get(34), List.of(hexes.get(10), hexes.get(14), hexes.get(15)));
        nodeToHexes.put(nodes.get(35), List.of(hexes.get(10), hexes.get(11), hexes.get(15)));
        nodeToHexes.put(nodes.get(36), List.of(hexes.get(11), hexes.get(15)));
        nodeToHexes.put(nodes.get(37), List.of(hexes.get(11)));
        nodeToHexes.put(nodes.get(38), List.of(hexes.get(12)));
        nodeToHexes.put(nodes.get(39), List.of(hexes.get(12), hexes.get(16)));
        nodeToHexes.put(nodes.get(40), List.of(hexes.get(12), hexes.get(13), hexes.get(16)));
        nodeToHexes.put(nodes.get(41), List.of(hexes.get(13), hexes.get(16), hexes.get(17)));
        nodeToHexes.put(nodes.get(42), List.of(hexes.get(13), hexes.get(14), hexes.get(17)));
        nodeToHexes.put(nodes.get(43), List.of(hexes.get(14), hexes.get(17), hexes.get(18)));
        nodeToHexes.put(nodes.get(44), List.of(hexes.get(14), hexes.get(15), hexes.get(18)));
        nodeToHexes.put(nodes.get(45), List.of(hexes.get(15), hexes.get(18)));
        nodeToHexes.put(nodes.get(46), List.of(hexes.get(15)));
        nodeToHexes.put(nodes.get(47), List.of(hexes.get(16)));
        nodeToHexes.put(nodes.get(48), List.of(hexes.get(16)));
        nodeToHexes.put(nodes.get(49), List.of(hexes.get(16), hexes.get(17)));
        nodeToHexes.put(nodes.get(50), List.of(hexes.get(17)));
        nodeToHexes.put(nodes.get(51), List.of(hexes.get(17), hexes.get(18)));
        nodeToHexes.put(nodes.get(52), List.of(hexes.get(18)));
        nodeToHexes.put(nodes.get(53), List.of(hexes.get(18)));
    }

    public Map<Node, List<Hex>> getNodeToHexesMap() {
        return this.nodeToHexes;
    }

    public List<Hex> getHexesFromNode(Node node) {
        if (node == null) {
            throw new IllegalStateException("The node object is null");
        }
        if (!nodeToHexes.containsKey(node)) {
            throw new IllegalStateException("The node object is not valid");
        }
        return nodeToHexes.get(node);
    }

}