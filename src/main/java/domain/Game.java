package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final Map<Integer, Player> players;
    private final DevCardDeck devCardDeck;
    private Board board;

    public Game() {
        this.players = new HashMap<>();
        this.devCardDeck = new DevCardDeck();
        this.board = new Board();
    }

    public DevCardDeck getDevCardDeck() {
        return this.devCardDeck;
    }

    public Board getBoard() {
        return this.board;
    }

    public Map<Integer, Player> getPlayers() {
        return this.players;
    }

    public void drawDevCard(int currentPlayerId) {
        Player player = players.get(currentPlayerId);

        DevCard randomCard = devCardDeck.drawCard();
        player.setDevCardHand(randomCard);

        System.out.println("Player " + currentPlayerId + " drew a " + randomCard.getType());
    }

    public void useDevCard(int currentPlayerId, DevCardType cardType, int targetHexId,
                           ResourceType choice1, ResourceType choice2, ResourceType targetType) {

        Player player = players.get(currentPlayerId);

        DevCard cardToPlay = player.getDevCardHand().stream()
                .filter(c -> c.getType() == cardType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player doesn't have this card type"));

        switch (cardType) {
            case KNIGHT:
                cardToPlay.doKnightAction(player, this.board, targetHexId);
                break;
            case ROAD_BUILDING:
                cardToPlay.doRoadBuildingAction(player, this.board);
                break;
            case YEAR_OF_PLENTY:
                cardToPlay.doYearOfPlentyAction(player, this.board, choice1, choice2);
                break;
            case MONOPOLY:
                List<Player> allPlayersList = new ArrayList<>(this.players.values());
                cardToPlay.doMonopolyAction(player, allPlayersList, this.board, targetType);
                break;
            default:
                throw new UnsupportedOperationException("This development card type cannot be manually played.");
        }

        player.getDevCardHand().remove(cardToPlay);
    }

}