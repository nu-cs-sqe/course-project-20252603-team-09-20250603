package domain;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
    void twoPlayers_SetupPlayers_InvalidNoCreation(){
        GameInitializer initializer = new GameInitializer();

        List<String> names = List.of("Cole", "Aryan");

        assertThrows(IllegalArgumentException.class, ()->initializer.setupPlayers(names));
    }

    @Test
    void threePlayers_SetupPlayers_ValidCreateThreePlayers_CorrectlyAssignsColors(){
        GameInitializer initializer = new GameInitializer();

        List<Player> players = initializer.setupPlayers(List.of("Cole", "Aryan", "Alvin"));

        assertEquals(3, players.size());
        assertEquals("Cole", players.get(0).getName());
        assertEquals("Aryan", players.get(1).getName());
        assertEquals("Alvin", players.get(2).getName());

        assertEquals(PlayerColor.RED, players.get(0).getColor());
        assertEquals(PlayerColor.BLUE, players.get(1).getColor());
        assertEquals(PlayerColor.ORANGE, players.get(2).getColor());
    }

    @Test
    void fourPlayers_SetupPlayers_ValidCreateFourPlayers_CorrectlyAssignsColors(){
        GameInitializer initializer = new GameInitializer();

        List<Player> players = initializer.setupPlayers(List.of("Cole", "Aryan", "Alvin Li", "Bennita"));

        assertEquals(4, players.size());
        assertEquals("Cole", players.get(0).getName());
        assertEquals("Aryan", players.get(1).getName());
        assertEquals("Alvin Li", players.get(2).getName());
        assertEquals("Bennita", players.get(3).getName());

        assertEquals(PlayerColor.RED, players.get(0).getColor());
        assertEquals(PlayerColor.BLUE, players.get(1).getColor());
        assertEquals(PlayerColor.ORANGE, players.get(2).getColor());
        assertEquals(PlayerColor.WHITE, players.get(3).getColor());
    }

    @Test
    void fivePlayers_SetupPlayers_InvalidNoCreation(){
        GameInitializer initializer = new GameInitializer();

        List<String> names = List.of("Cole", "Aryan", "Alvin", "Bennita", "Chris");

        assertThrows(IllegalArgumentException.class, ()->initializer.setupPlayers(names));
    }

    @Test
    void setupPlayers_BlankName_InvalidGame(){
        GameInitializer initializer = new GameInitializer();

        List<String> names = List.of("Cole", "", "Alvin");

        assertThrows(IllegalArgumentException.class, () -> initializer.setupPlayers(names));
    }

    @Test
    void setupPlayers_nullInput_InvalidGame(){
        GameInitializer initializer = new GameInitializer();

        List<String> names = null;

        assertThrows(IllegalArgumentException.class, () -> initializer.setupPlayers(names));
    }

    @Test
    void setupPlayers_DuplicateName_InvalidGame(){
        GameInitializer initializer = new GameInitializer();

        List<String> names = List.of("Cole", "Alvin", "Alvin");

        assertThrows(IllegalArgumentException.class, () -> initializer.setupPlayers(names));
    }

    @Test
    void setupGame_ValidNames_ReturnsConfiguredGameInSetupPhase(){
        GameInitializer initializer = new GameInitializer();

        Game game = initializer.setupGame(List.of("Alice", "Bob", "Carol"));

        assertNotNull(game);
        assertTrue(game.phaseSetupCheck());
        assertEquals(3, game.getPlayers().size());
        assertNotNull(game.getBoard());
        assertNotNull(game.getTurnManager());
    }

}
