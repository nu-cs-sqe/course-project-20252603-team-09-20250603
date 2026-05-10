package domain;

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

    @Test
    public void distributeResources_NoRobberAndOneOccupiedNode_PlayerCollectsResource() {
        Hex.ResourceCollector player = mock(Hex.ResourceCollector.class);
        Hex.ResourceNode occupiedNode = new TestNode(true, player, 1);
        hex = new Hex(1, Arrays.asList(occupiedNode));
        hex.setTerrainType("Forest");
        player.collectResources("Forest", 1);
        expectLastCall().once();
        replay(player);

        hex.distributeResources();

        verify(player);
    }

    @Test
    public void distributeResources_NoRobberAndCityNode_PlayerCollectsTwoResources() {
        Hex.ResourceCollector player = mock(Hex.ResourceCollector.class);
        Hex.ResourceNode cityNode = new TestNode(true, player, 2);
        hex = new Hex(1, Arrays.asList(cityNode));
        hex.setTerrainType("Mountains");
        player.collectResources("Mountains", 2);
        expectLastCall().once();
        replay(player);

        hex.distributeResources();

        verify(player);
    }

    @Test
    public void distributeResources_RobberPresent_PlayerDoesNotCollectResource() {
        Hex.ResourceCollector player = mock(Hex.ResourceCollector.class);
        Hex.ResourceNode occupiedNode = new TestNode(true, player, 1);
        hex = new Hex(1, Arrays.asList(occupiedNode));
        hex.setTerrainType("Forest");
        hex.setHasRobber(true);
        replay(player);

        hex.distributeResources();

        verify(player);
    }

    @Test
    public void distributeResources_AllNodesEmpty_PlayerDoesNotCollectResource() {
        Hex.ResourceCollector player = mock(Hex.ResourceCollector.class);
        Hex.ResourceNode emptyNode = new TestNode(false, player, 1);
        hex = new Hex(1, Arrays.asList(emptyNode));
        hex.setTerrainType("Forest");
        replay(player);

        hex.distributeResources();

        verify(player);
    }

    @Test
    public void distributeResources_AllNodesOccupied_AllPlayersCollectResources() {
        Hex.ResourceCollector firstPlayer = mock(Hex.ResourceCollector.class);
        Hex.ResourceCollector secondPlayer = mock(Hex.ResourceCollector.class);
        hex = new Hex(1, Arrays.asList(
                new TestNode(true, firstPlayer, 1),
                new TestNode(true, firstPlayer, 1),
                new TestNode(true, secondPlayer, 1),
                new TestNode(true, secondPlayer, 1),
                new TestNode(true, secondPlayer, 1),
                new TestNode(true, secondPlayer, 1)));
        hex.setTerrainType("Fields");
        firstPlayer.collectResources("Fields", 1);
        expectLastCall().times(2);
        secondPlayer.collectResources("Fields", 1);
        expectLastCall().times(4);
        replay(firstPlayer, secondPlayer);

        hex.distributeResources();

        verify(firstPlayer, secondPlayer);
    }

    @Test
    public void distributeResources_TerrainTypeMissing_PlayerDoesNotCollectResource() {
        Hex.ResourceCollector player = mock(Hex.ResourceCollector.class);
        Hex.ResourceNode occupiedNode = new TestNode(true, player, 1);
        hex = new Hex(1, Arrays.asList(occupiedNode));
        replay(player);

        hex.distributeResources();

        verify(player);
    }

    private static class TestNode implements Hex.ResourceNode {
        private boolean occupied;
        private Hex.ResourceCollector owner;
        private int productionAmount;

        TestNode(boolean occupied, Hex.ResourceCollector owner, int productionAmount) {
            this.occupied = occupied;
            this.owner = owner;
            this.productionAmount = productionAmount;
        }

        public boolean isOccupied() {
            return this.occupied;
        }

        public Hex.ResourceCollector getOwner() {
            return this.owner;
        }

        public int getProductionAmount() {
            return this.productionAmount;
        }
    }

}
