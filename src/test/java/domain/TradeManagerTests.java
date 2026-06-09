package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
}
