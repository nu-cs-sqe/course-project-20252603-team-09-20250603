package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiceTests {
    @Test
    public void rollAndGetDieSum_Roll1And1_Return2() {
        Random mockRandom = EasyMock.createMock(Random.class);

        EasyMock.expect(mockRandom.nextInt(6)).andReturn(0);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(0);

        EasyMock.replay(mockRandom);

        Dice dice = new Dice(mockRandom);
        dice.roll();

        assertEquals(1, dice.getDie1());
        assertEquals(1, dice.getDie2());
        assertEquals(2, dice.getDieSum());

        EasyMock.verify(mockRandom);
    }
}
