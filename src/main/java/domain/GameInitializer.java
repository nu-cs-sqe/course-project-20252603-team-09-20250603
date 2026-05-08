package domain;

public class GameInitializer {
    public void validatePlayerCount(int count){
        if (count < 3 || count > 4){
            throw new IllegalArgumentException("Catan requires players of 3 or 4");
        }
    }
}
