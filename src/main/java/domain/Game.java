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
    // TODO: add devCardDeck to constructor

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.dice = dice;
        this.turnManager = turnManager;
        this.placementValidator = new PlacementValidator(board);
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

        if(!currentPlayer.hasResources(cost)){
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
        currentPlayer.useResources(cost);

        if(buildType == BuildType.SETTLEMENT){
            currentPlayer.addVictoryPoints(1);
        }else if(buildType == BuildType.CITY) {
            currentPlayer.addVictoryPoints(1);
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
