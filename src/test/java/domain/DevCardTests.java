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

    @Test // TC-DC-YP-INACTIVE
    void test_YearOfPlentyCard_WhenInactive_ThrowsIllegalActionException() {
        DevCard card = new DevCard(DevCardType.YEAR_OF_PLENTY);
        Player player = new Player(1, "John", PlayerColor.RED);
        Board board = EasyMock.createMock(Board.class);

        EasyMock.replay(board);

        assertThrows(IllegalActionException.class, () ->
                card.doYearOfPlentyAction(player, board, ResourceType.WOOD, ResourceType.WHEAT));

        EasyMock.verify(board);
    }

    @Test // TC-DC-YP-WRONG-TYPE
    void test_YearOfPlentyCard_WithWrongCardType_ThrowsIllegalStateException() {
        DevCard card = new DevCard(DevCardType.KNIGHT);
        Player player = new Player(1, "John", PlayerColor.RED);
        Board board = EasyMock.createMock(Board.class);
        card.activateCard();

        EasyMock.replay(board);

        assertThrows(IllegalStateException.class, () ->
                card.doYearOfPlentyAction(player, board, ResourceType.WOOD, ResourceType.WHEAT));

        EasyMock.verify(board);
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

    @Test // TC-DC-MO-INACTIVE
    void test_MonopolyCard_WhenInactive_ThrowsIllegalActionException() {
        DevCard card = new DevCard(DevCardType.MONOPOLY);
        Player player = new Player(1, "John", PlayerColor.RED);
        Board board = EasyMock.createMock(Board.class);

        EasyMock.replay(board);

        assertThrows(IllegalActionException.class, () ->
                card.doMonopolyAction(player, List.of(player), board, ResourceType.ORE));

        EasyMock.verify(board);
    }

    @Test // TC-DC-MO-WRONG-TYPE
    void test_MonopolyCard_WithWrongCardType_ThrowsIllegalStateException() {
        DevCard card = new DevCard(DevCardType.YEAR_OF_PLENTY);
        Player player = new Player(1, "John", PlayerColor.RED);
        Board board = EasyMock.createMock(Board.class);
        card.activateCard();

        EasyMock.replay(board);

        assertThrows(IllegalStateException.class, () ->
                card.doMonopolyAction(player, List.of(player), board, ResourceType.ORE));

        EasyMock.verify(board);
    }

    @Test // TC-DC-VERIFY-WRONG-TYPE
    void test_RoadBuildingAction_WithWrongCardType_ThrowsIllegalStateException() {
        DevCard card = new DevCard(DevCardType.KNIGHT);
        Player player = new Player(1, "John", PlayerColor.RED);
        Board board = EasyMock.createMock(Board.class);
        card.activateCard();

        EasyMock.replay(board);

        assertThrows(IllegalStateException.class, () ->
                card.doRoadBuildingAction(player, board));

        EasyMock.verify(board);
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


    @Test // TC-DC-USE-RB
    void test_UseDevCard_RoadBuilding_ThroughGame_DeductsTwoRoads() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        player.setDevCardHand(new DevCard(DevCardType.ROAD_BUILDING));
        player.manageDevCardActivation(0);
        int roadsBefore = player.getInventory().get("roads");

        game.useDevCard(0, DevCardType.ROAD_BUILDING, -1, -1, null, null, null);

        assertEquals(roadsBefore - 2, player.getInventory().get("roads"));
    }

    @Test // TC-DC-USE-YP
    void test_UseDevCard_YearOfPlenty_ThroughGame_AddsTwoChosenResources() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        player.setDevCardHand(new DevCard(DevCardType.YEAR_OF_PLENTY));
        player.manageDevCardActivation(0);

        game.useDevCard(0, DevCardType.YEAR_OF_PLENTY, -1, -1,
                ResourceType.WOOD, ResourceType.WHEAT, null);

        assertEquals(1, player.getResources().getOrDefault(ResourceType.WOOD, 0));
        assertEquals(1, player.getResources().getOrDefault(ResourceType.WHEAT, 0));
    }

    @Test // TC-DC-USE-MO
    void test_UseDevCard_Monopoly_ThroughGame_SweepsResourceFromOpponents() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Player opponent = new Player(1, "Alice", PlayerColor.BLUE);
        Game game = new Game(new Board(), List.of(player, opponent), new Dice(new Random()), new TurnManager(2));

        Map<ResourceType, Integer> oppResources = new HashMap<>();
        oppResources.put(ResourceType.ORE, 3);
        opponent.addResources(oppResources);

        player.setDevCardHand(new DevCard(DevCardType.MONOPOLY));
        player.manageDevCardActivation(0);

        game.useDevCard(0, DevCardType.MONOPOLY, -1, -1, null, null, ResourceType.ORE);

        assertEquals(0, opponent.getResources().getOrDefault(ResourceType.ORE, 0));
        assertEquals(3, player.getResources().getOrDefault(ResourceType.ORE, 0));
    }

    @Test // TC-DC-USE-KN-ARMY
    void test_UseDevCard_ThirdKnight_ThroughGame_GrantsLargestArmy() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        player.incrementPlayedKnightCount();
        player.incrementPlayedKnightCount(); // two knights already played
        player.setDevCardHand(new DevCard(DevCardType.KNIGHT));
        player.manageDevCardActivation(0);

        game.useDevCard(0, DevCardType.KNIGHT, 0, -1, null, null, null);

        assertEquals(3, player.getPlayedKnightCount());
        assertTrue(player.isHasLargestArmy());
    }

    @Test // TC-DC-USE-KN-VICTIM0
    void test_UseDevCard_Knight_VictimIdZero_StealsFromPlayerZero() {
        Board board = new Board(new Random(0));
        Player thief = new Player(1, "John", PlayerColor.RED);
        Player victim = new Player(0, "Alice", PlayerColor.BLUE); // victim id is exactly 0

        board.getNode(0).buildSettlement(victim);
        Map<ResourceType, Integer> hand = new HashMap<>();
        hand.put(ResourceType.WHEAT, 2);
        victim.addResources(hand);

        Game game = new Game(board, List.of(thief, victim), new Dice(new Random()), new TurnManager(2));
        thief.setDevCardHand(new DevCard(DevCardType.KNIGHT));
        thief.manageDevCardActivation(thief.getId());

        // victimPlayerId == 0 must be treated as a real victim (boundary: < 0, not <= 0).
        game.useDevCard(thief.getId(), DevCardType.KNIGHT, 0, victim.getId(), null, null, null);

        assertEquals(1, victim.getResources().getOrDefault(ResourceType.WHEAT, 0));
        assertEquals(1, thief.getResources().getOrDefault(ResourceType.WHEAT, 0));
    }


    @Test // TC-DC-ARMY-2
    void test_UpdateLargestArmy_TwoKnights_DoesNotGrant() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        for (int i = 0; i < 2; i++) {
            player.incrementPlayedKnightCount();
        }

        game.updateLargestArmyPlayer();

        assertFalse(player.isHasLargestArmy());
        assertEquals(0, player.getVictoryPoints());
    }

    @Test // TC-DC-ARMY-3
    void test_UpdateLargestArmy_ThreeKnights_GrantsArmyAndTwoPoints() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        for (int i = 0; i < 3; i++) {
            player.incrementPlayedKnightCount();
        }

        game.updateLargestArmyPlayer();

        assertTrue(player.isHasLargestArmy());
        assertEquals(2, player.getVictoryPoints());
    }

    @Test // TC-DC-ARMY-STEAL
    void test_UpdateLargestArmy_HigherKnightCount_StealsFromCurrentHolder() {
        Player playerA = new Player(0, "A", PlayerColor.RED);
        Player playerB = new Player(1, "B", PlayerColor.BLUE);
        Game game = new Game(new Board(), List.of(playerA, playerB), new Dice(new Random()), new TurnManager(2));

        for (int i = 0; i < 3; i++) {
            playerA.incrementPlayedKnightCount();
        }
        game.updateLargestArmyPlayer();
        assertTrue(playerA.isHasLargestArmy());
        assertEquals(2, playerA.getVictoryPoints());

        for (int i = 0; i < 4; i++) {
            playerB.incrementPlayedKnightCount();
        }
        game.updateLargestArmyPlayer();

        assertFalse(playerA.isHasLargestArmy());
        assertEquals(0, playerA.getVictoryPoints());
        assertTrue(playerB.isHasLargestArmy());
        assertEquals(2, playerB.getVictoryPoints());
    }

    @Test // TC-DC-ARMY-TIE
    void test_UpdateLargestArmy_EqualKnightCount_DoesNotSteal() {
        Player playerA = new Player(0, "A", PlayerColor.RED);
        Player playerB = new Player(1, "B", PlayerColor.BLUE);
        Game game = new Game(new Board(), List.of(playerA, playerB), new Dice(new Random()), new TurnManager(2));

        for (int i = 0; i < 3; i++) {
            playerA.incrementPlayedKnightCount();
        }
        game.updateLargestArmyPlayer(); // A holds it

        for (int i = 0; i < 3; i++) {
            playerB.incrementPlayedKnightCount();
        }
        game.updateLargestArmyPlayer(); // B ties at 3 -> must NOT steal

        assertTrue(playerA.isHasLargestArmy());
        assertFalse(playerB.isHasLargestArmy());
        assertEquals(2, playerA.getVictoryPoints());
        assertEquals(0, playerB.getVictoryPoints());
    }


    @Test // TC-DC-USE-PICK-BY-TYPE
    void test_UseDevCard_SelectsRequestedTypeNotFirstInHand() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        player.setDevCardHand(new DevCard(DevCardType.YEAR_OF_PLENTY));
        player.setDevCardHand(new DevCard(DevCardType.ROAD_BUILDING));
        player.manageDevCardActivation(0);

        game.useDevCard(0, DevCardType.ROAD_BUILDING, -1, -1, null, null, null);

        List<DevCard> remaining = player.getDevCardHand();
        assertEquals(1, remaining.size());
        assertEquals(DevCardType.YEAR_OF_PLENTY, remaining.get(0).getType());
    }

    @Test // TC-DC-USE-MISSING
    void test_UseDevCard_PlayerDoesNotHaveCardType_ThrowsIllegalArgument() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);

        assertThrows(IllegalArgumentException.class,
                () -> game.useDevCard(0, DevCardType.KNIGHT, 0, -1, null, null, null));
    }

    @Test // TC-DC-DRAW-BADID
    void test_DrawDevCard_InvalidPlayerId_ThrowsIllegalArgument() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);

        assertThrows(IllegalArgumentException.class, () -> game.drawDevCard(999));
    }

    @Test // TC-DC-RB-INACTIVE
    void test_RoadBuilding_OnInactiveCard_ThrowsAndDeductsNoRoads() {
        DevCard card = new DevCard(DevCardType.ROAD_BUILDING); // bought this turn -> inactive
        Player player = new Player(0, "John", PlayerColor.RED);
        Board board = new Board();

        assertThrows(IllegalActionException.class, () -> card.doRoadBuildingAction(player, board));
        assertEquals(15, player.getInventory().get("roads"));
    }

    @Test // TC-DC-USE-VP
    void test_UseDevCard_VictoryPoint_ThrowsUnsupportedOperation() {
        Player player = new Player(0, "John", PlayerColor.RED);
        Game game = newGameWith(player);
        player.setDevCardHand(new DevCard(DevCardType.VICTORY_POINT));
        player.manageDevCardActivation(0);

        assertThrows(UnsupportedOperationException.class,
                () -> game.useDevCard(0, DevCardType.VICTORY_POINT, -1, -1, null, null, null));
    }
}
