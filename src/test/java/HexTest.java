import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HexTest {
    private Hex hex;

    @BeforeEach
    public void setUp() {
        hex = new Hex(1);
    }

    @Test
    public void getHasRobber_NewHex_ReturnsFalse() {
        assertFalse(hex.getHasRobber(), "A new hex should not start with the robber.");
    }

    @Test
    public void setHasRobber_GivenTrue_PlacesRobber() {
        hex.setHasRobber(true);

        assertTrue(hex.getHasRobber(), "setHasRobber(true) should place the robber on the hex.");
    }

    @Test
    public void setHasRobber_GivenFalse_RemovesRobber() {
        hex.setHasRobber(true);

        hex.setHasRobber(false);

        assertFalse(hex.getHasRobber(), "setHasRobber(false) should remove the robber from the hex.");
    }

    @Test
    public void getTerrainType_NewHex_ReturnsNull() {
        assertNull(hex.getTerrainType(), "A new hex should not have terrain until the board generator assigns it.");
    }

    @Test
    public void setTerrainType_GivenForest_StoresForest() {
        hex.setTerrainType("Forest");

        assertEquals("Forest", hex.getTerrainType(), "setTerrainType should store the assigned terrain.");
    }

    @Test
    public void getTokenNumber_NewHex_ReturnsZero() {
        assertEquals(0, hex.getTokenNumber(), "A new hex should not have a number token until one is assigned.");
    }

    @Test
    public void setTokenNumber_GivenMinimumValidToken_StoresTokenNumber() {
        hex.setTokenNumber(2);

        assertEquals(2, hex.getTokenNumber(), "2 is the lowest valid Catan number token.");
    }

    @Test
    public void setTokenNumber_GivenMaximumValidToken_StoresTokenNumber() {
        hex.setTokenNumber(12);

        assertEquals(12, hex.getTokenNumber(), "12 is the highest valid Catan number token.");
    }

    @Test
    public void setTokenNumber_GivenValueBelowMinimum_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(1),
                "Token numbers below 2 should be rejected.");
    }

    @Test
    public void setTokenNumber_GivenValueAboveMaximum_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(13),
                "Token numbers above 12 should be rejected.");
    }
}
