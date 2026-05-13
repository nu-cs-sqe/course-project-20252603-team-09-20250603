import java.util.ArrayList;
import java.util.List;

public class Hex {
    private static final int NO_TOKEN = 0;
    private static final int MIN_TOKEN_NUMBER = 2;
    private static final int MAX_TOKEN_NUMBER = 12;

    private int id;
    // TODO: Replace String with TerrainType enum when that dependency exists.
    private String terrainType;
    private int tokenNumber;
    private boolean hasRobber;
    private List<Node> adjacentNodes;

    public Hex(int id)
    {
        this(id, new ArrayList<>());
    }

    Hex(int id, List<Node> adjacentNodes)
    {
        this.id = id;
        this.terrainType = null;
        this.tokenNumber = NO_TOKEN;
        this.hasRobber = false;
        this.adjacentNodes = new ArrayList<>(adjacentNodes);
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

    public int getTokenNumber() {
        return this.tokenNumber;
    }

    public void setTokenNumber(int tokenNumber) {
        if (tokenNumber != NO_TOKEN && (tokenNumber < MIN_TOKEN_NUMBER || tokenNumber > MAX_TOKEN_NUMBER)) {
            throw new IllegalArgumentException("Token number must be 0 or between 2 and 12.");
        }

        this.tokenNumber = tokenNumber;
    }
}
