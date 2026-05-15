package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnManagerTest {

    @Test // TC-TM-01
    void test_FirstPlacementMovesToNextPlayer() {
        TurnManager tm = new TurnManager(3);

        tm.nextPlacement();

        assertEquals(1, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-02
    void test_PivotAtEndOfFirstRound() {
        TurnManager tm = new TurnManager(3);
        tm.nextPlacement();
        tm.nextPlacement();

        tm.nextPlacement();

        assertEquals(2, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-03
    void test_SnakeTurnReversesAtEnd() {
        TurnManager tm = new TurnManager(3);

        tm.nextPlacement();
        tm.nextPlacement();
        tm.nextPlacement();
        tm.nextPlacement();

        assertEquals(1, tm.getCurrentPlayerIndex());
    }

    @Test // TC-TM-04
    void test_SetupEndsAtPlayerZero() {
        TurnManager tm = new TurnManager(3);
        for(int i = 0; i < 5; i++) {
            tm.nextPlacement();
        }

        assertEquals(0, tm.getCurrentPlayerIndex());
    }



}
