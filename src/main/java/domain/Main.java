package domain;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameInitializer initializer = new GameInitializer();

        List<String> playerNames = Arrays.asList(args);

        Game game = initializer.setupGame(playerNames);

        System.out.println("Game created successfully!");

        //TODO: game.play()
    }
}

