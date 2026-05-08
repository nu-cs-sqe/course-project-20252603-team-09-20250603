package domain;

public class GameInitializer {
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
}
