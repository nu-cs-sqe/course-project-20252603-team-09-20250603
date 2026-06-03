package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private final TurnManager turnManager;
    private final PlacementValidator placementValidator;
    private GamePhase currPhase;
    // TODO: add devCardDeck to constructor

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.dice = dice;
        this.turnManager = turnManager;
        this.placementValidator = new PlacementValidator(board);
        this.currPhase = GamePhase.SETUP;
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

    public void setCurrPhase(GamePhase Phase){
        this.currPhase = Phase;
    }

    public Player getPlayer(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Player not found");
    }

    public boolean PhaseSetupCheck(){
        return this.currPhase == GamePhase.SETUP;
    }

    public void advancePhase() {
        if (this.currPhase == GamePhase.SETUP) {
            this.currPhase = GamePhase.NORMAL_PLAY;
        }
        else if (this.currPhase == GamePhase.NORMAL_PLAY){
            this.currPhase = GamePhase.GAME_OVER;
        }
    }

    public boolean start(){
        return true;
    }

    public void build(Player currentPlayer, BuildType buildType, int locationId) {
        if (buildType == null){
            throw new IllegalArgumentException("Build type cannot be null");
        }
        String inventoryKey = getInventoryKey(buildType);
        Map<ResourceType, Integer> cost = getBuildCost(buildType);

        if(currentPlayer.getInventory().get(inventoryKey) <= 0){
            throw new IllegalStateException("Player does not have enough inventory");
        }

        if(currPhase != GamePhase.SETUP && !currentPlayer.hasResources(cost)){
            throw new IllegalStateException("Player does not have enough resources");
        }

        if(buildType == BuildType.ROAD){
            try {
                Edge edge = board.getEdge(locationId);
                edge.buildRoad(currentPlayer);
            }catch (IllegalPlacementException exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
        } else if (buildType == BuildType.SETTLEMENT){
            Node node = board.getNode(locationId);

            try {
                placementValidator.validateSettlementPlacement(node);
            }catch (IllegalPlacementException exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
            node.buildSettlement(currentPlayer);
        } else if(buildType == BuildType.CITY){
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

    private String getInventoryKey(BuildType buildType) {
        switch (buildType) {
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

    private Map<ResourceType, Integer> getBuildCost(BuildType buildType) {
        Map<ResourceType, Integer> cost = new HashMap<>();

        switch (buildType) {
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
