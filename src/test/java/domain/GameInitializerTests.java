package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void firstPlayerIndex_AssignedRed(){
        GameInitializer initializer = new GameInitializer();

        assertEquals(PlayerColor.RED, initializer.assignColor(0));
    }

    @Test
    void firstPlayerIndex_AssignedBlue(){
        GameInitializer initializer = new GameInitializer();

        assertEquals(PlayerColor.BLUE, initializer.assignColor(1));
    }

    @Test
    void firstPlayerIndex_AssignedOrange(){
        GameInitializer initializer = new GameInitializer();

        assertEquals(PlayerColor.ORANGE, initializer.assignColor(2));
    }

    @Test
    void firstPlayerIndex_AssignedWhite(){
        GameInitializer initializer = new GameInitializer();

        assertEquals(PlayerColor.WHITE, initializer.assignColor(3));
    }

    @Test
    void fifthPlayerIndex_InvalidColorIndex(){
        GameInitializer initializer = new GameInitializer();

        assertThrows(IllegalArgumentException.class, ()->initializer.assignColor(4));
    }

    @Test
    void firstPlayerIndex_AssignedAllUniqueColors(){
        GameInitializer initializer = new GameInitializer();

        PlayerColor color1 = initializer.assignColor(0);
        PlayerColor color2 = initializer.assignColor(1);
        PlayerColor color3 = initializer.assignColor(2);
        PlayerColor color4 = initializer.assignColor(3);

        assertNotEquals(color1, color2);
        assertNotEquals(color1, color3);
        assertNotEquals(color1, color4);
        assertNotEquals(color2, color3);
        assertNotEquals(color2, color4);
        assertNotEquals(color3, color4);
    }

}
