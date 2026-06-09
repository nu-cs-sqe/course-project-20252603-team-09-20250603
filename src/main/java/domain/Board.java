package domain;

import java.util.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Board {

    private static final int NUM_HEXES = 19;
    private static final int NUM_NODES = 54;

    private List<Hex> hexes;
    private List<Node> nodes;
    private List<Edge> edges;

    private Map<Node, List<Hex>> nodeToHexes;
    private Map<Hex, List<Node>> hexToNodes;

    private final Random random;

    public Board() {
        this(new Random());
    }

    @SuppressFBWarnings(
            value = "EI_EXPOSE_REP2",
            justification = "Board intentionally shares the caller's Random so steals can be seeded deterministically in tests."
    )
    public Board(Random random) {
        this.random = random;
        this.hexes = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.nodeToHexes = new HashMap<>();
        this.hexToNodes = new HashMap<>();

        initializeBoard();
    }

    private void initializeBoard() {

        ResourceType[] hexResources = {
                ResourceType.BRICK,
                ResourceType.WOOD,
                ResourceType.SHEEP,
                ResourceType.WHEAT,
                ResourceType.WOOD,
                ResourceType.WHEAT,
                ResourceType.SHEEP,
                ResourceType.SHEEP,
                ResourceType.ORE,
                ResourceType.DESERT,
                ResourceType.ORE,
                ResourceType.WHEAT,
                ResourceType.BRICK,
                ResourceType.SHEEP,
                ResourceType.BRICK,
                ResourceType.WHEAT,
                ResourceType.WOOD,
                ResourceType.WOOD,
                ResourceType.ORE
        };

        int[] hexTokens = {
                9,
                11,
                6,
                4,
                3,
                2,
                5,
                11,
                8,
                0,
                10,
                9,
                10,
                5,
                12,
                4,
                6,
                3,
                8
        };

        int robberHexId = 9;

        for (int id = 0; id < NUM_HEXES; id++) {
            Hex hex = new Hex(id);
            hex.setResourceType(hexResources[id]);
            hex.setTokenNumber(hexTokens[id]);
            hex.setHasRobber(id == robberHexId);
            hexes.add(hex);
        }

        for (int id = 0; id < NUM_NODES; id++) {
            Node node = new Node(id);
            nodes.add(node);
        }

        buildNodeToHexes();
        buildHexToNodes();
        buildEdges();
    }

    private void buildHexToNodes() {
        hexToNodes.put(hexes.get(0), List.of(nodes.get(0), nodes.get(1), nodes.get(2), nodes.get(10), nodes.get(9), nodes.get(8)));
        hexToNodes.put(hexes.get(1), List.of(nodes.get(2), nodes.get(3), nodes.get(4), nodes.get(12), nodes.get(11), nodes.get(10)));
        hexToNodes.put(hexes.get(2), List.of(nodes.get(4), nodes.get(5), nodes.get(6), nodes.get(14), nodes.get(13), nodes.get(12)));
        hexToNodes.put(hexes.get(3), List.of(nodes.get(7), nodes.get(8), nodes.get(9), nodes.get(19), nodes.get(18), nodes.get(17)));
        hexToNodes.put(hexes.get(4), List.of(nodes.get(9), nodes.get(10), nodes.get(11), nodes.get(21), nodes.get(20), nodes.get(19)));
        hexToNodes.put(hexes.get(5), List.of(nodes.get(11), nodes.get(12), nodes.get(13), nodes.get(23), nodes.get(22), nodes.get(21)));
        hexToNodes.put(hexes.get(6), List.of(nodes.get(13), nodes.get(14), nodes.get(15), nodes.get(25), nodes.get(24), nodes.get(23)));
        hexToNodes.put(hexes.get(7), List.of(nodes.get(16), nodes.get(17), nodes.get(18), nodes.get(29), nodes.get(28), nodes.get(27)));
        hexToNodes.put(hexes.get(8), List.of(nodes.get(18), nodes.get(19), nodes.get(20), nodes.get(31), nodes.get(30), nodes.get(29)));
        hexToNodes.put(hexes.get(9), List.of(nodes.get(20), nodes.get(21), nodes.get(22), nodes.get(33), nodes.get(32), nodes.get(31)));
        hexToNodes.put(hexes.get(10), List.of(nodes.get(22), nodes.get(23), nodes.get(24), nodes.get(35), nodes.get(34), nodes.get(33)));
        hexToNodes.put(hexes.get(11), List.of(nodes.get(24), nodes.get(25), nodes.get(26), nodes.get(37), nodes.get(36), nodes.get(35)));
        hexToNodes.put(hexes.get(12), List.of(nodes.get(28), nodes.get(29), nodes.get(30), nodes.get(40), nodes.get(39), nodes.get(38)));
        hexToNodes.put(hexes.get(13), List.of(nodes.get(30), nodes.get(31), nodes.get(32), nodes.get(42), nodes.get(41), nodes.get(40)));
        hexToNodes.put(hexes.get(14), List.of(nodes.get(32), nodes.get(33), nodes.get(34), nodes.get(44), nodes.get(43), nodes.get(42)));
        hexToNodes.put(hexes.get(15), List.of(nodes.get(34), nodes.get(35), nodes.get(36), nodes.get(46), nodes.get(45), nodes.get(44)));
        hexToNodes.put(hexes.get(16), List.of(nodes.get(39), nodes.get(40), nodes.get(41), nodes.get(49), nodes.get(48), nodes.get(47)));
        hexToNodes.put(hexes.get(17), List.of(nodes.get(41), nodes.get(42), nodes.get(43), nodes.get(51), nodes.get(50), nodes.get(49)));
        hexToNodes.put(hexes.get(18), List.of(nodes.get(43), nodes.get(44), nodes.get(45), nodes.get(53), nodes.get(52), nodes.get(51)));
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

    private void buildEdges() {
        Set<String> seenEdges = new HashSet<>();
        int edgeId = 0;

        // iterate around each hex and connect adjacent nodes with edges
        for (List<Node> nodes : hexToNodes.values()) {
            for (int i = 0; i < 6; i++) {
                Node nodeA = nodes.get(i);
                Node nodeB = nodes.get((i + 1) % 6);

                int min = Math.min(nodeA.getId(), nodeB.getId());
                int max = Math.max(nodeA.getId(), nodeB.getId());
                String key = min + "-" + max;

                if (!seenEdges.contains(key)) {
                    seenEdges.add(key);
                    Edge newEdge = new Edge(edgeId++);
                    newEdge.setNodeA(nodeA);
                    newEdge.setNodeB(nodeB);
                    edges.add(newEdge);
                }
            }
        }
    }

    public Map<Node, List<Hex>> getNodeToHexesMap() {
        return Collections.unmodifiableMap(this.nodeToHexes);
    }

    public List<Hex> getHexesFromNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("The node object is null");
        }

        if (!nodeToHexes.containsKey(node)) {
            throw new IllegalStateException("The node object is not valid");
        }

        return new ArrayList<>(nodeToHexes.get(node));
    }

    public Node getNode(int nodeId) {
        if (nodeId < 0 || nodeId >= nodes.size()) {
            throw new IllegalArgumentException("Invalid node ID.");
        }

        return nodes.get(nodeId);
    }

    public Edge getEdge(int edgeId) {
        if (edgeId < 0 || edgeId >= edges.size()) {
            throw new IllegalArgumentException("Invalid edge ID.");
        }

        return edges.get(edgeId);
    }

    public Hex getHex(int hexId) {
        if (hexId < 0 || hexId >= hexes.size()) {
            throw new IllegalArgumentException("Invalid Hex Id");
        }

        return hexes.get(hexId);
    }

    public Map<ResourceType, Integer> getAdjacentResources(Node node) {
        Map<ResourceType, Integer> resources = new HashMap<>();

        List<Hex> adjacentHexes = getHexesFromNode(node);

        for (Hex hex : adjacentHexes) {

            ResourceType resource = hex.getResourceType();

            resources.put(
                    resource,
                    resources.getOrDefault(resource, 0) + 1
            );
        }

        return resources;
    }

    public void distributeResourcesOnRoll(int diceRoll) {
        if (diceRoll < 2 || diceRoll > 12) {
            throw new IllegalArgumentException("Dice roll must be between 2 and 12.");
        }
        for (Hex hex : hexes) {
            if (hex.getTokenNumber() != diceRoll) {
                continue;
            }

            if (hex.getHasRobber()) {
                continue;
            }

            ResourceType resourceType = hex.getResourceType();

            for (Node node : hexToNodes.get(hex)) {
                Player occupant = node.getNodeOccupant();

                if (occupant != null) {
                    int amount = 0;

                    if (node.getInfraType() == InfraType.SETTLEMENT) {
                        amount = 1;
                    } else if (node.getInfraType() == InfraType.CITY) {
                        amount = 2;
                    }

                    if (amount > 0) {
                        Map<ResourceType, Integer> resources = new HashMap<>();
                        resources.put(resourceType, amount);
                        occupant.addResources(resources);
                    }
                }
            }
        }
    }

    public List<Hex> getHexes() {
        return List.copyOf(hexes);
    }

    public List<Node> getNodes() {
        return List.copyOf(nodes);
    }

    public List<Edge> getEdges() {
        return List.copyOf(edges);
    }

    public void moveRobber(Player player, int targetHexId) {
        Hex targetHex = getHex(targetHexId);

        if (targetHex.getHasRobber()) {
            throw new IllegalStateException("Selected hex already has the robber.");
        }

        for (Hex hex : hexes) {
            hex.setHasRobber(false);
        }

        targetHex.setHasRobber(true);
    }

    /**
     * moves the robber to targetHexId and steals one random resource card from victim.
     * The victim must own a building on the target hex and cannot be the active player.
     * Passing null for the victim moves the robber w/o stealing (no opponent borders the hex).
     */
    public void moveRobberAndSteal(Player activePlayer, int targetHexId, Player victim) {
        if (victim != null) {
            if (victim == activePlayer) {
                throw new IllegalArgumentException("A player cannot steal from themselves.");
            }
            if (!getPlayersOnHex(targetHexId).contains(victim)) {
                throw new IllegalArgumentException("Cannot steal from a player with no building on the robber's hex.");
            }
        }

        moveRobber(activePlayer, targetHexId);

        if (victim != null) {
            stealRandomResource(activePlayer, victim);
        }
    }

    /** Distinct players owning a settlement or city on a node bordering the hex. */
    public List<Player> getPlayersOnHex(int hexId) {
        Hex hex = getHex(hexId);
        List<Player> occupants = new ArrayList<>();

        for (Node node : hexToNodes.get(hex)) {
            Player occupant = node.getNodeOccupant();
            if (occupant != null && !occupants.contains(occupant)) {
                occupants.add(occupant);
            }
        }

        return occupants;
    }

    private void stealRandomResource(Player thief, Player victim) {
        List<ResourceType> cards = new ArrayList<>();
        for (Map.Entry<ResourceType, Integer> entry : victim.getResources().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                cards.add(entry.getKey());
            }
        }

        if (cards.isEmpty()) {
            return;
        }

        ResourceType stolen = cards.get(random.nextInt(cards.size()));
        Map<ResourceType, Integer> oneCard = new HashMap<>();
        oneCard.put(stolen, 1);

        victim.useResources(oneCard);
        thief.addResources(oneCard);
    }

    public int getRobberHexId() {
        for (Hex hex : hexes) {
            if (hex.getHasRobber()) {
                return hex.getId();
            }
        }
        return -1;
    }

    public void freeRoads(Player player, int count) {}
    public List<Edge> getEdgesConnectedToNode(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("The node object is null");
        }

        if (!nodes.contains(node)) {
            throw new IllegalStateException("The node object is not valid");
        }

        List<Edge> connectedEdges = new ArrayList<>();

        for (Edge edge : edges) {
            if (edge.getNodeA().equals(node) || edge.getNodeB().equals(node)) {
                connectedEdges.add(edge);
            }
        }

        return connectedEdges;
    }
}
