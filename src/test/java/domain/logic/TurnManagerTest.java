package domain.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TurnManagerTest {
    @Test
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
