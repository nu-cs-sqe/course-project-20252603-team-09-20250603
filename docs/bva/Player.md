# Player BVA 

Tracks a single player's identity and resources.

### Method under test: `getName()`

|              | State of the System        | Expected output              | Implemented? |
|--------------|----------------------------|------------------------------|--------------|
| Test Case 1  | Player name is "Bob"       | Returns the name "Bob"       | :white_check_mark:|
| Test Case 2  | Player name is "Jane Doe"  | Returns the name "Jane Doe"  | :white_check_mark:|

### Method under test: `getColor()`

|             | State of the System             | Expected output                       | Implemented? |
|-------------|---------------------------------|---------------------------------------|--------------|
| Test Case 3 | Player Bob's color is red       | Returns red for only player Bob       | :white_check_mark:|
| Test Case 4 | Player Jane Doe's color is blue | returns blue for only player Jane Doe | :white_check_mark:|

### Method under test: `getInventory()`

|             | State of the System   | Expected output                                          | Implemented? |
|-------------|-----------------------|----------------------------------------------------------|--------------|
| Test Case 5 | New player is created | Inventory contains 15 roads, 5 settlements, and 4 cities | :white_check_mark:|

### Method under test: `getVictoryPoints()`

|             | State of the System                    | Expected output | Implemented?       |
|-------------|----------------------------------------|-----------------|--------------------|
| Test Case 6 | New player is created                  | Return 0        | :white_check_mark: |


## Method under test: `public void addResources(Map<ResourceType, Integer>)`

|          | Step 1                              | Step 2     | Step 3                                                                                                                                                                                                                      |
|----------|-------------------------------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Input 1  | Type of resources                   | Cases      | - ResourceType.WOOD, ResourceType.BRICK, ResourceType.SHEEP, ResourceType.WHEAT, ResourceType.ORE, ResourceType.DESERT                                                                                                      |
| Input 2  | Quantity of resources               | Interval   | - MIN (1), MAX (19)                                                                                                                                                                                                         |
| Input 3  | The state of the map resourceHand   | Collection | -  empty collection<br/>- contains exactly one element<br/>- contains more than one element<br/>- ~~max list size~~<br/>- contains duplicate elements<br/>- ~~using the first element~~<br/>- ~~using the last element~~    |
| Input 4  | The state of the input  map         | Collection | - ~~empty collection<br/>~~- contains exactly one element<br/>- contains more than one element<br/>- ~~max list size~~<br/>- contains duplicate elements<br/>- ~~using the first element~~<br/>- ~~using the last element~~ |
| Output 1 | Signaling of unsuccessful operation | Cases      | -  IllegalStateException, "The map object is null"                                                                                                                                                                          |
| Output 2 | The state of the map                | Collection | - empty collection<br/> - contains exactly one element<br/>- contains more than one element<br/>- ~~max list size~~<br/>- contains duplicate elements<br/>- ~~using the first element~~<br/>- ~~using the last element~~    |


### Step 4

| Test   | System under test                             | Expected output           | Implemented?       |
|--------|-----------------------------------------------|---------------------------|--------------------|
| Test 1 | Start with empty hand, add 1 wood             | Wood: 1                   | :x:                |
| Test 2 | Start with empty hand, add 19 brick           | Brick: 19                 | :x:                |
| Test 3 | Start with empty hand, add 1 sheep 1 wheat    | Sheep: 1, Wheat: 1        | :x:                |
| Test 4 | Start with empty hand, add desert             | empty collection          | :x:                |
| Test 5 | Start with 1 ore, add 1 sheep 1 ore           | Sheep: 1, ore: 2          | :x:                |
| Test 6 | Start with 2 brick, add desert                | Brick: 2                  | :x:                |
| Test 7 | Start with 2 wood, add 2 wood, 1 ore, 2 brick | Wood: 4, ore: 1, brick: 2 | :x:                |
| Test 8 | Start with  brick, add desert                 | Brick: 1                  | :x:                |
| Test 9 | Start with  brick, add null                   | IllegalArgumentException  | :x:                |

