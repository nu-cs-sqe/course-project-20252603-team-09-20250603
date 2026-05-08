package domain;

public class GameInitializer {
    private static final PlayerColor[] COLORS = {
            PlayerColor.RED,
            PlayerColor.BLUE,
            PlayerColor.ORANGE,
            PlayerColor.WHITE
    };

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
