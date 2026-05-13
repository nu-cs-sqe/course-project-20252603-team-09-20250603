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
        nodeToHexes.put(nodes.get(0), List.of(hexes.get(0)));
    }

    public List<Hex> getHexesFromNode(Node node) {
        return nodeToHexes.get(node);
    }
}
