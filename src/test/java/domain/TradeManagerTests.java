package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TradeManagerTests {
    @Test
    void localizedDomainException_NullMessageArgs_ReturnsEmptyArgsArray() {
        IllegalActionException exception = new IllegalActionException(
                DomainErrorKey.TRADE_BANK_INSUFFICIENT_RESOURCES,
                (Object[]) null
        );

        assertEquals(DomainErrorKey.TRADE_BANK_INSUFFICIENT_RESOURCES, exception.getErrorKey());
        assertEquals("error.trade.bank.insufficientResources", exception.getMessageKey());
        assertArrayEquals(new Object[0], exception.getMessageArgs());
    }

    @Test
    void localizedDomainException_MessageAccessors_ReturnDefensiveCopiesAndExpectedValues() {
        Object[] args = {"wood", 4};
        IllegalActionException exception = new IllegalActionException(
                DomainErrorKey.TRADE_BANK_SAME_RESOURCE,
                args
        );

        args[0] = "brick";
        Object[] messageArgs = exception.getMessageArgs();
        messageArgs[1] = 99;

        assertEquals(DomainErrorKey.TRADE_BANK_SAME_RESOURCE, exception.getErrorKey());
        assertEquals("error.trade.bank.sameResource", exception.getMessageKey());
        assertArrayEquals(new Object[]{"wood", 4}, exception.getMessageArgs());
        assertNotSame(args, exception.getMessageArgs());
        assertNotSame(messageArgs, exception.getMessageArgs());
    }

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

    @Test
    void tradeWithBank_NullResource_ThrowsIllegalArgumentException() {
        Player mockPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        EasyMock.replay(mockPlayer);

        assertThrows(IllegalArgumentException.class,
                () -> tradeManager.tradeWithBank(mockPlayer, null, ResourceType.BRICK));
        assertThrows(IllegalArgumentException.class,
                () -> tradeManager.tradeWithBank(mockPlayer, ResourceType.WOOD, null));

        EasyMock.verify(mockPlayer);
    }

    @Test
    void tradeWithPlayer_TwoPlayersTradeOneForOne_WithExactResources() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.expect(offeringPlayer.hasResources(offeredResources)).andReturn(true);
        EasyMock.expect(receivingPlayer.hasResources(requestedResources)).andReturn(true);

        offeringPlayer.useResources(offeredResources);
        EasyMock.expectLastCall().once();
        receivingPlayer.useResources(requestedResources);
        EasyMock.expectLastCall().once();
        offeringPlayer.addResources(requestedResources);
        EasyMock.expectLastCall().once();
        receivingPlayer.addResources(offeredResources);
        EasyMock.expectLastCall().once();

        EasyMock.replay(offeringPlayer, receivingPlayer);

        tradeManager.tradeWithPlayer(
                offeringPlayer,
                receivingPlayer,
                offeredResources,
                requestedResources
        );

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_TwoPlayersTradeMultiResourceBundles_WithSurplusResources() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 2);
        offeredResources.put(ResourceType.SHEEP, 1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);
        requestedResources.put(ResourceType.ORE, 2);

        EasyMock.expect(offeringPlayer.hasResources(offeredResources)).andReturn(true);
        EasyMock.expect(receivingPlayer.hasResources(requestedResources)).andReturn(true);

        offeringPlayer.useResources(offeredResources);
        EasyMock.expectLastCall().once();
        receivingPlayer.useResources(requestedResources);
        EasyMock.expectLastCall().once();
        offeringPlayer.addResources(requestedResources);
        EasyMock.expectLastCall().once();
        receivingPlayer.addResources(offeredResources);
        EasyMock.expectLastCall().once();

        EasyMock.replay(offeringPlayer, receivingPlayer);

        tradeManager.tradeWithPlayer(
                offeringPlayer,
                receivingPlayer,
                offeredResources,
                requestedResources
        );

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_OfferingPlayerShortByOneResource_ThrowsIllegalActionException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 2);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.expect(offeringPlayer.hasResources(offeredResources)).andReturn(false);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_ReceivingPlayerShortByOneResource_ThrowsIllegalActionException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 2);

        EasyMock.expect(offeringPlayer.hasResources(offeredResources)).andReturn(true);
        EasyMock.expect(receivingPlayer.hasResources(requestedResources)).andReturn(false);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }


    @Test
    void tradeWithPlayer_SamePlayerForBothSides_ThrowsIllegalActionException() {
        Player player = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.replay(player);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        player,
                        player,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(player);
    }

    @Test
    void tradeWithPlayer_OfferedResourcesEmpty_ThrowsIllegalActionException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_RequestedResourcesEmpty_ThrowsIllegalActionException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_ZeroQuantityInTradeMap_ThrowsIllegalArgumentException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 0);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalArgumentException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_NegativeQuantityInTradeMap_ThrowsIllegalArgumentException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, -1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalArgumentException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_NullOfferedResources_ThrowsIllegalActionException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 1);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        null,
                        requestedResources
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_NullRequestedResources_ThrowsIllegalActionException() {
        Player offeringPlayer = EasyMock.createMock(Player.class);
        Player receivingPlayer = EasyMock.createMock(Player.class);
        TradeManager tradeManager = new TradeManager();

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 1);

        EasyMock.replay(offeringPlayer, receivingPlayer);

        assertThrows(IllegalActionException.class,
                () -> tradeManager.tradeWithPlayer(
                        offeringPlayer,
                        receivingPlayer,
                        offeredResources,
                        null
                ));

        EasyMock.verify(offeringPlayer, receivingPlayer);
    }

    @Test
    void tradeWithPlayer_ZeroQuantityInRequestedResources_ThrowsIllegalArgumentException() {
        TradeManager tradeManager = new TradeManager();

        Player offeringPlayer = new Player(0, "Bob", PlayerColor.RED);
        Player receivingPlayer = new Player(1, "Alice", PlayerColor.BLUE);

        Map<ResourceType, Integer> offeredResources = new HashMap<>();
        offeredResources.put(ResourceType.WOOD, 1);

        Map<ResourceType, Integer> requestedResources = new HashMap<>();
        requestedResources.put(ResourceType.BRICK, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            tradeManager.tradeWithPlayer(
                    offeringPlayer,
                    receivingPlayer,
                    offeredResources,
                    requestedResources
            );
        });
    }
}
