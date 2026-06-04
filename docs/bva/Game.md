# Game BVA

Handles the progression of game

## Methods:
`start()` - loops until game ends
`build(Player currentPlayer, InfraType infraType, int locationId)`
    handles building a road, settlement, or city
    validates placement
    during setup, tracks the second settlement per player for starting resources
    handles decrementing inventory and resources
`distributeSetupResources()`
    gives each player resources from hexes adjacent to their second setup settlement
`handleMoveRobber(int roll, int newHexId)`
    handles moving a robber when the roll is 7
    block a hex by setting its hasRobber
    removes the robber from wherever it was before
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
`phaseSetupCheck()`
    returns `true` if the current phase is `SETUP`, `false` otherwise.
`advancePhase()`
    Advances the phase forward one step: `SETUP` → `NORMAL_PLAY` → `GAME_OVER`.
    When leaving `SETUP`, calls `distributeSetupResources()`.

### Method under test: `handleMoveRobber()`

|             | State of the System                         | Expected output / behavior                                 | Implemented?       |
|-------------|---------------------------------------------|------------------------------------------------------------|--------------------|
| Test Case 1 | Roll is not 7                               | Robber does not move                                       | :white_check_mark: |
| Test Case 2 | Roll is 7 and no hex currently has robber   | Selected hex has robber                                    | :white_check_mark: |
| Test Case 3 | Roll is 7 and robber starts on another hex  | Previous hex no longer has robber; selected hex has robber | :white_check_mark: |
| Test Case 4 | Roll is 7 and selected hex already has robber | Throws `IllegalStateException`                           | :white_check_mark: |
| Test Case 5 | Roll is 7 and robber moves                  | Exactly one hex has robber                                 | :white_check_mark: |
| Test Case 6 | Roll is 7 and selected hex ID is invalid    | Throws `IllegalArgumentException`                          | :white_check_mark: |

## Method under test: `getPlayer(int id)`

| Test Case   | State of the System                   | Expected Output                   | Implemented? |
|-------------|---------------------------------------|-----------------------------------|--------------|
| Test Case 1 | `id` matches the first player in list | Returns that `Player`             | :check_mark: |
| Test Case 2 | `id` matches the last player in list  | Returns that `Player`             | :check_mark: |
| Test Case 3 | `id` matches a middle player in list  | Returns that `Player`             | :check_mark: |
| Test Case 4 | `id` does not match any player        | Throws `IllegalArgumentException` | :check_mark: |

## Method under test: `phaseSetupCheck()`

| Test Case   | State of the System        | Expected Output | Implemented? |
|-------------|----------------------------|-----------------|--------------|
| Test Case 1 | `currPhase == SETUP`       | Returns `true`  | :check_mark: |
| Test Case 2 | `currPhase == NORMAL_PLAY` | Returns `false` | :check_mark: |
| Test Case 3 | `currPhase == GAME_OVER`   | Returns `false` | :check_mark: |

## Method under test: `advancePhase()`

| Test Case   | State of the System                                      | Expected Output                       | Implemented? |
|-------------|----------------------------------------------------------|---------------------------------------|--------------|
| Test Case 1 | `currPhase == SETUP` and players have second settlements | `currPhase` becomes `NORMAL_PLAY`; starting resources distributed | :check_mark: |
| Test Case 2 | `currPhase == NORMAL_PLAY`                               | `currPhase` becomes `GAME_OVER`       | :check_mark: |
| Test Case 3 | `currPhase == GAME_OVER`                                 | `currPhase` stays `GAME_OVER` (no-op) | :check_mark: |

## Method under test: `distributeSetupResources()`

| Test Case   | State of the System                                      | Expected Output                                         | Implemented? |
|-------------|----------------------------------------------------------|---------------------------------------------------------|--------------|
| Test Case 1 | Player has a recorded second setup settlement            | Player receives resources from that node only           | :check_mark: |
| Test Case 2 | Player has only a first setup settlement                 | Player receives no resources                            | :check_mark: |
| Test Case 3 | Player has first and second setup settlements            | Resources match second settlement, not first            | :check_mark: |
| Test Case 4 | Multiple players each have a second setup settlement     | Each player receives only their own second settlement resources | :check_mark: |
| Test Case 5 | No player has a second setup settlement                  | All player resource hands remain empty                  | :check_mark: |
| Test Case 6 | Second settlement is adjacent to desert                  | Desert is not added to the player's resource hand       | :check_mark: |
