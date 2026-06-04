package domain;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        GameInitializer initializer = new GameInitializer();

        Game game = initializer.setupGame(List.of(args));

        System.out.println("Game created successfully!");

        System.out.println(game.start()); //printing start as true for now for checkstyle and spotbug errors
    }
}

