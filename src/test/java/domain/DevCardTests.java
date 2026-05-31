package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;
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
            card.doDevCardAction(mockPlayer, mockBoard, targetHexId);
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

        mockBoard.moveRobber(player1, targetHexId);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(mockBoard);

        card.doDevCardAction(player1, mockBoard, targetHexId);

        EasyMock.verify(mockBoard);
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

        card.doDevCardAction(player1, mockBoard, -1);

        EasyMock.verify(mockBoard);

        int finalRoadCount = player1.getInventory().getOrDefault("roads", 15);
        assertEquals(13, finalRoadCount);
    }
}