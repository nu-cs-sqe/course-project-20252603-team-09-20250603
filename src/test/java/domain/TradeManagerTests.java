package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TradeManagerTests {
    @Test
    void tradeWithBank_PlayerHasExactlyFourWood_TradesForOneBrick() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WOOD, 4);

        Map<ResourceType, Integer> gain = new HashMap<>();
        gain.put(ResourceType.BRICK, 1);

        EasyMock.expect(mockPlayer.hasResources(cost)).andReturn(true);
        mockPlayer.useResources(cost);
        EasyMock.expectLastCall().once();
        mockPlayer.addResources(gain);
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockPlayer);

        tradeManager.tradeWithBank(mockPlayer, ResourceType.WOOD, ResourceType.BRICK);

        EasyMock.verify(mockPlayer);
    }

    @Test
    void tradeWithBank_PlayerHasFiveSheep_TradesForOneOre() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.SHEEP, 4);

        Map<ResourceType, Integer> gain = new HashMap<>();
        gain.put(ResourceType.ORE, 1);

        EasyMock.expect(mockPlayer.hasResources(cost)).andReturn(true);
        mockPlayer.useResources(cost);
        EasyMock.expectLastCall().once();
        mockPlayer.addResources(gain);
        EasyMock.expectLastCall().once();

        EasyMock.replay(mockPlayer);

        tradeManager.tradeWithBank(mockPlayer, ResourceType.SHEEP, ResourceType.ORE);

        EasyMock.verify(mockPlayer);
    }

    @Test
    void tradeWithBank_PlayerHasThreeWheat_ThrowsIllegalActionException() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.WHEAT, 4);

        EasyMock.expect(mockPlayer.hasResources(cost)).andReturn(false);

        EasyMock.replay(mockPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithBank(mockPlayer, ResourceType.WHEAT, ResourceType.WOOD));

        EasyMock.verify(mockPlayer);
    }

    @Test
    void tradeWithBank_SameGiveAndReceiveResource_ThrowsIllegalActionException() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        EasyMock.replay(mockPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithBank(mockPlayer, ResourceType.WOOD, ResourceType.WOOD));

        EasyMock.verify(mockPlayer);
    }
}
