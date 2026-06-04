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
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;
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

    public int getId() {
        return id;
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
        return this.inventory;
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
        if (this.victoryPoints - points < 0) {
            this.victoryPoints = 0;
        } else {
            this.victoryPoints -= points;
        }
    }

    public List<DevCard> getDevCardHand() { return this.devHand; }

    public void setDevCardHand(DevCard devCard) {
        this.devHand.add(devCard);

        if (devCard.getType() == DevCardType.VICTORY_POINT) {
            this.addVictoryPoints(1); // Scoreboard updates immediately upon drawing!
        }
    }


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
