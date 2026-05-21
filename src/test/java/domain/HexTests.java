package domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class HexTests {
    private Hex hex;

    @BeforeEach
    public void setUp() {
        hex = new Hex(1);
    }

    @Test
    public void getHasRobber_NewHex_ReturnsFalse() {
        Assertions.assertFalse(hex.getHasRobber(), "A new hex should not start with the robber.");
    }

    @Test
    public void setHasRobber_GivenTrue_PlacesRobber() {
        hex.setHasRobber(true);

        Assertions.assertTrue(hex.getHasRobber(), "setHasRobber(true) should place the robber on the hex.");
    }

    @Test
    public void setHasRobber_GivenFalse_RemovesRobber() {
        hex.setHasRobber(true);

        hex.setHasRobber(false);

        Assertions.assertFalse(hex.getHasRobber(), "setHasRobber(false) should remove the robber from the hex.");
    }

    @Test
    public void getResourceType_NewHex_ReturnsNull() {
        Assertions.assertNull(hex.getResourceType(), "A new hex should not have resource until the board generator assigns it.");
    }

    @Test
    public void setResourceType_GivenForest_StoresForest() {
        hex.setResourceType(ResourceType.WOOD);

        Assertions.assertEquals(ResourceType.WOOD, hex.getResourceType(), "setResourceType should store the assigned resource.");
    }

    @Test
    public void getTokenNumber_NewHex_ReturnsZero() {
        Assertions.assertEquals(0, hex.getTokenNumber(), "A new hex should not have a number token until one is assigned.");
    }

    @Test
    public void setTokenNumber_GivenMinimumValidToken_StoresTokenNumber() {
        hex.setTokenNumber(2);

        Assertions.assertEquals(2, hex.getTokenNumber(), "2 is the lowest valid Catan number token.");
    }

    @Test
    public void setTokenNumber_GivenMaximumValidToken_StoresTokenNumber() {
        hex.setTokenNumber(12);

        Assertions.assertEquals(12, hex.getTokenNumber(), "12 is the highest valid Catan number token.");
    }

    @Test
    public void setTokenNumber_GivenValueBelowMinimum_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(1),
                "Token numbers below 2 should be rejected.");
    }

    @Test
    public void setTokenNumber_GivenSeven_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(7),
                "Token number 7 should be rejected because the game Catan does not have a 7 token.");
    }

    @Test
    public void setTokenNumber_GivenValueAboveMaximum_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> hex.setTokenNumber(13),
                "Token numbers above 12 should be rejected.");
    }
}
