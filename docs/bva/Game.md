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