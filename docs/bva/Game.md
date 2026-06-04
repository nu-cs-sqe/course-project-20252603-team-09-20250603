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
`getBoard`
    Getter to pass to controller - untested as simply acts as a simple getter (discussed with Prof. Yiji in OH)
`getPlayers`
    Getter to pass to controller - untested as simply acts as a simple getter
`setCurrPhase`
    Setter to pass to controller - untested as simply acts as a simple getter
`getTurnManager`
    Getter to pass to controller - untested as simply acts as a simple getter
`getPlayer(int id)`
    Returns the player with the matching id. Throws `IllegalArgumentException` if not found.
`PhaseSetupCheck()`
    returns `true` if the current phase is `SETUP`, `false` otherwise.
`AdvancePhase()`
    Advances the phase forward one step: `SETUP` → `NORMAL_PLAY` → `GAME_OVER`.

### Method under test: `handleMoveRobber()`

|             | State of the System                         | Expected output / behavior                                 | Implemented?       |
|-------------|---------------------------------------------|------------------------------------------------------------|--------------------|
| Test Case 1 | Roll is not 7                               | Robber does not move                                       | :white_check_mark: |
| Test Case 2 | Roll is 7 and no hex currently has robber   | Selected hex has robber                                    | :white_check_mark:                |
| Test Case 3 | Roll is 7 and robber starts on another hex  | Previous hex no longer has robber; selected hex has robber | :white_check_mark:                |
| Test Case 4 | Roll is 7 and selected hex already has robber | Throws `IllegalArgumentException`                           | :white_check_mark:                |
| Test Case 5 | Roll is 7 and robber moves                  | Exactly one hex has robber                                 | :white_check_mark:                |
| Test Case 6 | Roll is 7 and selected hex ID is invalid    | Throws `IllegalArgumentException`                          | :white_check_mark:                |

## Method under test: `getPlayer(int id)`

| Test Case   | State of the System                   | Expected Output                   | Implemented? |
|-------------|---------------------------------------|-----------------------------------|--------------|
| Test Case 1 | `id` matches the first player in list | Returns that `Player`             | :check_mark  |
| Test Case 2 | `id` matches the last player in list  | Returns that `Player`             | :check_mark  |
| Test Case 3 | `id` matches a middle player in list  | Returns that `Player`             | :check_mark  |
| Test Case 4 | `id` does not match any player        | Throws `IllegalArgumentException` | :check_mark  |

## Method under test: `PhaseSetupCheck()`

| Test Case   | State of the System        | Expected Output | Implemented? |
|-------------|----------------------------|-----------------|--------------|
| Test Case 1 | `currPhase == SETUP`       | Returns `true`  | :check_mark  |
| Test Case 2 | `currPhase == NORMAL_PLAY` | Returns `false` | :check_mark  |
| Test Case 3 | `currPhase == GAME_OVER`   | Returns `false` | :check_mark  |

## Method under test: `advancePhase()`

| Test Case   | State of the System        | Expected Output                       | Implemented? |
|-------------|----------------------------|---------------------------------------|-------|
| Test Case 1 | `currPhase == SETUP`       | `currPhase` becomes `NORMAL_PLAY`     | :check_mark     |
| Test Case 2 | `currPhase == NORMAL_PLAY` | `currPhase` becomes `GAME_OVER`       | :check_mark     |
| Test Case 3 | `currPhase == GAME_OVER`   | `currPhase` stays `GAME_OVER` (no-op) | :check_mark     |