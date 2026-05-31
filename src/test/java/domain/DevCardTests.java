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

        assertThrows(IllegalActionException.class, () -> {
            card.doDevCardAction(mockPlayer, mockBoard);
        });

        EasyMock.verify(mockPlayer, mockBoard);
    }

    @Test // TC-DC-02
    void test_EndOfTurnPhaseChange_RemainsInactive() {
        int owningPlayerId = 1;
        Player player1 = new Player(owningPlayerId, "John", PlayerColor.RED);
        DevCard card = new DevCard(DevCardType.KNIGHT);
        player1.setDevCardHand(card);

        assertFalse(card.getIsActive());

        player1.manageDevCardActivation(2);
        assertFalse(card.getIsActive());

        player1.manageDevCardActivation(3);
        assertFalse(card.getIsActive());
    }
}