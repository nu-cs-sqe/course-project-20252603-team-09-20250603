package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTests {
    @Test
    void getName_NoSpaces_ReturnsCorrectly(){
        Player player = new Player(0,"Bob", PlayerColor.RED);

        assertEquals("Bob", player.getName());
    }

    @Test
    void getName_WithSpaces_ReturnsCorrectly(){
        Player player = new Player(0,"Jane Doe", PlayerColor.RED);

        assertEquals("Jane Doe", player.getName());
    }

    @Test
    void getColor_Red_ReturnsCorrectly(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertEquals(PlayerColor.RED, player.getColor());
    }

    @Test
    void getColor_BLUE_ReturnsCorrectly(){
        Player player = new Player(1, "Jane Doe", PlayerColor.BLUE);

        assertEquals(PlayerColor.BLUE, player.getColor());
    }

    @Test
    void getStartingInventory_ReturnsCorrectly(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertEquals(15, player.getInventory().get("roads"));
        assertEquals(5, player.getInventory().get("settlements"));
        assertEquals(4, player.getInventory().get("cities"));
    }

    @Test
    void getStartingNewPlayerVictoryPoints_ReturnsZero(){
        Player player = new Player(0, "Bob", PlayerColor.RED);

        assertEquals(0, player.getVictoryPoints());
    }

}
