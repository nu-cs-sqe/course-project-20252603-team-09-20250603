package domain;

public class Hex {
    private int id;
    // TODO: Replace String with TerrainType enum when that dependency exists.
    private String terrainType;
    private boolean hasRobber;

    public Hex(int id)
    {
        this.id = id;
        this.terrainType = null;
        this.hasRobber = false;
    }
    public boolean getHasRobber() {
        return this.hasRobber;
    }

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public String getTerrainType() {
        return this.terrainType;
    }

    public void setTerrainType(String terrainType) {
        this.terrainType = terrainType;
    }
}
