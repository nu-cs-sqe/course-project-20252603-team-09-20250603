package domain;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private final int id;
    private final String name;
    private final PlayerColor color;
    private final Map<String, Integer> inventory;
    private int victoryPoints;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;

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
}
