# Player BVA

Tracks a single player's identity, infrastructure inventory, and victory points.

---
`getID`
Getter to pass to controller - untested as simply acts as a simple getter

### Method under test: `getName()`

|             | State of the System         | Expected output      | Implemented? |
|-------------|-----------------------------|----------------------|--------------|
| Test Case 1 | Player name is `"Bob"`      | Returns `"Bob"`      | :white_check_mark: |
| Test Case 2 | Player name is `"Jane Doe"` | Returns `"Jane Doe"` | :white_check_mark: |

---

### Method under test: `getColor()`

|             | State of the System             | Expected output                  | Implemented? |
|-------------|---------------------------------|----------------------------------|--------------|
| Test Case 3 | Player Bob's color is red       | Returns red for player Bob       | :white_check_mark: |
| Test Case 4 | Player Jane Doe's color is blue | Returns blue for player Jane Doe | :white_check_mark: |

---

### Method under test: `getInventory()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--|
| Test Case 5  | New player is created | Inventory contains 15 roads, 5 settlements, and 4 cities | :white_check_mark: |
| Test Case 6  | Player uses one road | Inventory returns 14 roads, 5 settlements, and 4 cities | :white_check_mark: |
| Test Case 7  | Player uses one settlement | Inventory returns 15 roads, 4 settlements, and 4 cities | :white_check_mark: |
| Test Case 8  | Player uses one city | Inventory returns 15 roads, 5 settlements, and 3 cities | :white_check_mark: |
| Test Case 9  | Player uses one road, one settlement, and one city | Inventory returns 14 roads, 4 settlements, and 3 cities | :white_check_mark: |
| Test Case 10 | Caller modifies the map returned by `getInventory()` | Player's actual inventory does not change | :white_check_mark: |

---

### Method under test: `useInventoryItem(String item)`

|              | State of the System | Expected output / behavior | Implemented? |
|--------------|---------------------|-----------------------------|--|
| Test Case 11 | Player has 15 roads and uses one road | Road inventory decreases from 15 to 14 | :white_check_mark: |
| Test Case 12 | Player has 5 settlements and uses one settlement | Settlement inventory decreases from 5 to 4 | :white_check_mark: |
| Test Case 13 | Player has 4 cities and uses one city | City inventory decreases from 4 to 3 | :white_check_mark: |
| Test Case 14 | Player has exactly 1 item remaining and uses that item | Item inventory decreases from 1 to 0 | :white_check_mark: |
| Test Case 15 | Player has 0 of an item remaining and tries to use it | Throws `IllegalStateException` and inventory stays at 0 | :white_check_mark: |
| Test Case 16 | Player tries to use an invalid inventory item, such as `"ships"` | Throws `IllegalStateException` and inventory does not change | :white_check_mark: |

---

### Method under test: `getVictoryPoints()`

|             | State of the System                    | Expected output | Implemented?       |
|-------------|----------------------------------------|-----------------|--------------------|
| Test Case 6 | New player is created                  | Return 0        | :white_check_mark: |

---
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
| Test 1 | Start with empty hand, add 1 wood             | Wood: 1                   | :white_check_mark: |
| Test 2 | Start with empty hand, add 19 brick           | Brick: 19                 | :white_check_mark: |
| Test 3 | Start with empty hand, add 1 sheep 1 wheat    | Sheep: 1, Wheat: 1        | :white_check_mark: |
| Test 4 | Start with empty hand, add desert             | empty collection          | :white_check_mark: |
| Test 5 | Start with 1 ore, add 1 sheep 1 ore           | Sheep: 1, ore: 2          | :white_check_mark: |
| Test 6 | Start with 2 brick, add desert                | Brick: 2                  | :white_check_mark: |
| Test 7 | Start with 2 wood, add 2 wood, 1 ore, 2 brick | Wood: 4, ore: 1, brick: 2 | :white_check_mark: |
| Test 8 | Start with  brick, add desert                 | Brick: 1                  | :white_check_mark: |
| Test 9 | Start with  brick, add null                   | IllegalArgumentException  | :white_check_mark: |

### Method under test: `hasResources(Map<ResourceType, Integer> cost)`

|              | State of the System                                      | Expected output / behavior                                      | Implemented? |
|--------------|----------------------------------------------------------|------------------------------------------------------------------|--------------|
| Test Case 26 | Player has exactly the required resources                | Returns `true`                                                   | :white_check_mark: |
| Test Case 27 | Player has more than the required resources              | Returns `true`                                                   | :white_check_mark: |
| Test Case 28 | Player is missing one required resource                  | Returns `false`                                                  | :white_check_mark: |
| Test Case 29 | Player has the resource, but not enough of it            | Returns `false`                                                  | :white_check_mark: |
| Test Case 30 | Cost map is empty                                        | Returns `true`                                                   | :white_check_mark: |
| Test Case 31 | Cost map is null                                         | Throws `IllegalArgumentException`                                | :white_check_mark: |

### Method under test: `useResources(Map<ResourceType, Integer> cost)`

|              | State of the System                                      | Expected output / behavior                                      | Implemented? |
|--------------|----------------------------------------------------------|------------------------------------------------------------------|--------------|
| Test Case 32 | Player has exactly the required resources and uses them  | Required resources decrease to 0                                 | :white_check_mark: |
| Test Case 33 | Player has more than the required resources and uses them | Resources decrease by the cost amount                           | :white_check_mark: |
| Test Case 34 | Player is missing one required resource                  | Throws `IllegalStateException` and resources remain unchanged    | :white_check_mark: |
| Test Case 35 | Player has the resource, but not enough of it            | Throws `IllegalStateException` and resources remain unchanged    | :white_check_mark: |
| Test Case 36 | Cost map is empty                                        | Resources remain unchanged                                       | :white_check_mark: |
| Test Case 37 | Cost map is null                                         | Throws `IllegalArgumentException`                                | :white_check_mark: |

### Method under test: `addVictoryPoints(int points)`

|              | State of the System                 | Expected output / behavior         | Implemented? |
|--------------|-------------------------------------|------------------------------------|----------|
| Test Case 38 | New player receives 1 victory point | Victory points increase from 0 to 1 | :white_check_mark:       |
| Test Case 39 | Player with 1 point receives 1 more | Victory points increase from 1 to 2 | :white_check_mark:       |
| Test Case 40 | Player receives 0 victory points    | Victory points stays the same      | :white_check_mark:       |
| Test Case 41 | Player receives negative points     | Throws `IllegalAgrumentException`  | :white_check_mark:       |

### Method under test: `removeVictoryPoints(int points)`

|              | State of the System                                  | Expected output / behavior        | Implemented? |
|--------------|------------------------------------------------------|-----------------------------------|--------------|
| Test Case 1  | Player has 2 points and loses 1 point                | Victory points become 1           | :x: |
| Test Case 2  | Player has 2 points and loses exactly 2 points       | Victory points become 0           | :x: |
| Test Case 3  | Player has 2 points and loses 0 points               | Victory points stay 2             | :x: |
| Test Case 4  | Player tries to lose negative points                 | Throws `IllegalArgumentException` | :x: |
| Test Case 5  | Player tries to lose more points than they have      | Victory points become 0           | :x: |


### Method under test: `removeRandomCard()`

|               | State of the System                                                    | Expected output / behavior                                        | Implemented?       |
|---------------|------------------------------------------------------------------------|-------------------------------------------------------------------|--------------------|
| Test Case 1   | ResourceHand is empty                                                  | Throws `IllegalStateException`                                    | :white_check_mark: |
| Test Case 2   | ResourceHand has exactly 1 wood                                        | Returns `WOOD`; hand becomes empty                                | :white_check_mark: |
| Test Case 3   | ResourceHand has exactly 1 brick                                       | Returns `BRICK`; hand becomes empty                               | :white_check_mark: |
| Test Case 4   | ResourceHand has 2 wood                                                | Returns `WOOD`; wood count decreases to 1                         | :white_check_mark: |
| Test Case 5   | ResourceHand has 1 wood and 1 brick                                    | Returns either `WOOD` or `BRICK`; total card count decreases by 1 | :white_check_mark: |
| Test Case 6   | ResourceHand has multiple resource types                               | Returned resource is one of the resources originally in the hand  | :white_check_mark: |
| Test Case 7   | ResourceHand has multiple cards of one type and other resource types   | Only the returned resource count decreases by 1                   | :x:                |
| Test Case 8   | ResourceHand contains desert only                                      | Throws `IllegalStateException`                                    | :x:                |
| Test Case 9   | ResourceHand contains desert and valid resources                       | Desert is never returned; one valid resource is removed           | :x:                |
| Test Case 10  | `removeRandomCard()` is called multiple times on a non-empty hand      | Resource counts update cumulatively across calls                  | :x:                |
