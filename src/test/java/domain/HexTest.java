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

}