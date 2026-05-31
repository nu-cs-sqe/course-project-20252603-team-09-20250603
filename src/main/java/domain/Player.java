package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private final int id;
    private final String name;
    private final PlayerColor color;
    private final Map<String, Integer> inventory;
    private int victoryPoints;
    private final List<DevCard> devHand;
    private int playedKnightCount = 0;
    private ResourceHand resourceHand;

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
        this.resourceHand = new ResourceHand();
    }

    public ResourceHand getResourceHand() {
        return this.resourceHand;
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

    public int getVictoryPoints(){
        return victoryPoints;
    }

    public List<DevCard> getDevCardHand() { return this.devHand; }

    public void setDevCardHand(DevCard devCard) { this.devHand.add(devCard); }

    public void manageDevCardActivation(int activePlayerId) {
        if (this.id == activePlayerId) {
            for (DevCard card : devHand) {
                card.activateCard();
            }
        }
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
