# Game BVA

Handles the progression of game

## Methods:
`start()` - loops until game ends
`handleInitialPlacementPhase()`
    handles the starting settlement + road placements for each player
    invokes PlacementValidator and TurnManager
`handleMoveRobber(Player player)`
    handles moving a robber when the roll is 7
    block a hex by setting its hasRobber
    removes the robber from wherever it was before
`handleDistributeResources(int roll)`
    distributes resources to all players based on a roll
`getPlayerAction(Player currentPlayer)`
    action selection mocked for testing for now
`handleBuild(Player currentPlayer)`
    handles building a road, settlement, or city
    validates placement
    handles decrementing resources
`handleBuyDevCard(Player currentPlayer)`
    handles player buying devCard
    handles decrementing resources
`handleUseDevCard(Player currentPlayer)`
    handles player using devCard
`handleTrade(Player currentPlayer)`
    handles player trading
    updates resources
`updatePlayerPoints(Player currentPlayer)`
    updates the player's victory points

### Method under test: `handleMoveRobber()`

|             | State of the System                         | Expected output / behavior                                 | Implemented?       |
|-------------|---------------------------------------------|------------------------------------------------------------|--------------------|
| Test Case 1 | Roll is not 7                               | Robber does not move                                       | :white_check_mark: |
| Test Case 2 | Roll is 7 and no hex currently has robber   | Selected hex has robber                                    | :white_check_mark:                |
| Test Case 3 | Roll is 7 and robber starts on another hex  | Previous hex no longer has robber; selected hex has robber | :white_check_mark:                |
| Test Case 4 | Roll is 7 and selected hex already has robber | Throws `IllegalArgumentException`                           | :white_check_mark:                |
| Test Case 5 | Roll is 7 and robber moves                  | Exactly one hex has robber                                 | :white_check_mark:                |
| Test Case 6 | Roll is 7 and selected hex ID is invalid    | Throws `IllegalArgumentException`                          | :white_check_mark:                |
