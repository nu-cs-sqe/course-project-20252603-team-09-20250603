package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private GamePhase currPhase;
    private final Map<Integer, Node> setupSettlements;
    private final Map<Integer, Node> secondSetupSettlements;
    // TODO: add devCardDeck to constructor

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.dice = dice;
        this.turnManager = turnManager;
        this.placementValidator = new PlacementValidator(board);
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

    public boolean start(){
        return true;
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
                    // TODO placement validator for a regular raod
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
        } else {
            throw new IllegalArgumentException("Invalid build type.");
        }

        currentPlayer.useInventoryItem(inventoryKey);

        if(currPhase != GamePhase.SETUP) {
            currentPlayer.useResources(cost);
        }
    }

    public void handleMoveRobber(int roll, int newHexId) {
        if (roll != 7){
            return;
        }

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
}
