package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {

    private Game game;
    private Player player0;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player0 = new Player(0, "Ben", PlayerColor.RED);
        player1 = new Player(1, "Benny", PlayerColor.BLUE);
        player2 = new Player(2, "Benji", PlayerColor.ORANGE);

        game = new Game(
                new Board(),
                List.of(player0, player1, player2),
                new Dice(new Random()),
                new TurnManager(3)
        );
    }

    @Test
    public void getPlayer_FirstPlayer_ReturnsCorrectPlayer() {
        assertEquals(player0, game.getPlayer(0));
    }

    @Test
    public void getPlayer_LastPlayer_ReturnsCorrectPlayer() {
        assertEquals(player2, game.getPlayer(2));
    }

    @Test
    public void getPlayer_MiddlePlayer_ReturnsCorrectPlayer() {
        assertEquals(player1, game.getPlayer(1));
    }

    @Test
    public void getPlayer_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> game.getPlayer(99));
    }


    @Test
    public void phaseSetupCheck_WhenSetup_ReturnsTrue() {
        game.setCurrPhase(GamePhase.SETUP);
        assertTrue(game.phaseSetupCheck());
    }

    @Test
    public void phaseSetupCheck_WhenNormalPlay_ReturnsFalse() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        assertFalse(game.phaseSetupCheck());
    }

    @Test
    public void phaseSetupCheck_WhenGameOver_ReturnsFalse() {
        game.setCurrPhase(GamePhase.GAME_OVER);
        assertFalse(game.phaseSetupCheck());
    }

    @Test // TC-G-08
    public void advancePhase_WhenSetup_BecomesNormalPlayAndDistributesResources() {
        game.setCurrPhase(GamePhase.SETUP);
        placeSetupSettlements(player0, 0, 10);

        game.advancePhase();

        assertFalse(game.phaseSetupCheck());
        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(10)),
                player0.getResources()
        );
    }

    @Test
    public void advancePhase_WhenNormalPlay_BecomesGameOver() {
        game.setCurrPhase(GamePhase.NORMAL_PLAY);
        game.advancePhase();
        assertFalse(game.phaseSetupCheck());
        assertDoesNotThrow(() -> game.advancePhase());
        assertFalse(game.phaseSetupCheck());
    }

    @Test
    public void advancePhase_WhenGameOver_IsNoOp() {
        game.setCurrPhase(GamePhase.GAME_OVER);
        assertDoesNotThrow(() -> game.advancePhase());
        assertFalse(game.phaseSetupCheck());
    }

    @Test
    public void distributeSetupResources_UsesOnlySecondSetupSettlement() {
        game.setCurrPhase(GamePhase.SETUP);
        placeSetupSettlements(player0, 0, 10);

        game.distributeSetupResources();

        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(10)),
                player0.getResources()
        );
    }

    @Test // TC-G-12
    public void distributeSetupResources_OnlyFirstSettlement_GivesNoResources() {
        game.setCurrPhase(GamePhase.SETUP);
        game.build(player0, InfraType.SETTLEMENT, 0);

        game.distributeSetupResources();

        assertTrue(player0.getResources().isEmpty());
    }

    @Test // TC-G-13
    public void distributeSetupResources_ExcludesFirstSettlementResources() {
        game.setCurrPhase(GamePhase.SETUP);
        placeSetupSettlements(player0, 0, 10);

        game.distributeSetupResources();

        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(10)),
                player0.getResources()
        );
        assertNotEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(0)),
                player0.getResources()
        );
    }

    @Test // TC-G-14
    public void distributeSetupResources_MultiplePlayers_EachReceivesOwnSecondSettlementResources() {
        game.setCurrPhase(GamePhase.SETUP);

        int[] player0Nodes = placeSetupSettlements(player0);
        int[] player1Nodes = placeSetupSettlements(player1);
        int[] player2Nodes = placeSetupSettlements(player2);

        game.distributeSetupResources();

        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(player0Nodes[1])),
                player0.getResources()
        );
        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(player1Nodes[1])),
                player1.getResources()
        );
        assertEquals(
                game.getBoard().getAdjacentResources(game.getBoard().getNode(player2Nodes[1])),
                player2.getResources()
        );
    }

    @Test // TC-G-15
    public void distributeSetupResources_NoSecondSettlements_AllHandsRemainEmpty() {
        game.distributeSetupResources();

        assertTrue(player0.getResources().isEmpty());
        assertTrue(player1.getResources().isEmpty());
        assertTrue(player2.getResources().isEmpty());
    }

    @Test // TC-G-16
    public void distributeSetupResources_DesertHexAdjacent_DoesNotAddDesertToHand() {
        game.setCurrPhase(GamePhase.SETUP);
        game.build(player0, InfraType.SETTLEMENT, findValidSettlementNode());
        int desertNodeId = findNodeAdjacentToDesert();
        game.build(player0, InfraType.SETTLEMENT, desertNodeId);

        game.distributeSetupResources();

        assertFalse(player0.getResources().containsKey(ResourceType.DESERT));
    }

    private int[] placeSetupSettlements(Player player) {
        int firstNodeId = findValidSettlementNode();
        game.build(player, InfraType.SETTLEMENT, firstNodeId);
        int secondNodeId = findValidSettlementNode();
        game.build(player, InfraType.SETTLEMENT, secondNodeId);
        return new int[] {firstNodeId, secondNodeId};
    }

    private void placeSetupSettlements(Player player, int firstNodeId, int secondNodeId) {
        game.build(player, InfraType.SETTLEMENT, firstNodeId);
        game.build(player, InfraType.SETTLEMENT, secondNodeId);
    }

    private int findValidSettlementNode() {
        PlacementValidator validator = new PlacementValidator(game.getBoard());
        for (Node node : game.getBoard().getNodes()) {
            try {
                validator.validateSettlementPlacement(node);
                return node.getId();
            } catch (IllegalPlacementException ignored) {
                // try next node
            }
        }
        throw new IllegalStateException("No valid settlement node found.");
    }

    private int findNodeAdjacentToDesert() {
        PlacementValidator validator = new PlacementValidator(game.getBoard());
        for (Node node : game.getBoard().getNodes()) {
            boolean touchesDesert = false;
            for (Hex hex : game.getBoard().getHexesFromNode(node)) {
                if (hex.getResourceType() == ResourceType.DESERT) {
                    touchesDesert = true;
                    break;
                }
            }
            if (!touchesDesert) {
                continue;
            }
            try {
                validator.validateSettlementPlacement(node);
                return node.getId();
            } catch (IllegalPlacementException ignored) {
                // try next node
            }
        }
        throw new IllegalStateException("No valid desert-adjacent settlement node found.");
    }
}
