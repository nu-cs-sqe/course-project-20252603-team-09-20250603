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
}
