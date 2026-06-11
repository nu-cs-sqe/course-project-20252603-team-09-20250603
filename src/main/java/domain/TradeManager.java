package domain;

import java.util.HashMap;
import java.util.Map;

public class TradeManager {
    public void tradeWithBank(Player player, ResourceType giveResource, ResourceType receiveResource) {
        if (giveResource == receiveResource) {
            throw new IllegalActionException(DomainErrorKey.TRADE_BANK_SAME_RESOURCE);
        }

        if (giveResource == null || receiveResource == null) {
            throw new IllegalArgumentException("Cannot trade a resource for null.");
        }

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(giveResource, 4);

        if (!player.hasResources(cost)) {
            throw new IllegalActionException(DomainErrorKey.TRADE_BANK_INSUFFICIENT_RESOURCES);
        }

        player.useResources(cost);

        Map<ResourceType, Integer> gain = new HashMap<>();
        gain.put(receiveResource, 1);
        player.addResources(gain);
    }

    public void tradeWithPlayer(
            Player offeringPlayer,
            Player receivingPlayer,
            Map<ResourceType, Integer> offeredResources,
            Map<ResourceType, Integer> requestedResources
    ) {
        if (offeredResources == null || offeredResources.isEmpty()) {
            throw new IllegalActionException(DomainErrorKey.TRADE_PLAYER_NOTHING_OFFERED);
        }

        if (requestedResources == null || requestedResources.isEmpty()) {
            throw new IllegalActionException(DomainErrorKey.TRADE_PLAYER_NOTHING_REQUESTED);
        }

        for (Integer amount : offeredResources.values()) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Offered resource quantities must be positive.");
            }
        }

        for (Integer amount : requestedResources.values()) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Requested resource quantities must be positive.");
            }
        }

        if (offeringPlayer == receivingPlayer) {
            throw new IllegalActionException(DomainErrorKey.TRADE_PLAYER_SELF);
        }

        if (!offeringPlayer.hasResources(offeredResources)
                || !receivingPlayer.hasResources(requestedResources)) {
            throw new IllegalActionException(DomainErrorKey.TRADE_PLAYER_INSUFFICIENT_RESOURCES);
        }

        offeringPlayer.useResources(offeredResources);
        receivingPlayer.useResources(requestedResources);
        offeringPlayer.addResources(requestedResources);
        receivingPlayer.addResources(offeredResources);
    }
}
