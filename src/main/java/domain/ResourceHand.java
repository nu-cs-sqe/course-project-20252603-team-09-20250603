package domain;

import java.util.HashMap;
import java.util.Map;

public class ResourceHand {
    private final Map<ResourceType, Integer> hand;

    public ResourceHand() {
        this.hand = new HashMap<>();
        for (ResourceType type : ResourceType.values()) {
            this.hand.put(type, 0);
        }
    }

    public void setResourceCount(ResourceType type, int count) {
        if (type == null) {return;}
        if (count < 0) {
            throw new IllegalArgumentException("Resource count cannot be below zero.");
        }
        this.hand.put(type, count);
    }

    public int getResourceCount(ResourceType type) {
        if (type == null) {return 0;}
        return this.hand.getOrDefault(type, 0);
    }

    public void addResource(ResourceType type, int amount) {
        if (type == null || amount <= 0) {return;}
        int current = getResourceCount(type);
        this.hand.put(type, current + amount);
    }
}
