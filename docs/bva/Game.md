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


## Method under test: `build(Player currentPlayer, BuildType buildType, int locationId)` victory point updates

|              | State of the System                                  | Expected output / behavior                                  | Implemented?       |
|--------------|------------------------------------------------------|--------------------------------------------------------------|--------------------|
| Test Case 7  | Player successfully builds a road                    | Player victory points stay the same                          | :white_check_mark: |
| Test Case 8  | Player successfully builds a settlement              | Player victory points increase by 1                          | :white_check_mark: |
| Test Case 9  | Player upgrades their own settlement into a city     | Player victory points increase by 1 more, for 2 total points | :white_check_mark: |
| Test Case 10 | Player tries to build a settlement without resources | Build fails and player victory points stay the same          | :white_check_mark: |
| Test Case 11 | Player tries to build a city on an empty node        | Build fails and player victory points stay the same          | :white_check_mark: |

## Method under test: `calculateLongestRoad(Player player)`

|              | State of the System                            | Expected output / behavior                         | Implemented? |
|--------------|------------------------------------------------|-----------------------------------------------------|---------|
| Test Case 12 | Player has no roads                            | Returns 0                                           | :white_check_mark: |
| Test Case 13 | Player has one road                            | Returns 1                                           | :white_check_mark: |
| Test Case 14 | Player has a chain of three connected roads    | Returns 3                                           | :white_check_mark: |
| Test Case 15 | Board has roads from multiple players          | Only the selected player's roads are counted        | :white_check_mark: |
| Test Case 16 | Player has at least five connected roads       | Player qualifies for longest road bonus             | :white_check_mark: |

## Method under test: `updateLongestRoadBonus()`

|              | State of the System                              | Expected output / behavior                         | Implemented? |
|--------------|--------------------------------------------------|-----------------------------------------------------|----------|
| Test Case 17 | No player has a road of length at least 5        | No victory points are awarded                       | :white_check_mark:       |
| Test Case 18 | One player reaches a road length of 5            | That player gains 2 victory points                  | :white_check_mark:       |
| Test Case 19 | Same player still has longest road               | Victory points do not change again                  | :white_check_mark:       |
| Test Case 20 | Another player exceeds the current longest road  | Old player loses 2 points; new player gains 2       | :white_check_mark:       |