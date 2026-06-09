package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevCardDeck {
    private final List<DevCard> deck;

    public DevCardDeck() {
        this.deck = new ArrayList<>();
        initializeDeck();
        shuffleDeck();
    }

    private void initializeDeck() {
        for (int i = 0; i < 14; i++) {
            deck.add(new DevCard(DevCardType.KNIGHT));
        }
        for (int i = 0; i < 5; i++) {
            deck.add(new DevCard(DevCardType.VICTORY_POINT));
        }
        for (int i = 0; i < 2; i++) {
            deck.add(new DevCard(DevCardType.ROAD_BUILDING));
        }
        for (int i = 0; i < 2; i++) {
            deck.add(new DevCard(DevCardType.YEAR_OF_PLENTY));
        }
        for (int i = 0; i < 2; i++) {
            deck.add(new DevCard(DevCardType.MONOPOLY));
        }
    }

    public void setNextCardType(DevCardType type) {
        DevCard riggedCard = new DevCard(type);
        this.deck.add(0, riggedCard);
    }

    public void shuffleDeck() {
        Collections.shuffle(this.deck);
    }

    public DevCard drawCard() {
        if (deck.isEmpty()) {
            throw new IllegalStateException("No development cards left in the deck!");
        }
        return deck.remove(0);
    }

}