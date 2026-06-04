package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiceTests {
    @ParameterizedTest
    @CsvSource({
            "0, 0, 1, 1, 2",
            "0, 1, 1, 2, 3",
            "2, 3, 3, 4, 7",
            "4, 5, 5, 6, 11",
            "5, 5, 6, 6, 12"
    })

    public void rollAndGetDieSum_SetDie1and2_ReturnExpectedSum(
            int randomDie1,
            int randomDie2,
            int expectedDie1,
            int expectedDie2,
            int expectedSum
    ) {
        Random mockRandom = EasyMock.createMock(Random.class);

        EasyMock.expect(mockRandom.nextInt(6)).andReturn(randomDie1);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(randomDie2);

        EasyMock.replay(mockRandom);

        Dice dice = new Dice(mockRandom);
        dice.roll();

        assertEquals(expectedDie1, dice.getDie1());
        assertEquals(expectedDie2, dice.getDie2());
        assertEquals(expectedSum, dice.getDieSum());

        EasyMock.verify(mockRandom);
    }
}
