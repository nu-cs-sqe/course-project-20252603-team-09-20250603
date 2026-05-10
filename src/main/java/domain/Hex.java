package domain;

public class Hex {
    private int id;
    private boolean hasRobber;

    public Hex(int id)
    {
        this.id = id;
        this.hasRobber = false;
    }
    public boolean getHasRobber() {
        return this.hasRobber;
    }
}

