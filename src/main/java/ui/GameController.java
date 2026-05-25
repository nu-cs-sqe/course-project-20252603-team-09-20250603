package ui;

import domain.BuildType;
import domain.Game;
import domain.Player;

import java.util.Scanner;

public class GameController {
    private final Game game;
    private final Scanner scanner;

    public GameController(Game game, Readable input) {
        this.game = game;
        this.scanner = new Scanner(input);
    }

    public void handleBuild(Player currentPlayer) {
        BuildType buildType = getBuildTypeFromPlayer();
        int locationId = getBuildLocationFromPlayer(buildType);

        game.build(currentPlayer, buildType, locationId);
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
}
