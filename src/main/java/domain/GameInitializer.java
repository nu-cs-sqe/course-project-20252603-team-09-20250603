package domain;

import java.util.*;

public class GameInitializer {
    private static final PlayerColor[] COLORS = {
            PlayerColor.RED,
            PlayerColor.BLUE,
            PlayerColor.ORANGE,
            PlayerColor.WHITE
    };

    public Game setupGame(List<String> playerNames){
        List<Player> players = setupPlayers(playerNames);
        Board board = new Board();
        Dice dice = new Dice(new Random());
        TurnManager turnManager = new TurnManager(players.size());
        Game game = new Game(board, players, dice, turnManager);
        game.setCurrPhase(GamePhase.SETUP);
        return game;
    }

    public List<Player> setupPlayers(List<String> names){
        if (names == null){
            throw new IllegalArgumentException("No players entered");
        }

        validatePlayerCount(names.size());

        List<Player> players = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for(int i = 0; i < names.size(); i++){
            String name = names.get(i);
            validatePlayerName(name);

            String normalizedName = name.trim().toLowerCase();

            if(!usedNames.add(normalizedName)){
                throw new IllegalArgumentException("Player names must be unique");
            }

            PlayerColor color = assignColor(i);
            Player player = new Player(i, names.get(i), color);

            players.add(player);
        }

        // TODO: perhaps randomize and assign order or just keep as is

        return players;
    }

    public void validatePlayerCount(int count){
        if (count < 3 || count > 4){
            throw new IllegalArgumentException("Catan requires players of 3 or 4");
        }
    }

    public void validatePlayerName(String name){
        if (name == null || name.trim().isBlank()){
            throw new IllegalArgumentException("Player name invalid, please input a name");
        }
    }

    public PlayerColor assignColor(int index){
        if (index < 0 || index >= 4){
            throw new IllegalArgumentException("Invalid player index for color assignment");
        }

        return COLORS[index];
    }
}
