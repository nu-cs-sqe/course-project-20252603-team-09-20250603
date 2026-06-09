package domain;

import java.util.HashMap;
import java.util.Map;

public class TradeManager {
    public void tradeWithBank(Player player, ResourceType giveResource, ResourceType receiveResource) {
        if (giveResource == receiveResource) {
            throw new IllegalActionException("Cannot trade a resource for itself.");
        }

        if (giveResource == null || receiveResource == null) {
            throw new IllegalArgumentException("Cannot trade a resource for null.");
        }

        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(giveResource, 4);

        if (!player.hasResources(cost)) {
            throw new IllegalActionException("Player does not have enough resources to trade with the bank.");
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
        if (offeringPlayer == receivingPlayer) {
            throw new IllegalActionException("Cannot trade with yourself.");
        }

        if (!offeringPlayer.hasResources(offeredResources)
                || !receivingPlayer.hasResources(requestedResources)) {
            throw new IllegalActionException("One or both players do not have the required resources.");
        }

        offeringPlayer.useResources(offeredResources);
        receivingPlayer.useResources(requestedResources);
        offeringPlayer.addResources(requestedResources);
        receivingPlayer.addResources(offeredResources);
    }
}
