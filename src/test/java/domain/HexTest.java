package domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HexTest {
    private Hex hex;

    @BeforeEach
    public void setUp() {
        hex = new Hex(1);
    }

    @Test
    public void testInitialHasRobberIsFalse() {
        assertFalse(hex.getHasRobber(), "A new hex should not start with the robber.");
    }

    @Test
    public void testSetHasRobberCanPlaceRobber() {
        hex.setHasRobber(true);

        assertTrue(hex.getHasRobber(), "setHasRobber(true) should place the robber on the hex.");
    }

    @Test
    public void testSetHasRobberCanRemoveRobber() {
        hex.setHasRobber(true);

        hex.setHasRobber(false);

        assertFalse(hex.getHasRobber(), "setHasRobber(false) should remove the robber from the hex.");
    }

    @Test
    public void testInitialTerrainTypeIsNull() {
        assertNull(hex.getTerrainType(), "A new hex should not have terrain until the board generator assigns it.");
    }

    @Test
    public void testSetTerrainTypeStoresTerrain() {
        hex.setTerrainType("Forest");

        assertEquals("Forest", hex.getTerrainType(), "setTerrainType should store the assigned terrain.");
    }

    @Test
    public void testInitialTokenNumberIsZero() {
        assertEquals(0, hex.getTokenNumber(), "A new hex should not have a number token until one is assigned.");
    }

    @Test
    public void testSetTokenNumberStoresMinimumValidToken() {
        hex.setTokenNumber(2);

        assertEquals(2, hex.getTokenNumber(), "2 is the lowest valid Catan number token.");
    }

    @Test
    public void testSetTokenNumberStoresMaximumValidToken() {
        hex.setTokenNumber(12);

        assertEquals(12, hex.getTokenNumber(), "12 is the highest valid Catan number token.");
    }

    @Test
    public void testSetTokenNumberRejectsValueBelowMinimum() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(1),
                "Token numbers below 2 should be rejected.");
    }

    @Test
    public void testSetTokenNumberRejectsValueAboveMaximum() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(13),
                "Token numbers above 12 should be rejected.");
    }

}
