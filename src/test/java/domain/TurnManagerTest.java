package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TurnManagerTest {

    @Test // TC-TM-01
    void test_FirstPlacementMovesToNextPlayer() {
        TurnManager tm = new TurnManager(3);

        tm.nextPlayer();

        assertEquals(1, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-02
    void test_PivotAtEndOfFirstRound() {
        TurnManager tm = new TurnManager(3);
        tm.nextPlayer();
        tm.nextPlayer();

        tm.nextPlayer();

        assertEquals(3, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-03
    void test_SnakeTurnReversesAtEnd() {
        TurnManager tm = new TurnManager(3);

        tm.nextPlayer();
        tm.nextPlayer();
        tm.nextPlayer();
        tm.nextPlayer();

        assertEquals(3, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-04
    void test_SetupEndsAtPlayerOne() {
        TurnManager tm = new TurnManager(4);
        for(int i = 0; i < 8; i++) {
            tm.nextPlayer();
        }

        assertEquals(1, tm.getCurrentPlayerIndex());
        //assertTrue(tm.SetupStatus());
    }

    @Test // TC-TM-05
    void test_SetupStatusTrueAfterAllPlacements() {
        TurnManager tm = new TurnManager(4);
        for (int i = 0; i < 9; i++) {
            tm.nextPlayer();
        }
        assertTrue(tm.SetupStatus());
    }



}
