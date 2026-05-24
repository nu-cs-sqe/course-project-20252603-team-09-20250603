package domain;

import java.util.List;
import java.util.Scanner;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private final TurnManager turnManager;
    private final Scanner scanner;
    // TODO: add devCardDeck to constructor

    public Game(Board board, List<Player> players, Dice dice, TurnManager turnManager) {
        this.board = board;
        this.players = players;
        this.dice = dice;
        this.turnManager = turnManager;
        this.scanner = new Scanner(System.in);
    }

    public void handleBuild(Player currentPlayer) {
        BuildType buildType = getBuildTypeFromPlayer();
        int locationId = getBuildLocationFromPlayer(buildType);

        build(currentPlayer, buildType, locationId);
    }

    public void build(Player currentPlayer, BuildType buildType, int locationId) {
        String inventoryKey = getInventoryKey(buildType);

        if(currentPlayer.getInventory().get(inventoryKey) <= 0){
            throw new IllegalStateException("Player does not have enough inventory");
        }

        if(buildType == BuildType.ROAD){
            Edge edge = board.getEdge(locationId);
            edge.buildRoad(currentPlayer);
        } else if (buildType == BuildType.SETTLEMENT){
            Node node = board.getNode(locationId);
            node.buildSettlement(currentPlayer);
        } else if(buildType == BuildType.CITY){
            Node node = board.getNode(locationId);
            node.buildCity(currentPlayer);
        } else {
            throw new IllegalArgumentException("Invalid build type.");
        }

        currentPlayer.useInventoryItem(inventoryKey);
    }

    private BuildType getBuildTypeFromPlayer() {
        System.out.println("What would you like to build?");
        System.out.println("1. Road");
        System.out.println("2. Settlement");
        System.out.println("3. City");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                return BuildType.ROAD;
            case 2:
                return BuildType.SETTLEMENT;
            case 3:
                return BuildType.CITY;
            default:
                throw new IllegalArgumentException("Invalid build type selection.");
        }
    }

    private int getBuildLocationFromPlayer(BuildType buildType) {
        if (buildType == BuildType.ROAD) {
            System.out.println("Which edge would you like to build on?");
        } else {
            System.out.println("Which node would you like to build on?");
        }

        return scanner.nextInt();
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
