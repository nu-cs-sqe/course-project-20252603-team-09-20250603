package domain;

import java.util.ArrayList;
import java.util.List;

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

        if(currentPlayer.getInventory().get(inventoryKey) <= 0){
            throw new IllegalStateException("Player does not have enough inventory");
        }

        if(buildType == BuildType.ROAD){
            Edge edge = board.getEdge(locationId);
            edge.buildRoad(currentPlayer);
        } else if (buildType == BuildType.SETTLEMENT){
            Node node = board.getNode(locationId);

            try {
                placementValidator.validateSettlementPlacement(node);
            }catch (IllegalPlacementException exception) {
                throw new IllegalStateException(exception.getMessage(), exception);
            }
            node.buildSettlement(currentPlayer);
        } else if(buildType == BuildType.CITY){
            Node node = board.getNode(locationId);
            node.buildCity(currentPlayer);
        } else {
            throw new IllegalArgumentException("Invalid build type.");
        }

        currentPlayer.useInventoryItem(inventoryKey);
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
}
