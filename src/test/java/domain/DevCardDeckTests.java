package domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevCardDeckTests {

    private static final int DECK_SIZE = 25;

    private Map<DevCardType, Integer> drawAll(DevCardDeck deck, int count) {
        Map<DevCardType, Integer> counts = new EnumMap<>(DevCardType.class);
        for (int i = 0; i < count; i++) {
            counts.merge(deck.drawCard().getType(), 1, Integer::sum);
        }
        return counts;
    }

    private List<DevCardType> initializationOrder() {
        List<DevCardType> order = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            order.add(DevCardType.KNIGHT);
        }
        for (int i = 0; i < 5; i++) {
            order.add(DevCardType.VICTORY_POINT);
        }
        for (int i = 0; i < 2; i++) {
            order.add(DevCardType.ROAD_BUILDING);
        }
        for (int i = 0; i < 2; i++) {
            order.add(DevCardType.YEAR_OF_PLENTY);
        }
        for (int i = 0; i < 2; i++) {
            order.add(DevCardType.MONOPOLY);
        }
        return order;
    }

    @Test // TC-DD-01
    void newDeck_DrawsExactlyTwentyFiveCardsBeforeRunningOut() {
        DevCardDeck deck = new DevCardDeck();

        for (int i = 0; i < DECK_SIZE; i++) {
            assertNotNull(deck.drawCard());
        }

        assertThrows(IllegalStateException.class, deck::drawCard);
    }

    @Test // TC-DD-02
    void newDeck_HasStandardCardComposition() {
        DevCardDeck deck = new DevCardDeck();

        Map<DevCardType, Integer> counts = drawAll(deck, DECK_SIZE);

        assertEquals(14, counts.getOrDefault(DevCardType.KNIGHT, 0));
        assertEquals(5, counts.getOrDefault(DevCardType.VICTORY_POINT, 0));
        assertEquals(2, counts.getOrDefault(DevCardType.ROAD_BUILDING, 0));
        assertEquals(2, counts.getOrDefault(DevCardType.YEAR_OF_PLENTY, 0));
        assertEquals(2, counts.getOrDefault(DevCardType.MONOPOLY, 0));
    }

    @Test // TC-DD-03
    void drawCard_OnEmptyDeck_ThrowsIllegalStateException() {
        DevCardDeck deck = new DevCardDeck();
        drawAll(deck, DECK_SIZE);

        assertThrows(IllegalStateException.class, deck::drawCard);
    }

    @Test // TC-DD-04
    void drawCard_ReturnsACard() {
        DevCardDeck deck = new DevCardDeck();

        assertNotNull(deck.drawCard());
    }

    @Test // TC-DD-05
    void setNextCardType_RigsTheVeryNextDraw() {
        DevCardDeck deck = new DevCardDeck();

        deck.setNextCardType(DevCardType.MONOPOLY);

        assertEquals(DevCardType.MONOPOLY, deck.drawCard().getType());
    }

    @Test // TC-DD-06
    void setNextCardType_AddsOneCard_AllowsAnExtraDraw() {
        DevCardDeck deck = new DevCardDeck();

        deck.setNextCardType(DevCardType.KNIGHT);

        // 25 standard cards + 1 rigged card = 26 draws before the deck is empty.
        for (int i = 0; i < DECK_SIZE + 1; i++) {
            assertNotNull(deck.drawCard());
        }
        assertThrows(IllegalStateException.class, deck::drawCard);
    }

    @Test // TC-DD-07
    void setNextCardType_MostRecentlySetIsDrawnFirst() {
        DevCardDeck deck = new DevCardDeck();

        deck.setNextCardType(DevCardType.KNIGHT);
        deck.setNextCardType(DevCardType.MONOPOLY);

        assertEquals(DevCardType.MONOPOLY, deck.drawCard().getType());
        assertEquals(DevCardType.KNIGHT, deck.drawCard().getType());
    }

    @Test // TC-DD-08
    void shuffleDeck_PreservesCardCountAndComposition() {
        DevCardDeck deck = new DevCardDeck();

        deck.shuffleDeck();

        Map<DevCardType, Integer> counts = drawAll(deck, DECK_SIZE);
        assertEquals(14, counts.getOrDefault(DevCardType.KNIGHT, 0));
        assertEquals(5, counts.getOrDefault(DevCardType.VICTORY_POINT, 0));
        assertEquals(2, counts.getOrDefault(DevCardType.ROAD_BUILDING, 0));
        assertEquals(2, counts.getOrDefault(DevCardType.YEAR_OF_PLENTY, 0));
        assertEquals(2, counts.getOrDefault(DevCardType.MONOPOLY, 0));
        assertThrows(IllegalStateException.class, deck::drawCard);
    }

    @Test // TC-DD-09
    void shuffleDeck_ReordersAwayFromInitializationOrder() {
        DevCardDeck deck = new DevCardDeck(new Random(0)); // seeded for a deterministic shuffle

        List<DevCardType> drawnOrder = new ArrayList<>();
        for (int i = 0; i < DECK_SIZE; i++) {
            drawnOrder.add(deck.drawCard().getType());
        }

        // A deck that was shuffled must not come out in the original build order.
        assertNotEquals(initializationOrder(), drawnOrder);
    }
}
