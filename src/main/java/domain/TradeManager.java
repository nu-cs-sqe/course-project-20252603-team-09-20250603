package domain;

import java.util.HashMap;
import java.util.Map;

public class TradeManager {
    public void tradeWithBank(Player player, ResourceType giveResource, ResourceType receiveResource) {
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
}
