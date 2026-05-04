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

    @Test // TC-TM-02
    void test_PivotAtEndOfFirstRound() {
        // State: 3 players, 2 placements already done (P1 and P2 finished)
        TurnManager tm = new TurnManager(3);
        tm.nextPlacement(); // P1 -> P2
        tm.nextPlacement(); // P2 -> P3

        // Act: P3 completes their first house (the 3rd placement)
        tm.nextPlacement();

        // Assert: Index should stay 2 so P3 goes again immediately
        assertEquals(2, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-03
    void test_SnakeTurnReversesAtEnd() {
        // Arrange: Set up a 3-player game
        TurnManager tm = new TurnManager(3);

        // Act: Move through the first 4 placements
        tm.nextPlacement(); // P1 done
        tm.nextPlacement(); // P2 done
        tm.nextPlacement(); // P3 done
        tm.nextPlacement(); // P4 done

        // Assert: The BVA states index n-1 should repeat
        assertEquals(1, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-04
    void test_SetupEndsAtPlayerZero() {
        TurnManager tm = new TurnManager(3);
        // Move through 5 placements (0, 1, 2, 2, 1)
        for(int i = 0; i < 5; i++) {
            tm.nextPlacement();
        }

        // Act: P1 finishes their 2nd house (The 5th house total)
        // Assert: The next player to place (The 6th house) is P0
        assertEquals(0, tm.getCurrentPlayerIndex());
    }



}
