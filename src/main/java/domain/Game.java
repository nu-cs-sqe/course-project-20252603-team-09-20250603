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
    private Player largestArmyPlayer = null;
    private int largestArmySize = 2;
    private final DevCardDeck devCardDeck;

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.dice = dice;
        this.turnManager = turnManager;
        this.placementValidator = new PlacementValidator(board);
        this.devCardDeck = new DevCardDeck();
    }

    public Board getBoard() { return this.board; }

    public boolean start(){
        return true;
    }

    public DevCardDeck getDevCardDeck() {
        return this.devCardDeck;
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

        if(!currentPlayer.hasResources(cost)){
            throw new IllegalStateException("Player does not have enough resources");
        }

        if(infraType == InfraType.ROAD){
            try {
                Edge edge = board.getEdge(locationId);
                edge.buildRoad(currentPlayer);
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
            node.buildSettlement(currentPlayer);
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
        currentPlayer.useResources(cost);
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

        DevCard randomCard = devCardDeck.drawCard();
        player.setDevCardHand(randomCard);

        System.out.println("Player " + currentPlayerId + " drew a " + randomCard.getType());
    }

    public void useDevCard(int currentPlayerId, DevCardType cardType, int targetHexId,
                           ResourceType choice1, ResourceType choice2, ResourceType targetType) {

        Player player = findPlayerById(currentPlayerId);

        DevCard cardToPlay = player.getDevCardHand().stream()
                .filter(c -> c.getType() == cardType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player doesn't have this card type"));

        switch (cardType) {
            case KNIGHT:
                cardToPlay.doKnightAction(player, this.board, targetHexId);
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

        player.getDevCardHand().remove(cardToPlay);
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

}
