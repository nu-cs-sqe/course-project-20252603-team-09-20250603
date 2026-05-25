package domain;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private final int id;
    private final String name;
    private final PlayerColor color;
    private final Map<String, Integer> inventory;
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
}
