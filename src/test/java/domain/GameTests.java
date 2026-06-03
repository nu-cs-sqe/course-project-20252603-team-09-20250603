package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    private Game game;
    private Player player0;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player0 = new Player(0, "Ben", PlayerColor.RED);
        player1 = new Player(1, "Benny", PlayerColor.BLUE);
        player2 = new Player(2, "Benji", PlayerColor.ORANGE);

        game = new Game(
                new Board(),
                List.of(player0, player1, player2),
                new Dice(new Random()),
                new TurnManager(3)
        );
    }

    @Test
    public void getPlayer_FirstPlayer_ReturnsCorrectPlayer() {
        assertEquals(player0, game.getPlayer(0));
    }

    @Test
    public void getPlayer_LastPlayer_ReturnsCorrectPlayer() {
        assertEquals(player2, game.getPlayer(2));
    }

    @Test
    public void getPlayer_MiddlePlayer_ReturnsCorrectPlayer() {
        assertEquals(player1, game.getPlayer(1));
    }

    @Test
    public void getPlayer_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> game.getPlayer(99));
    }


    @Test
    public void phaseSetupCheck_WhenSetup_ReturnsTrue() {
        game.setCurrPhase(GamePhase.SETUP);
        assertTrue(game.PhaseSetupCheck());
    }

    @Test
    public void phaseSetupCheck_WhenNormalPlay_ReturnsFalse() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        assertFalse(game.PhaseSetupCheck());
    }

    @Test
    public void phaseSetupCheck_WhenGameOver_ReturnsFalse() {
        game.setCurrPhase(GamePhase.GAME_OVER);
        assertFalse(game.PhaseSetupCheck());
    }
}