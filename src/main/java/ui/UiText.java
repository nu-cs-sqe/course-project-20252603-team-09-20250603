package ui;

import domain.DevCardType;
import domain.InfraType;
import domain.LocalizedDomainException;
import domain.Player;
import domain.PlayerAction;
import domain.PlayerColor;
import domain.ResourceType;

public final class UiText {
    private UiText() {
    }

    public static String resource(ResourceType resourceType) {
        switch (resourceType) {
            case WOOD:
                return I18n.text("resource.wood");
            case BRICK:
                return I18n.text("resource.brick");
            case ORE:
                return I18n.text("resource.ore");
            case SHEEP:
                return I18n.text("resource.sheep");
            case WHEAT:
                return I18n.text("resource.wheat");
            case DESERT:
                return I18n.text("resource.desert");
            default:
                return resourceType.name();
        }
    }

    public static String devCard(DevCardType devCardType) {
        switch (devCardType) {
            case KNIGHT:
                return I18n.text("devCard.knight");
            case ROAD_BUILDING:
                return I18n.text("devCard.roadBuilding");
            case YEAR_OF_PLENTY:
                return I18n.text("devCard.yearOfPlenty");
            case MONOPOLY:
                return I18n.text("devCard.monopoly");
            case VICTORY_POINT:
                return I18n.text("devCard.victoryPoint");
            default:
                return devCardType.name();
        }
    }

    public static String infra(InfraType infraType) {
        switch (infraType) {
            case ROAD:
                return I18n.text("infra.road");
            case SETTLEMENT:
                return I18n.text("infra.settlement");
            case CITY:
                return I18n.text("infra.city");
            default:
                return infraType.name();
        }
    }

    public static String action(PlayerAction action) {
        switch (action) {
            case BUILD:
                return I18n.text("action.build");
            case BUY_DEV_CARD:
                return I18n.text("action.buyDevCard");
            case USE_DEV_CARD:
                return I18n.text("action.useDevCard");
            case TRADE_WITH_PLAYER:
                return I18n.text("action.tradeWithPlayer");
            case TRADE_WITH_BANK:
                return I18n.text("action.tradeWithBank");
            case END_TURN:
                return I18n.text("action.endTurn");
            default:
                return action.name();
        }
    }

    public static String color(PlayerColor color) {
        switch (color) {
            case RED:
                return I18n.text("color.red");
            case BLUE:
                return I18n.text("color.blue");
            case ORANGE:
                return I18n.text("color.orange");
            case WHITE:
                return I18n.text("color.white");
            default:
                return color.name();
        }
    }

    public static String playerLabel(Player player) {
        return I18n.text("player.label", player.getName(), color(player.getColor()));
    }

    public static String exceptionMessage(Throwable throwable) {
        if (throwable == null) {
            return I18n.text("error.generic");
        }

        if (throwable instanceof LocalizedDomainException) {
            LocalizedDomainException localized = (LocalizedDomainException) throwable;
            return I18n.text(localized.getMessageKey(), localized.getMessageArgs());
        }

        Throwable cause = throwable.getCause();
        if (cause instanceof LocalizedDomainException) {
            LocalizedDomainException localized = (LocalizedDomainException) cause;
            return I18n.text(localized.getMessageKey(), localized.getMessageArgs());
        }

        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            return I18n.text("error.generic");
        }

        if (message.startsWith("error.")) {
            return I18n.text(message);
        }

        String mappedDynamicMessage = mapLegacyDynamicMessage(message);
        if (mappedDynamicMessage != null) {
            return mappedDynamicMessage;
        }

        String legacyKey = mapLegacyMessageToKey(message);
        return legacyKey == null ? message : I18n.text(legacyKey);
    }

    private static String mapLegacyDynamicMessage(String message) {
        if (message.startsWith("Player not found with ID: ")) {
            return I18n.text("error.game.playerIdNotFound",
                    message.substring("Player not found with ID: ".length()));
        }

        if (message.startsWith("Player not found with name: ")) {
            return I18n.text("error.game.playerNameNotFound",
                    message.substring("Player not found with name: ".length()));
        }

        return null;
    }

    private static String mapLegacyMessageToKey(String message) {
        switch (message) {
            case "No players entered":
                return "error.setup.noPlayersEntered";
            case "Player names must be unique":
                return "error.setup.namesMustBeUnique";
            case "Catan requires players of 3 or 4":
                return "error.setup.invalidPlayerCount";
            case "Player name invalid, please input a name":
                return "error.setup.invalidPlayerName";
            case "Invalid player index for color assignment":
                return "error.setup.invalidColorIndex";
            case "Player not found":
                return "error.game.playerNotFound";
            case "Build type cannot be null":
                return "error.game.buildTypeRequired";
            case "Player does not have enough inventory":
                return "error.game.insufficientInventory";
            case "Player does not have enough resources":
                return "error.game.insufficientResources";
            case "You must build a settlement before building a road during setup.":
                return "error.game.setupSettlementBeforeRoad";
            case "Player does not have enough resources to draw a dev card.":
                return "error.game.insufficientResourcesDevCard";
            case "Player doesn't have this card type":
                return "error.game.playerCardMissing";
            case "This development card type cannot be manually played.":
                return "error.game.devCardManualPlayUnsupported";
            case "Selected hex already has the robber.":
                return "error.game.robberHexAlreadyOccupied";
            case "No development cards left in the deck!":
                return "error.game.devCardDeckEmpty";
            case "Cannot trade a resource for null.":
                return "error.trade.nullResource";
            case "Offered resource quantities must be positive.":
                return "error.trade.offerMustBePositive";
            case "Requested resource quantities must be positive.":
                return "error.trade.requestMustBePositive";
            case "Cannot settle on an already-settled node.":
                return "error.placement.nodeAlreadySettled";
            case "Cannot build a road on an occupied edge.":
                return "error.placement.roadOnOccupiedEdge";
            case "Player has no resource cards.":
                return "error.player.noResourceCards";
            case "Not enough road pieces remaining in inventory!":
                return "error.player.notEnoughRoadPieces";
            case "Invalid placement.":
                return "error.setup.invalidPlacement";
            default:
                return null;
        }
    }
}
