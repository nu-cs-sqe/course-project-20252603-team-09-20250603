package domain;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private final int id;
    private final String name;
    private final PlayerColor color;
    private final Map<String, Integer> inventory;
    private final Map<ResourceType, Integer> resourceHand;
    private int victoryPoints;

    public Player(int id, String name, PlayerColor color){
        this.id = id;
        this.name = name;
        this.color = color;
        this.inventory = new HashMap<>();
        this.inventory.put("roads", 15);
        this.inventory.put("settlements", 5);
        this.inventory.put("cities", 4);
        this.victoryPoints = 0;
        this.resourceHand = new HashMap<>();
    }

    public String getName(){
        return name;
    }

    public PlayerColor getColor(){
        return color;
    }

    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory);
    }

    public void useInventoryItem(String item) {
        int currentAmount = inventory.getOrDefault(item, 0);

        if (currentAmount <= 0) {
            throw new IllegalStateException("No " + item + " remaining.");
        }

        inventory.put(item, currentAmount - 1);
    }


    public int getVictoryPoints(){
        return victoryPoints;
    }

    public void addVictoryPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to add cannot be negative.");
        }
        this.victoryPoints += points;
    }

    public void addResources(Map<ResourceType, Integer> resources) {
        if (resources == null) {
            throw new IllegalArgumentException("resources cannot be null");
        }

        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            ResourceType resource = entry.getKey();
            int amount = entry.getValue();

            if (resource != ResourceType.DESERT) {
            resourceHand.put(
                    resource,
                    resourceHand.getOrDefault(resource, 0) + amount
                );
            }
        }
    }

    public Map<ResourceType, Integer> getResources() {
        return new HashMap<>(resourceHand);
    }

    public void useResources(Map<ResourceType, Integer> cost) {
        if (!hasResources(cost)) {
            throw new IllegalStateException("Player does not have enough resources.");
        }

        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            ResourceType resource = entry.getKey();
            int amount = entry.getValue();

            resourceHand.put(resource, resourceHand.getOrDefault(resource, 0) - amount);
        }
    }

    public boolean hasResources(Map<ResourceType, Integer> cost) {
        if (cost == null) {
            throw new IllegalArgumentException("cost cannot be null");
        }

        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            ResourceType resource = entry.getKey();
            int requiredAmount = entry.getValue();

            if (resourceHand.getOrDefault(resource, 0) < requiredAmount) {
                return false;
            }
        }

        return true;
    }


}
