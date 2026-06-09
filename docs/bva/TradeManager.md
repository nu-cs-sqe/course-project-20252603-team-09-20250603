# TradeManager BVA

Validates and executes trades with the bank and between players.

## Method under test: `tradeWithBank(Player player, ResourceType giveResource, ResourceType receiveResource)`

Bank conversion ratio is 4:1

| Test Case | State of the System                              | Expected output                                              | Implemented?       |
|-----------|--------------------------------------------------|--------------------------------------------------------------|--------------------|
| Test 1    | Player has exactly 4 WOOD and trades for 1 BRICK | WOOD decreases to 0 and BRICK increases by 1                 | :white_check_mark: |
| Test 2    | Player has 5 SHEEP and trades for 1 ORE          | SHEEP decreases by 4 and 1 SHEEP remains; ORE increases by 1 | :white_check_mark: |
| Test 3    | Player has 3 WHEAT and tries to trade for 1 WOOD | Throws IllegalActionException; resources remain unchanged    | :white_check_mark: |
| Test 4    | giveResource and receiveResource are the same    | Throws IllegalActionException; resources remain unchanged    | :white_check_mark: |
| Test 6    | giveResource is null or receiveResource is null  | Throws IllegalArgumentException; resources remain unchanged  | :white_check_mark: |

## Method under test:

`tradeWithPlayer(Player offeringPlayer, Player receivingPlayer, Map<ResourceType, Integer> offeredResources, Map<ResourceType, Integer> requestedResources)`

| Test Case | State of the System                                                               | Expected output                                                                       | Implemented? |
|-----------|-----------------------------------------------------------------------------------|---------------------------------------------------------------------------------------|--------------|
| Test 7    | Two players trade 1-for-1 and both have exactly enough resources                  | Resources are exchanged correctly; both exact counts reach 0 for traded-away resource | :x:          |
| Test 8    | Two players trade multi-resource bundles and both have more than enough resources | Resources are exchanged correctly and surplus resources remain                        | :x:          |
| Test 9    | Offering player is short by 1 resource                                            | Throws IllegalActionException; neither player's resources change                      | :x:          |
| Test 10   | Receiving player is short by 1 resource                                           | Throws IllegalActionException; neither player's resources change                      | :x:          |
| Test 11   | offeringPlayer and receivingPlayer are the same player                            | Throws IllegalActionException; resources remain unchanged                             | :x:          |
| Test 12   | offeredResources is empty or requestedResources is empty                          | Throws IllegalActionException; resources remain unchanged                             | :x:          |
| Test 13   | A trade map contains a quantity of 0                                              | Throws IllegalArgumentException; resources remain unchanged                           | :x:          |
| Test 14   | A trade map contains a negative quantity                                          | Throws IllegalArgumentException; resources remain unchanged                           | :x:          |
| Test 15   | One trade map is null                                                             | Throws IllegalArgumentException; resources remain unchanged                           | :x:          |