package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DevCardTests {

    @Test // TC-DC-01
    void test_TurnOfPurchase_ThrowsException() {
        DevCard card = new DevCard(DevCardType.KNIGHT);

        Player mockPlayer = EasyMock.createMock(Player.class);
        Board mockBoard = EasyMock.createMock(Board.class);

        assertFalse(card.getIsActive());

        EasyMock.replay(mockPlayer, mockBoard);

        int targetHexId = 5;
        assertThrows(IllegalActionException.class, () -> {
            card.doKnightAction(mockPlayer, mockBoard, targetHexId, null);
        });

        EasyMock.verify(mockPlayer, mockBoard);
    }

    @Test // TC-DC-02
    void test_EndOfTurnPhaseChange_RemainsInactive() {
        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);
        DevCard card = new DevCard(DevCardType.ROAD_BUILDING);
        player1.setDevCardHand(card);

        assertFalse(card.getIsActive());

        player1.manageDevCardActivation(2);
        assertFalse(card.getIsActive());

        player1.manageDevCardActivation(3);
        assertFalse(card.getIsActive());
    }

    @Test // TC-DC-03
    void test_StartOfNextTurn_ActivatesCard() {
        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);

        DevCard card = new DevCard(DevCardType.MONOPOLY);
        player1.setDevCardHand(card);

        assertFalse(card.getIsActive());

        player1.manageDevCardActivation(owningPlayerId);

        assertTrue(card.getIsActive());
    }

    @Test // TC-DC-KN
    void test_KnightCard_IncrementsKnightPoolAndMovesRobber() {
        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);

        DevCard card = new DevCard(DevCardType.KNIGHT);
        card.activateCard();

        Board mockBoard = EasyMock.createMock(Board.class);

        int targetHexId = 5;

        mockBoard.moveRobberAndSteal(player1, targetHexId, null);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(mockBoard);

        card.doKnightAction(player1, mockBoard, targetHexId, null);

        EasyMock.verify(mockBoard);
    }

    @Test // TC-DC-KN-STEAL
    void test_KnightCard_StealsOneCardFromAdjacentVictim() {
        Board board = new Board(new Random(0));
        Player thief = new Player(0, "John", PlayerColor.RED);
        Player victim = new Player(1, "Alice", PlayerColor.BLUE);

        // Victim owns a settlement on node 0, which borders hex 0.
        board.getNode(0).buildSettlement(victim);
        Map<ResourceType, Integer> hand = new HashMap<>();
        hand.put(ResourceType.WHEAT, 2);
        victim.addResources(hand);

        Game game = new Game(board, List.of(thief, victim), new Dice(new Random()), new TurnManager(1));

        DevCard knight = new DevCard(DevCardType.KNIGHT);
        thief.setDevCardHand(knight);
        thief.manageDevCardActivation(thief.getId()); // start of turn: activate the card

        game.useDevCard(thief.getId(), DevCardType.KNIGHT, 0, victim.getId(), null, null, null);

        assertEquals(1, victim.getResources().getOrDefault(ResourceType.WHEAT, 0));
        assertEquals(1, thief.getResources().getOrDefault(ResourceType.WHEAT, 0));
        assertTrue(board.getHex(0).getHasRobber());
        assertEquals(1, thief.getPlayedKnightCount());
    }

    @Test // TC-DC-RB
    void test_RoadBuildingCard() {
        DevCard card = new DevCard(DevCardType.ROAD_BUILDING);
        card.activateCard();

        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);
        Board mockBoard = EasyMock.createMock(Board.class);

        int initialRoadCount = player1.getInventory().get("roads");
        assertEquals(15, initialRoadCount);

        mockBoard.freeRoads(player1, 2);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(mockBoard);

        card.doRoadBuildingAction(player1, mockBoard);

        EasyMock.verify(mockBoard);

        int finalRoadCount = player1.getInventory().getOrDefault("roads", 15);
        assertEquals(13, finalRoadCount);
    }

    @Test // TC-DC-YP
    void test_YearOfPlentyCard() {
        DevCard card = new DevCard(DevCardType.YEAR_OF_PLENTY);
        card.activateCard();

        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);
        Board mockBoard = EasyMock.createMock(Board.class);

        assertEquals(0, player1.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertEquals(0, player1.getResources().getOrDefault(ResourceType.WHEAT, 0));

        EasyMock.replay(mockBoard);

        card.doYearOfPlentyAction(player1, mockBoard, ResourceType.WOOD, ResourceType.WHEAT);

        EasyMock.verify(mockBoard);

        assertEquals(1, player1.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertEquals(1, player1.getResources().getOrDefault(ResourceType.WHEAT, 0));
    }

    @Test // TC-DC-MO
    void test_MonopolyCard() {
        DevCard card = new DevCard(DevCardType.MONOPOLY);
        card.activateCard();

        Board mockBoard = EasyMock.createMock(Board.class);

        Player player1 = new Player(1, "John", PlayerColor.RED);
        Player player2 = new Player(2, "Alice", PlayerColor.BLUE);
        Player player3 = new Player(3, "Bob", PlayerColor.ORANGE);

        Map<ResourceType, Integer> p2Resources = new HashMap<>();
        p2Resources.put(ResourceType.ORE, 3);
        p2Resources.put(ResourceType.WOOD, 1);
        player2.addResources(p2Resources);

        Map<ResourceType, Integer> p3Resources = new HashMap<>();
        p3Resources.put(ResourceType.ORE, 2);
        player3.addResources(p3Resources);

        List<Player> allPlayers = new ArrayList<>();
        allPlayers.add(player1);
        allPlayers.add(player2);
        allPlayers.add(player3);

        EasyMock.replay(mockBoard);

        card.doMonopolyAction(player1, allPlayers, mockBoard, ResourceType.ORE);

        EasyMock.verify(mockBoard);

        assertEquals(0, player2.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(1, player2.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertEquals(0, player3.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(5, player1.getResources().getOrDefault(ResourceType.ORE, 0));
    }

    @Test // TC-DC-VP
    void test_VictoryPointCard_PassivelyIncrementsCalculatedPoints() {
        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);

        DevCard vpCard = new DevCard(DevCardType.VICTORY_POINT);

        assertEquals(0, player1.getVictoryPoints());

        player1.setDevCardHand(vpCard);

        assertEquals(1, player1.getVictoryPoints());
    }

    private Map<ResourceType, Integer> devCardCost() {
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.ORE, 1);
        cost.put(ResourceType.SHEEP, 1);
        cost.put(ResourceType.WHEAT, 1);
        return cost;
    }

    private Game newGameWith(Player player) {
        Board board = new Board();
        return new Game(board, List.of(player), new Dice(new Random()), new TurnManager(1));
    }

    @Test // TC-DC-DRAW-OK
    void test_DrawDevCard_WithEnoughResources_DrawsCardAndDeductsCost() {
        Player player = new Player(0, "John", PlayerColor.RED);
        player.addResources(devCardCost());

        Game game = newGameWith(player);
        game.setNextDevCardType(DevCardType.KNIGHT);

        assertEquals(0, player.getDevCardHand().size());

        game.drawDevCard(0);

        assertEquals(1, player.getDevCardHand().size());
        assertEquals(DevCardType.KNIGHT, player.getDevCardHand().get(0).getType());
        assertEquals(0, player.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(0, player.getResources().getOrDefault(ResourceType.SHEEP, 0));
        assertEquals(0, player.getResources().getOrDefault(ResourceType.WHEAT, 0));
    }

    @Test // TC-DC-DRAW-SURPLUS
    void test_DrawDevCard_WithSurplusResources_OnlyDeductsCost() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.ORE, 2);
        resources.put(ResourceType.SHEEP, 1);
        resources.put(ResourceType.WHEAT, 1);
        resources.put(ResourceType.WOOD, 3); // unrelated, must be untouched
        player.addResources(resources);

        Game game = newGameWith(player);
        game.setNextDevCardType(DevCardType.MONOPOLY);

        game.drawDevCard(0);

        assertEquals(1, player.getDevCardHand().size());
        assertEquals(1, player.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(0, player.getResources().getOrDefault(ResourceType.SHEEP, 0));
        assertEquals(0, player.getResources().getOrDefault(ResourceType.WHEAT, 0));
        assertEquals(3, player.getResources().getOrDefault(ResourceType.WOOD, 0));
    }

    @Test // TC-DC-DRAW-NONE
    void test_DrawDevCard_WithNoResources_ThrowsAndDrawsNothing() {
        Player player = new Player(0, "John", PlayerColor.RED);

        Game game = newGameWith(player);
        game.setNextDevCardType(DevCardType.KNIGHT);

        assertThrows(IllegalStateException.class, () -> game.drawDevCard(0));

        assertEquals(0, player.getDevCardHand().size());
    }

    @Test // TC-DC-DRAW-PARTIAL
    void test_DrawDevCard_MissingOneResource_ThrowsAndChargesNothing() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Map<ResourceType, Integer> resources = new HashMap<>();
        resources.put(ResourceType.ORE, 1);
        resources.put(ResourceType.SHEEP, 1);
        player.addResources(resources);

        Game game = newGameWith(player);
        game.setNextDevCardType(DevCardType.KNIGHT);

        assertThrows(IllegalStateException.class, () -> game.drawDevCard(0));

        assertEquals(0, player.getDevCardHand().size());
        assertEquals(1, player.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(1, player.getResources().getOrDefault(ResourceType.SHEEP, 0));
    }

    @Test // TC-DC-ONCE-FLAG
    void test_PlayingDevCard_SetsHasPlayedFlag() {
        int owningPlayerId = 0;
        Player player = new Player(owningPlayerId, "John", PlayerColor.RED);
        player.setDevCardHand(new DevCard(DevCardType.YEAR_OF_PLENTY));
        player.manageDevCardActivation(owningPlayerId); // start of turn: activate cards, clear flag

        Game game = newGameWith(player);

        assertFalse(player.getHasPlayedDevCardThisTurn());

        game.useDevCard(owningPlayerId, DevCardType.YEAR_OF_PLENTY, 0, -1,
                ResourceType.WOOD, ResourceType.WHEAT, null);

        assertTrue(player.getHasPlayedDevCardThisTurn());
    }

    @Test // TC-DC-ONCE-SECOND
    void test_PlayingSecondDevCard_SameTurn_ThrowsException() {
        int owningPlayerId = 0;
        Player player = new Player(owningPlayerId, "John", PlayerColor.RED);
        player.setDevCardHand(new DevCard(DevCardType.YEAR_OF_PLENTY));
        player.setDevCardHand(new DevCard(DevCardType.MONOPOLY));
        player.manageDevCardActivation(owningPlayerId);

        Game game = newGameWith(player);

        game.useDevCard(owningPlayerId, DevCardType.YEAR_OF_PLENTY, 0, -1,
                ResourceType.WOOD, ResourceType.WHEAT, null);

        assertThrows(IllegalActionException.class, () ->
                game.useDevCard(owningPlayerId, DevCardType.MONOPOLY, 0, -1,
                        null, null, ResourceType.ORE));

        assertEquals(1, player.getDevCardHand().size());
        assertEquals(DevCardType.MONOPOLY, player.getDevCardHand().get(0).getType());
    }

    @Test // TC-DC-ONCE-RESET
    void test_FlagResets_OnNextTurn_AllowsPlayingAnotherCard() {
        int owningPlayerId = 0;
        Player player = new Player(owningPlayerId, "John", PlayerColor.RED);
        player.setDevCardHand(new DevCard(DevCardType.YEAR_OF_PLENTY));
        player.setDevCardHand(new DevCard(DevCardType.MONOPOLY));
        player.manageDevCardActivation(owningPlayerId);

        Game game = newGameWith(player);

        game.useDevCard(owningPlayerId, DevCardType.YEAR_OF_PLENTY, 0, -1,
                ResourceType.WOOD, ResourceType.WHEAT, null);
        assertTrue(player.getHasPlayedDevCardThisTurn());

        player.manageDevCardActivation(owningPlayerId);
        assertFalse(player.getHasPlayedDevCardThisTurn());

        assertDoesNotThrow(() ->
                game.useDevCard(owningPlayerId, DevCardType.MONOPOLY, 0, -1,
                        null, null, ResourceType.ORE));
        assertEquals(0, player.getDevCardHand().size());
    }
}