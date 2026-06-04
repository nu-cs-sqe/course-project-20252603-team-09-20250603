package ui;

import domain.Game;
import domain.InfraType;
import domain.Player;
import domain.PlayerAction;
import domain.TurnManager;
import java.util.ArrayList;
import java.util.List;

public class PlayerActionController {
    public enum LocationType {
        NODE, EDGE
    }

    private PlayerActionView view;
    private final Game game;
    private final TurnManager turnManager;
    private final List<Player> players;

    private int selectedLocationId = -1;
    private LocationType selectedLocationType = null;
    private InfraType selectedBuildType = null;

    public PlayerActionController(List<Player> players, Game game, TurnManager turnManager) {
        this.players = new ArrayList<>(players);
        this.game = game;
        this.turnManager = turnManager;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP2")
    public void setView(PlayerActionView view) {
        this.view = view;
        update();
    }

    public void update() {
        if (view != null) {
            view.renderActionMenu();
        }
    }

    public void onActionClicked(PlayerAction action) {
        switch (action) {
            case BUILD:
                clearBuildState();
                if (view != null) {
                    view.renderBuildMenu();
                }
                break;
            case BUY_DEV_CARD:
                // TODO: implement later
                break;
            case USE_DEV_CARD:
                // TODO: implement later
                break;
            case TRADE:
                // TODO: implement later
                break;
            case END_TURN:
                turnManager.nextPlayer();
                clearBuildState();
                update();
                break;
            default:
                break;
        }
    }

    public void onBuildTypeSelected(InfraType infraType) {
        if (infraType == null) {
            return;
        }
        selectedBuildType = infraType;
        selectedLocationId = -1;
        selectedLocationType = null;
        if (view != null) {
            view.onBuildTypeSelected(infraType);
        }
    }

    public void onLocationSelected(int locationId, LocationType locationType) {
        if (selectedBuildType == null) {
            if (view != null) {
                view.showError("Select infrastructure type to build first!");
            }
            return;
        }

        if (!isLocationTypeValidForInfra(locationType)) {
            String infraName = selectedBuildType.name().toLowerCase();
            String allowed = (selectedBuildType == InfraType.ROAD) ? "edges" : "nodes";
            if (view != null) {
                view.showError(infraName + " must be placed on " + allowed + "!");
            }
            return;
        }

        selectedLocationId = locationId;
        selectedLocationType = locationType;
    }

    public void onBuildConfirmed() {
        if (selectedBuildType == null || selectedLocationId < 0 || selectedLocationType == null) {
            if (view != null) {
                view.showError("must select both infrastructure type and location in order to build");
            }
            return;
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return;
        }

        try {
            game.build(currentPlayer, selectedBuildType, selectedLocationId);
            if (view != null) {
                view.showSuccess("Successful build");
            }
            clearBuildState();
            update();
        } catch (IllegalStateException | IllegalArgumentException e) {
            if (view != null) {
                view.showError(e.getMessage());
            }
            clearBuildState();
            update();
        }
    }

    public void onBuildCanceled() {
        clearBuildState();
        update();
    }

    public Player getCurrentPlayer() {
        int index = turnManager.getCurrentPlayerIndex();
        return players.get(index);
    }

    private boolean isLocationTypeValidForInfra(LocationType locationType) {
        if (selectedBuildType == InfraType.ROAD) {
            return locationType == LocationType.EDGE;
        } else {
            return locationType == LocationType.NODE;
        }
    }

    private void clearBuildState() {
        selectedBuildType = null;
        selectedLocationId = -1;
        selectedLocationType = null;
    }
}
