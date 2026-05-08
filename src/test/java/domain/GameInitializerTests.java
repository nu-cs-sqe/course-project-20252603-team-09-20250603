package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameInitializerTests {
    @Test
    void twoPlayerCount_InvalidGame(){
        GameInitializer initializer = new GameInitializer();

        assertThrows(IllegalArgumentException.class, ()->initializer.validatePlayerCount(2));
    }

    @Test
    void threePlayerCount_ValidGame(){
        GameInitializer initializer = new GameInitializer();

        assertDoesNotThrow(() -> initializer.validatePlayerCount(3));
    }

    @Test
    void fourPlayerCount_ValidGame(){
        GameInitializer initializer = new GameInitializer();

        assertDoesNotThrow(() -> initializer.validatePlayerCount(4));
    }

    @Test
    void fivePlayerCount_InvalidGame(){
        GameInitializer initializer = new GameInitializer();

        assertThrows(IllegalArgumentException.class, () -> initializer.validatePlayerCount(5));
    }

    @Test
    void onlyFirstName_NoSpace_ValidName(){
        GameInitializer initializer = new GameInitializer();

        String name = "Cole";
        assertDoesNotThrow(()->initializer.validatePlayerName(name));
    }

    @Test
    void firstAndLastName_WithSpace_ValidName(){
        GameInitializer initializer = new GameInitializer();

        String name = "Alvin Li";
        assertDoesNotThrow(()->initializer.validatePlayerName(name));
    }

    @Test
    void blankName_InvalidName(){
        GameInitializer initializer = new GameInitializer();

        String name = "";
        assertThrows(IllegalArgumentException.class, ()->initializer.validatePlayerName(name));
    }

    @Test
    void whiteSpaceName_InvalidName(){
        GameInitializer initializer = new GameInitializer();

        String name = " ";
        assertThrows(IllegalArgumentException.class, ()->initializer.validatePlayerName(name));
    }

    @Test
    void nullName_InvalidName(){
        GameInitializer initializer = new GameInitializer();

        String name = null;
        assertThrows(IllegalArgumentException.class, ()->initializer.validatePlayerName(name));
    }

}
