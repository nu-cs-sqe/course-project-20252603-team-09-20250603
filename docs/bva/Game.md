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
    Getter to pass to controller - untested as simply actas a simple getter
`setCurrPhase`
    Setter to pass to controller - untested as simply actas a simple getter
`getTurnManager`
    Getter to pass to controller - untested as simply actas a simple getter
`getPlayer(int id)`
    Returns the player with the matching id. Throws `IllegalArgumentException` if not found.
`PhaseSetupCheck()`
    returns `true` if the current phase is `SETUP`, `false` otherwise.

### Method under test: `getCurrentPlayer()`

| Test Case     | State of the System                                | Expected output                           | Implemented? |
|---------------|----------------------------------------------------|-------------------------------------------|--------------|
| Test Case 1   | Game has 3 players; current player is first player | Returns first player                      | :x:          |
| Test Case 2   | Game has 4 players; current player is last player  | Returns last player                       | :x:          |
| Test Case 3   | Game has 3 players; turn advances past last player | Returns first player again if turns cycle | :x:          |

### Method under test: `nextTurn()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 1  |                     |                 | :x:          |
| Test Case 2  |                     |                 | :x:          |

### Method under test: `getBoard()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 1  |                     |                 | :x:          |
| Test Case 2  |                     |                 | :x:          |

### Method under test: `buildSettlement()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 1  |                     |                 | :x:          |
| Test Case 2  |                     |                 | :x:          |

### Method under test: `buildRoad()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 1  |                     |                 | :x:          |
| Test Case 2  |                     |                 | :x:          |

### Method under test: `buildCity()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 1  |                     |                 | :x:          |
| Test Case 2  |                     |                 | :x:          |

### Method under test: `isSetupComplete()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 1  |                     |                 | :x:          |
| Test Case 2  |                     |                 | :x:          |

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
