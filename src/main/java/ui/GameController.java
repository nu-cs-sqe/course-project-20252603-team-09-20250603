package ui;

import domain.InfraType;
import domain.Game;
import domain.Player;

import java.util.Scanner;

public class GameController {
    private final Game game;
    private final Scanner scanner;

    // The controller must drive the same live Game instance, so sharing the
    // reference is intentional rather than a leak of internal state.
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public GameController(Game game, Readable input) {
        this.game = game;
        this.scanner = new Scanner(input);
    }

    public void handleBuild(Player currentPlayer) {
        InfraType infraType = getInfraTypeFromPlayer();
        int locationId = getBuildLocationFromPlayer(infraType);

        game.build(currentPlayer, infraType, locationId);
    }

    private InfraType getInfraTypeFromPlayer() {
        System.out.println("What would you like to build?");
        System.out.println("1. Road");
        System.out.println("2. Settlement");
        System.out.println("3. City");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                return InfraType.ROAD;
            case 2:
                return InfraType.SETTLEMENT;
            case 3:
                return InfraType.CITY;
            default:
                throw new IllegalArgumentException("Invalid build type selection.");
        }
    }

    private int getBuildLocationFromPlayer(InfraType infraType) {
        if (infraType == InfraType.ROAD) {
            System.out.println("Which edge would you like to build on?");
        } else {
            System.out.println("Which node would you like to build on?");
        }

        return scanner.nextInt();
    }
}
