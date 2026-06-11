package domain;

public enum DomainErrorKey {
    DEV_CARD_ALREADY_PLAYED_THIS_TURN("error.devCard.alreadyPlayedThisTurn"),
    DEV_CARD_NOT_PLAYABLE_ON_PURCHASE_TURN("error.devCard.notPlayableOnPurchaseTurn"),
    PLACEMENT_NODE_ALREADY_OCCUPIED("error.placement.nodeAlreadyOccupied"),
    PLACEMENT_ADJACENT_NODE_OCCUPIED("error.placement.adjacentNodeOccupied"),
    PLACEMENT_EDGE_ALREADY_OCCUPIED("error.placement.edgeAlreadyOccupied"),
    PLACEMENT_ROAD_MUST_CONNECT_TO_SETTLEMENT("error.placement.roadMustConnectToSettlement"),
    PLACEMENT_ROAD_MUST_CONNECT_TO_OWN_NETWORK("error.placement.roadMustConnectToOwnNetwork"),
    TRADE_BANK_SAME_RESOURCE("error.trade.bank.sameResource"),
    TRADE_BANK_INSUFFICIENT_RESOURCES("error.trade.bank.insufficientResources"),
    TRADE_PLAYER_NOTHING_OFFERED("error.trade.player.nothingOffered"),
    TRADE_PLAYER_NOTHING_REQUESTED("error.trade.player.nothingRequested"),
    TRADE_PLAYER_SELF("error.trade.player.self"),
    TRADE_PLAYER_INSUFFICIENT_RESOURCES("error.trade.player.insufficientResources");

    private final String key;

    DomainErrorKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
