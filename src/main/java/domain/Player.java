package domain;

import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private final int id;
    private final String name;
    private final PlayerColor color;
    private final Map<String, Integer> inventory;
    private final Map<ResourceType, Integer> resourceHand;
    private int victoryPoints;
    private final List<DevCard> devHand;
    private boolean hasLargestArmy;
    private boolean hasLongestRoad;
    private int playedKnightCount = 0;
    private boolean hasPlayedDevCardThisTurn = false;
    private final Random random;

    public Player(int id, String name, PlayerColor color){
        this.id = id;
        this.name = name;
        this.color = color;
        this.inventory = new HashMap<>();
        this.inventory.put("roads", 15);
        this.inventory.put("settlements", 5);
        this.inventory.put("cities", 4);
        this.victoryPoints = 0;
        this.devHand = new ArrayList<>();
        this.resourceHand = new HashMap<>();
        this.random = new Random();
    }

    public int getId() {
        return id;
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

    public void setHasLargestArmy(boolean hasLargestArmy) {
        if (this.hasLargestArmy != hasLargestArmy) {
            this.hasLargestArmy = hasLargestArmy;
            if (hasLargestArmy) {
                this.addVictoryPoints(2);
            } else {
                this.removeVictoryPoints(2);
            }
        }
    }

    public boolean isHasLargestArmy() {
        return this.hasLargestArmy;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public boolean getHasLongestRoad() {
        return this.hasLongestRoad;
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

    public void removeVictoryPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points to remove cannot be negative.");
        }
        else {
            this.victoryPoints = Math.max(0, this.victoryPoints - points);
        }
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

    public ResourceType removeRandomCard() {
        List<ResourceType> cards = new ArrayList<>();

        for (Map.Entry<ResourceType, Integer> entry : resourceHand.entrySet()) {
            ResourceType resource = entry.getKey();
            int amount = entry.getValue();

            if (resource != ResourceType.DESERT) {
                for (int i = 0; i < amount; i++) {
                    cards.add(resource);
                }
            }
        }

        if (cards.isEmpty()) {
            throw new IllegalStateException("Player has no resource cards.");
        }

        int randomIndex = random.nextInt(cards.size());

        ResourceType removedResource = cards.get(randomIndex);

        resourceHand.put(removedResource, resourceHand.get(removedResource) - 1);

        if (resourceHand.get(removedResource) == 0) {
            resourceHand.remove(removedResource);
        }

        return removedResource;
    }

    public boolean hasMoreThanSevenResources() {
        int totalResources = 0;

        for (int amount : resourceHand.values()) {
            totalResources += amount;
        }

        return totalResources > 7;
    }
    public List<DevCard> getDevCardHand() { return Collections.unmodifiableList(new ArrayList<>(this.devHand)); }

    public void removeDevCard(DevCard devCard) {
        this.devHand.remove(devCard);
    }

    public void setDevCardHand(DevCard devCard) {
        this.devHand.add(devCard);

        if (devCard.getType() == DevCardType.VICTORY_POINT) {
            this.addVictoryPoints(1); 
        }
    }


    public void manageDevCardActivation(int activePlayerId) {
        if (this.id == activePlayerId) {
            this.hasPlayedDevCardThisTurn = false;
            for (DevCard card : devHand) {
                card.activateCard();
            }
        }
    }

    public boolean getHasPlayedDevCardThisTurn() {
        return this.hasPlayedDevCardThisTurn;
    }

    public void setHasPlayedDevCardThisTurn(boolean hasPlayed) {
        this.hasPlayedDevCardThisTurn = hasPlayed;
    }

    public void incrementPlayedKnightCount() {
        this.playedKnightCount++;
    }

    public int getPlayedKnightCount() {
        return this.playedKnightCount;
    }

    public void deductRoads(int count) {
        int currentRoads = this.inventory.getOrDefault("roads", 0);
        if (currentRoads < count) {
            throw new IllegalStateException("Not enough road pieces remaining in inventory!");
        }
        this.inventory.put("roads", currentRoads - count);
    }


}
