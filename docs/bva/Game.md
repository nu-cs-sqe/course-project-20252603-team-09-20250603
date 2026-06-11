# Game BVA

Handles the progression of game

### Method under test: `handleMoveRobberLocation()`

|             | State of the System                         | Expected output / behavior                                 | Implemented?       |
|-------------|---------------------------------------------|------------------------------------------------------------|--------------------|
| Test Case 1 | Roll is 7 and no hex currently has robber   | Selected hex has robber                                    | :white_check_mark:                |
| Test Case 2 | Roll is 7 and robber starts on another hex  | Previous hex no longer has robber; selected hex has robber | :white_check_mark:                |
| Test Case 3 | Roll is 7 and selected hex already has robber | Throws `IllegalStateException`                           | :white_check_mark:                |
| Test Case 4 | Roll is 7 and robber moves                  | Exactly one hex has robber                                 | :white_check_mark:                |
| Test Case 5 | Roll is 7 and selected hex ID is invalid    | Throws `IllegalArgumentException`                          | :white_check_mark:                |

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


## Method under test: `build(Player currentPlayer, BuildType buildType, int locationId)` victory point updates

|              | State of the System                                  | Expected output / behavior                                  | Implemented?       |
|--------------|------------------------------------------------------|--------------------------------------------------------------|--------------------|
| Test Case 7  | Player successfully builds a road                    | Player victory points stay the same                          | :white_check_mark: |
| Test Case 8  | Player successfully builds a settlement              | Player victory points increase by 1                          | :white_check_mark: |
| Test Case 9  | Player upgrades their own settlement into a city     | Player victory points increase by 1 more, for 2 total points | :white_check_mark: |
| Test Case 10 | Player tries to build a settlement without resources | Build fails and player victory points stay the same          | :white_check_mark: |
| Test Case 11 | Player tries to build a city on an empty node        | Build fails and player victory points stay the same          | :white_check_mark: |

## Method under test: `build(...)` placement, inventory & resource rules

Validates the placement/cost side of `build()` (separate from the victory-point cases above), so the
behavior is exercised by JUnit tests (pitest does not run the cucumber suite).

| Test Case ID | State of the System                                                | Expected output / behavior                                  | Implemented?       |
|--------------|--------------------------------------------------------------------|-------------------------------------------------------------|--------------------|
| **TC-GB-01** | Valid, connected road in normal play                               | Edge becomes occupied by the player                         | :white_check_mark: |
| **TC-GB-02** | Successful build                                                   | Player's infrastructure inventory decreases by one          | :white_check_mark: |
| **TC-GB-03** | Successful build                                                   | Player's resources decrease by the build cost               | :white_check_mark: |
| **TC-GB-04** | Player has zero of that infrastructure in inventory                | Rejected (`IllegalStateException`); nothing is placed       | :white_check_mark: |
| **TC-GB-05** | Player lacks the required resources (normal play)                  | Rejected (`IllegalStateException`); nothing is placed       | :white_check_mark: |
| **TC-GB-06** | Normal-play road not connected to the player's road/building       | Rejected (`IllegalStateException`); edge stays empty        | :white_check_mark: |
| **TC-GB-07** | Settlement violates the distance rule (neighbor occupied)          | Rejected (`IllegalStateException`); target node stays empty | :white_check_mark: |
| **TC-GB-08** | Setup-phase road, unconnected vs. connected to the new settlement  | Unconnected rejected; connected road is placed              | :white_check_mark: |
| **TC-GB-09** | `infraType` is `null`                                              | Rejected (`IllegalArgumentException`, "Build type cannot be null") | :white_check_mark: |
| **TC-GB-10** | Setup-phase road placed before any settlement (no recent settlement) | Rejected (`IllegalStateException`, "...build a settlement before...road during setup."); edge stays empty | :white_check_mark: |
| **TC-GB-11** | City built on an unsettled (empty) node                            | Rejected (`IllegalStateException`, "Cannot upgrade an unsettled node to city."); node stays empty | :white_check_mark: |
| **TC-GB-12** | Setup-phase: a 2nd road placed without a new settlement in between  | Rejected (`IllegalStateException`); recent settlement is cleared after its road | :white_check_mark: |

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

## Method under test: `useDevCard(...)`

|              | State of the System                                            | Expected output / behavior                                                  | Implemented? |
|--------------|----------------------------------------------------------------|-----------------------------------------------------------------------------|--------------|
| Test Case 21 | Player attempts to manually play a `VICTORY_POINT` dev card     | Rejected (`UnsupportedOperationException`, "This development card type cannot be manually played.") | :white_check_mark: |
| Test Case 23 | Player tries to play a card type they do not hold              | Rejected (`IllegalArgumentException`, "Player doesn't have this card type") | :white_check_mark: |
| Test Case 24 | `currentPlayerId` does not match any player                     | Rejected (`IllegalArgumentException`, "Player not found with ID: ...") | :white_check_mark: |

## Method under test: `updateLargestArmyPlayer()`

|              | State of the System                                                  | Expected output / behavior                                       | Implemented? |
|--------------|----------------------------------------------------------------------|------------------------------------------------------------------|--------------|
| Test Case 22 | A new player's knight count overtakes the previous largest-army holder | Previous holder loses the largest-army flag; new holder gains it | :white_check_mark: |

## Method under test: `findPlayerByName(String name)`

|              | State of the System              | Expected output / behavior                                        | Implemented? |
|--------------|----------------------------------|-------------------------------------------------------------------|--------------|
| Test Case 25 | No player matches the given name | Throws `IllegalArgumentException` ("Player not found with name: ...") | :white_check_mark: |
## Method under test: `useDevCard(int currentPlayerId, DevCardType cardType, int targetHexId, int victimPlayerId, ResourceType choice1, ResourceType choice2, ResourceType targetType)`

Each card type, when played through the `Game`, must produce its real effect (the dispatch actually
runs the card action). The victim argument is interpreted as "no victim" only when negative.

| Test Case      | State of the System                                              | Expected output / behavior                                              | Implemented?       |
|----------------|------------------------------------------------------------------|-------------------------------------------------------------------------|--------------------|
| TC-DC-USE-RB   | Active `ROAD_BUILDING` card played through the game              | Player's unbuilt road inventory decreases by exactly 2                   | :white_check_mark: |
| TC-DC-USE-YP   | Active `YEAR_OF_PLENTY` card played, choosing Wood and Wheat     | Player gains exactly 1 Wood and 1 Wheat                                  | :white_check_mark: |
| TC-DC-USE-MO   | Active `MONOPOLY` card played targeting Ore; opponent holds 3 Ore | All of the opponent's Ore is swept to the player (opponent 0, player +3) | :white_check_mark: |
| TC-DC-USE-KN-ARMY | Player with 2 prior knights plays a 3rd `KNIGHT` through the game | Knight count becomes 3 and the player is granted Largest Army           | :white_check_mark: |
| TC-DC-USE-KN-VICTIM0 | `KNIGHT` played with `victimPlayerId == 0`, a valid adjacent victim | Player 0 is treated as a real victim; exactly one card is stolen        | :white_check_mark: |

## Method under test: `updateLargestArmyPlayer()`

The Largest Army threshold starts at 2 (a player needs 3+ knights), and the title is only taken by a
**strictly** higher knight count than the current holder.

| Test Case        | State of the System                                            | Expected output / behavior                                          | Implemented?       |
|------------------|----------------------------------------------------------------|---------------------------------------------------------------------|--------------------|
| TC-DC-ARMY-2     | A player has exactly 2 played knights (lower boundary)         | No Largest Army awarded; no victory points gained                   | :white_check_mark: |
| TC-DC-ARMY-3     | A player has exactly 3 played knights (upper boundary)        | Player is granted Largest Army and gains 2 victory points           | :white_check_mark: |
| TC-DC-ARMY-STEAL | Holder has 3 knights; another player reaches 4 knights        | Old holder loses Largest Army and 2 points; new holder gains both   | :white_check_mark: |
| TC-DC-ARMY-TIE   | Holder has 3 knights; another player ties at 3 knights        | Title is not transferred (strictly-greater rule); points unchanged  | :white_check_mark: |
## Method under test: `isGameOver()`

| Test Case   | State of the System                | Expected Output | Implemented?       |
|-------------|------------------------------------|-----------------|--------------------|
| Test Case 1 | New game (phase is not GAME_OVER)  | Returns `false` | :white_check_mark: |
| Test Case 2 | A player has won (phase GAME_OVER) | Returns `true`  | :white_check_mark: |

## Method under test: `getWinner()`

|             | State of the System                                       | Expected output / behavior              | Implemented?       |
|-------------|-----------------------------------------------------------|-----------------------------------------|--------------------|
| Test Case 1 | No player has reached `pointsNeededToWin` (10)            | Returns `null`                          | :white_check_mark: |
| Test Case 2 | A player has exactly 10 victory points                    | Returns that player                     | :white_check_mark: |
| Test Case 3 | A player has more than 10 victory points                  | Returns that player                     | :white_check_mark: |
| Test Case 4 | Multiple players are at or above 10 victory points        | Returns the player with the most points | :white_check_mark: |
| Test Case 5 | A player has reached 10 points but `getWinner` is called  | Game is **not** ended (no side effect)  | :white_check_mark: |

## Method under test: `checkForWinner()`

|             | State of the System                          | Expected output / behavior                              | Implemented?       |
|-------------|----------------------------------------------|---------------------------------------------------------|--------------------|
| Test Case 1 | No player has reached 10 victory points      | Returns `null`; phase stays unchanged (game not over)   | :white_check_mark: |
| Test Case 2 | A player has reached 10 victory points       | Returns that player; phase becomes `GAME_OVER`          | :white_check_mark: |
| Test Case 3 | A player jumps from 8 to 11 points (skips 10) | Returns that player; phase becomes `GAME_OVER`         | :white_check_mark: |
