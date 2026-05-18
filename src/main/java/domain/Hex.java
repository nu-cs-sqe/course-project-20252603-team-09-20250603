package domain;

import java.util.ArrayList;
import java.util.List;

public class Hex {

    private final int id;

    public Hex(int id)
    {
        this(id, new ArrayList<>());
    }

    Hex(int id, List<Node> adjacentNodes)
    {
        this.id = id;
    }

    public int getId() { return this.id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Hex)) {
            return false;
        }

        Hex hex = (Hex) o;
        return id == hex.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}