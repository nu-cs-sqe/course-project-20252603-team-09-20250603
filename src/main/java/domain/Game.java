package domain;

import java.util.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    @SuppressFBWarnings(
            value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
            justification = "Game has to share/mutate the same TurnManager instance used by the controller."
    )
    private final TurnManager turnManager;
    private final PlacementValidator placementValidator;
    private Player largestArmyPlayer = null;
    private int largestArmySize = 2;
    private final DevCardDeck devCardDeck;
    private Player longestRoadPlayer;
    private GamePhase currPhase;
    private final Map<Integer, Node> setupSettlements;
    private final Map<Integer, Node> secondSetupSettlements;

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.dice = dice;
        this.turnManager = turnManager;
        this.placementValidator = new PlacementValidator(board);
        this.devCardDeck = new DevCardDeck();
        this.currPhase = GamePhase.SETUP;
        this.setupSettlements = new HashMap<>();
        this.secondSetupSettlements = new HashMap<>();
    }

    public Board getBoard() {
        return board;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public void setCurrPhase(GamePhase phase){
        this.currPhase = phase;
    }

    public Player getPlayer(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Player not found");
    }

    public boolean phaseSetupCheck(){
        return this.currPhase == GamePhase.SETUP;
    }

    public void advancePhase() {
        if (this.currPhase == GamePhase.SETUP) {
            distributeSetupResources();
            this.currPhase = GamePhase.NORMAL_PLAY;
        }
        else if (this.currPhase == GamePhase.NORMAL_PLAY){
            this.currPhase = GamePhase.GAME_OVER;
        }
    }

    public void distributeSetupResources() {
        for (Player player : players) {
            Node settlement = secondSetupSettlements.get(player.getId());
            if (settlement != null) {
                player.addResources(board.getAdjacentResources(settlement));
            }
        }
    }

    public void setNextDevCardType(DevCardType type) {
        this.devCardDeck.setNextCardType(type);
    }

    public void rollDice() {
        dice.roll();
    }

    public int getDie1() {
        return dice.getDie1();
    }

    public int getDie2() {
        return dice.getDie2();
    }

    public int getDieSum() {
        return dice.getDieSum();
    }

    public void build(Player currentPlayer, InfraType infraType, int locationId) {
        if (infraType == null){
            throw new IllegalArgumentException("Build type cannot be null");
        }
        String inventoryKey = getInventoryKey(infraType);
        Map<ResourceType, Integer> cost = getBuildCost(infraType);

        if(currentPlayer.getInventory().get(inventoryKey) <= 0){
            throw new IllegalStateException("Player does not have enough inventory");
        }

        if(currPhase != GamePhase.SETUP && !currentPlayer.hasResources(cost)){
            throw new IllegalStateException("Player does not have enough resources");
        }

        if(infraType == InfraType.ROAD){
            try {
                Edge edge = board.getEdge(locationId);
                if (currPhase == GamePhase.SETUP) {
                    Node recentSettlement = setupSettlements.get(currentPlayer.getId());
                    if (recentSettlement == null) {
                        throw new IllegalStateException("You must build a settlement before building a road during setup.");
                    }

                    placementValidator.validateInitialRoad(locationId, recentSettlement);
                } else {
                    placementValidator.validateRegularRoad(locationId, currentPlayer);
                }
                    edge.buildRoad(currentPlayer);
                    if (currPhase == GamePhase.SETUP) {
                        setupSettlements.remove(currentPlayer.getId());
                    }
           }catch (IllegalPlacementException exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
        } else if (infraType == InfraType.SETTLEMENT){
            Node node = board.getNode(locationId);

            try {
                placementValidator.validateSettlementPlacement(node);
            }catch (IllegalPlacementException exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
            boolean isSecondSetupSettlement = currPhase == GamePhase.SETUP
                    && countPlayerSettlements(currentPlayer) == 1;
            node.buildSettlement(currentPlayer);
            if (currPhase == GamePhase.SETUP) {
                setupSettlements.put(currentPlayer.getId(), node);
                if (isSecondSetupSettlement) {
                    secondSetupSettlements.put(currentPlayer.getId(), node);
                }
            }
        } else if(infraType == InfraType.CITY){
            try {
                Node node = board.getNode(locationId);
                node.buildCity(currentPlayer);
            }catch (IllegalPlacementException exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
        }

        currentPlayer.useInventoryItem(inventoryKey);

        if(infraType == InfraType.SETTLEMENT || infraType == InfraType.CITY){
            currentPlayer.addVictoryPoints(1);
        }

        if(currPhase != GamePhase.SETUP) {
            currentPlayer.useResources(cost);
        }
    }

    public void handleMoveRobberLocation(int newHexId) {

        Hex newHex = board.getHex(newHexId);

        if (newHex == null){
            throw new IllegalArgumentException("Selected hex does not exist.");
        }

        if (newHex.getHasRobber()) {
            throw new IllegalStateException("Selected hex already has the robber.");
        }

        for (Hex hex : board.getHexes()) {
            hex.setHasRobber(false);
        }

        newHex.setHasRobber(true);
    }
  
    public void handleMoveRobber(int roll, int newHexId, int activePlayerId, int victimId) {
        if (roll != 7){
            return;
        }

        Player activePlayer = victimId < 0 ? null : findPlayerById(activePlayerId);
        Player victim = victimId < 0 ? null : findPlayerById(victimId);

        board.moveRobberAndSteal(activePlayer, newHexId, victim);
    }
    private int countPlayerSettlements(Player player) {
        int count = 0;
        for (Node node : board.getNodes()) {
            if (player.equals(node.getNodeOccupant()) && node.getInfraType() == InfraType.SETTLEMENT) {
                count++;
            }
        }
        return count;
    }

    private String getInventoryKey(InfraType infraType) {
        switch (infraType) {
            case ROAD:
                return "roads";
            case SETTLEMENT:
                return "settlements";
            case CITY:
                return "cities";
            default:
                throw new IllegalArgumentException("Invalid build type.");
        }
    }

    private Map<ResourceType, Integer> getBuildCost(InfraType infraType) {
        Map<ResourceType, Integer> cost = new HashMap<>();

        switch (infraType) {
            case ROAD:
                cost.put(ResourceType.BRICK, 1);
                cost.put(ResourceType.WOOD, 1);
                break;
            case SETTLEMENT:
                cost.put(ResourceType.BRICK, 1);
                cost.put(ResourceType.WOOD, 1);
                cost.put(ResourceType.SHEEP, 1);
                cost.put(ResourceType.WHEAT, 1);
                break;
            case CITY:
                cost.put(ResourceType.WHEAT, 2);
                cost.put(ResourceType.ORE, 3);
                break;
            default:
                throw new IllegalArgumentException("Invalid build type.");
        }

        return cost;
    }

    public void drawDevCard(int currentPlayerId) {
        Player player = findPlayerById(currentPlayerId);

        // A dev card costs 1 Ore + 1 Wool (Sheep) + 1 Grain (Wheat).
        Map<ResourceType, Integer> cost = getDevCardCost();
        if (!player.hasResources(cost)) {
            throw new IllegalStateException("Player does not have enough resources to draw a dev card.");
        }

        // Draw first so an empty deck throws before the player is charged.
        DevCard randomCard = devCardDeck.drawCard();
        player.setDevCardHand(randomCard);
        player.useResources(cost);

        System.out.println("Player " + currentPlayerId + " drew a " + randomCard.getType());
    }

    private Map<ResourceType, Integer> getDevCardCost() {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.ORE, 1);
        cost.put(ResourceType.SHEEP, 1);
        cost.put(ResourceType.WHEAT, 1);
        return cost;
    }

    public void useDevCard(int currentPlayerId, DevCardType cardType, int targetHexId, int victimPlayerId,
                           ResourceType choice1, ResourceType choice2, ResourceType targetType) {

        Player player = findPlayerById(currentPlayerId);

        if (player.getHasPlayedDevCardThisTurn()) {
            throw new IllegalActionException("Player has already played a development card this turn.");
        }

        DevCard cardToPlay = player.getDevCardHand().stream()
                .filter(c -> c.getType() == cardType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player doesn't have this card type"));

        switch (cardType) {
            case KNIGHT:
                Player victim = victimPlayerId < 0 ? null : findPlayerById(victimPlayerId);
                cardToPlay.doKnightAction(player, this.board, targetHexId, victim);
                this.updateLargestArmyPlayer();
                break;
            case ROAD_BUILDING:
                cardToPlay.doRoadBuildingAction(player, this.board);
                break;
            case YEAR_OF_PLENTY:
                cardToPlay.doYearOfPlentyAction(player, this.board, choice1, choice2);
                break;
            case MONOPOLY:
                cardToPlay.doMonopolyAction(player, this.players, this.board, targetType);
                break;
            default:
                throw new UnsupportedOperationException("This development card type cannot be manually played.");
        }

        player.removeDevCard(cardToPlay);
        player.setHasPlayedDevCardThisTurn(true);
    }

    public void updateLargestArmyPlayer() {
        Player currentContender = largestArmyPlayer;
        int maxKnights = largestArmySize;

        for (Player p : players) {
            if (p.getPlayedKnightCount() > maxKnights) {
                maxKnights = p.getPlayedKnightCount();
                currentContender = p;
            }
        }

        if (currentContender != largestArmyPlayer) {
            if (largestArmyPlayer != null) {
                largestArmyPlayer.setHasLargestArmy(false);
            }

            largestArmyPlayer = currentContender;
            largestArmySize = maxKnights;

            largestArmyPlayer.setHasLargestArmy(true);
        }
    }

    private Player findPlayerById(int id) {
        return this.players.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + id));
    }

    public Player findPlayerByName(String name) {
        return this.players.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found with name: " + name));
    }

    public int calculateLongestRoad(Player player) {
        int longestRoad = 0;

        for (Node node : board.getNodes()) {
            int roadLength = calculateLongestRoadFromNode(player, node, new HashSet<>());
            longestRoad = Math.max(longestRoad, roadLength);
        }

        return longestRoad;
    }

    private int calculateLongestRoadFromNode(
            Player player,
            Node currentNode,
            Set<Edge> usedEdges
    ) {
        if (isBlockedByOtherPlayer(player, currentNode)) {
            return 0;
        }

        int longest = 0;

        for (Edge edge : board.getEdgesConnectedToNode(currentNode)) {
            if (edge.getEdgeOccupant() != player || usedEdges.contains(edge)) {
                continue;
            }

            usedEdges.add(edge);

            Node nextNode = getOtherNode(edge, currentNode);
            int pathLength = 1 + calculateLongestRoadFromNode(player, nextNode, usedEdges);

            longest = Math.max(longest, pathLength);

            usedEdges.remove(edge);
        }

        return longest;
    }

    private Node getOtherNode(Edge edge, Node currentNode) {
        if (edge.getNodeA().equals(currentNode)) {
            return edge.getNodeB();
        }

        return edge.getNodeA();
    }

    private boolean isBlockedByOtherPlayer(Player player, Node node) {
        return node.getNodeOccupant() != null && node.getNodeOccupant() != player;
    }

    public void updateLongestRoadBonus() {
        Player candidate = null;
        int candidateLength = 0;

        for (Player player : players) {
            int roadLength = calculateLongestRoad(player);

            if (roadLength >= 5 && roadLength > candidateLength) {
                candidate = player;
                candidateLength = roadLength;
            }
        }

        if (candidate == null || candidate == longestRoadPlayer) {
            return;
        }

        if (longestRoadPlayer != null) {
            longestRoadPlayer.removeVictoryPoints(2);
        }

        candidate.addVictoryPoints(2);
        longestRoadPlayer = candidate;
    }
}
