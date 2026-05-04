package domain.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnManagerTest {

    @Test // TC-TM-01
    void test_FirstPlacementMovesToNextPlayer() {
        // Arrange: Start a game with 3 players
        // System state: placementsCount = 0, currentPlayerIndex = 0
        TurnManager tm = new TurnManager(3);

        // Act: One placement is completed
        tm.nextPlacement();

        // Assert: Expected output is index 1
        assertEquals(1, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-03
    void test_SnakeTurnReversesAtEnd() {
        // Arrange: Set up a 3-player game
        TurnManager tm = new TurnManager(3);

        // Act: Move through the first 3 placements
        tm.nextPlacement(); // P1 done
        tm.nextPlacement(); // P2 done
        tm.nextPlacement(); // P3 done

        // Assert: The BVA states index n-1 should repeat
        assertEquals(2, tm.getCurrentPlayerIndex());
    }


}
