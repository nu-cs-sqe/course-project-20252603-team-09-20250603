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

}
