*****# BVA Analysis for Board.md


### Steps
- Step 0: Understand problem
- Step 1: Identify all input spaces and output spaces
- Step 2: Identify data type used in BVA Catalog
- Step 3: Select concrete values using BVA Catalog

### Method under test: `public List<Hex> getHexesFromNode(Node node)`
*Returns list of Hex adjacent to each Node.*

|          | Step 1                              | Step 2   | Step 3                                                                                |
|----------|-------------------------------------|----------|---------------------------------------------------------------------------------------|
| Input 1  | Node occupant (state of node)       | Cases    | - Node object, null                                                                   |
| Input 2  | Node id                             | Interval | - MIN (0), MIN -1, MAX (53), MAX + 1                                                  |
| Output 1 | Adjacent number of Hexes            | Interval | - MIN (1), MAX (3)                                                                    |
| Output 2 | Signaling of unsuccessful operation | Cases    | -  IllegalStateException, "The node object is not valid" or "The node object is null" |
| Output 3 | Hex id                              | Interval | -  MIN(0), MAX(18)                                                                    |
### Step 4

| Test    | System under test              | Expected output       | Implemented?       |
|---------|--------------------------------|-----------------------|--------------------|
| Test 1  | Node object 0, 1 adjacent hex  | hex #0                | :white_check_mark: |
| Test 2  | Node object 9, 3 adjacent hex  | Hex #3, 0, 4          | :white_check_mark: |
| Test 3  | null node object               | IllegalStateException | :white_check_mark: |
| Test 4  | Node object -1                 | IllegalStateException | :white_check_mark: |
| Test 5  | Node object 54                 | IllegalStateException | :white_check_mark: |
| Test 6  | Node object 53, 1 adjacent hex | hex #18               | :white_check_mark: |

### Method under test: `public Map<ResourceType, Integer> getAdjacentResources(Node node)`

|          | Step 1                                   | Step 2     | Step 3                                                                                                                                                                                                                            |
|----------|------------------------------------------|------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Input 1  | Node occupant (state of node)            | Cases      | - Node object, null                                                                                                                                                                                                               |
| Input 2  | Node id                                  | Interval   | - MIN (0), MIN -1, MAX (53), MAX + 1                                                                                                                                                                                              |
| Input 3  | Adjacent number of hexes                 | Interval   | - MIN (1), MAX (3)                                                                                                                                                                                                                |
| Output 1 | Signaling of unsuccessful operation      | Cases      | -  IllegalStateException, "The node object is not valid" or "The node object is null"                                                                                                                                             |
| Output 2 | Type of resources                        | Cases      | -  ResourceType.WOOD, ResourceType.BRICK, ResourceType.SHEEP, ResourceType.WHEAT, ResourceType.ORE                                                                                                                                |
| Output 3 | Quantity of resources                    | Interval   | - MIN (1), MAX (3)                                                                                                                                                                                                                |
| Output 4 | The state of the map                     | Collection | - ~~empty collection<br/>~~- contains exactly one element<br/>- contains more than one element<br/>- ~~max list size~~<br/>- ~~contains duplicate elements<br/>-~~ ~~using the first element~~<br/>- ~~using the last element~~   |


### Step 4

| Test    | System under test          | Expected output               | Implemented?       |
|---------|----------------------------|-------------------------------|--------------------|
| Test 1  | Node Id 0, 1 hex adjacent  | Brick: 1                      | :white_check_mark: |
| Test 2  | Node Id 53, 1 hex adjacent | Ore: 1                        | :white_check_mark: |
| Test 3  | Node Id -1                 | IllegalStateException         | :white_check_mark: |
| Test 4  | Node Id 54                 | IllegalStateException         | :white_check_mark: |
| Test 5  | Node Id 2, 2 hex adjacent  | Brick: 1, wood: 1             | :white_check_mark: |
| Test 6  | Node Id 10, 3 hex adjacent | Brick: 1, wood: 2             | :white_check_mark: |
| Test 7  | Node Id 44, 3 hex adjacent | Brick: 1, wheat: 1, ore: 1    | :white_check_mark: |
| Test 8  | Node Id 19, 3 hex adjacent | wood: 1, wheat: 1, ore: 1     | :white_check_mark: |
| Test 9  | Node Id 40, 3 hex adjacent | sheep: 3                      | :white_check_mark: |
| Test 10 | Node Id 32, 3 hex adjacent | desert: 1, sheep: 1, brick: 1 | :white_check_mark: |

### Method under test: `private void buildEdges`
| Test    | System under test                                    | Expected output             | Implemented? |
|---------|------------------------------------------------------|-----------------------------|-------------|
| Test 1  | Initialize board and nodes, there should be 72 edges | Length of board.edges == 72 | :white_check_mark:          |


### Method under test: `getEdgesConnectedToNode(Node node)`

|              | State of the System                         | Expected output / behavior                    | Implemented? |
|--------------|---------------------------------------------|------------------------------------------------|----------|
| Test Case 1  | Node is null                                | Throws `IllegalArgumentException`              | :white_check_mark: |
| Test Case 2  | Node is not part of the board               | Throws `IllegalStateException`                 | :white_check_mark: |
| Test Case 3  | Valid node has connected edges              | Returns all edges connected to that node       | :white_check_mark: |
| Test Case 4  | Valid edge node on board boundary           | Returns only the connected boundary edges      | :white_check_mark: |


### Method under test: `getEdge(int edgeId)`

|              | State of the System                  | Expected output / behavior        | Implemented? |
|--------------|--------------------------------------|-----------------------------------|----------|
| Test Case 1  | Edge ID is 0                         | Returns edge with ID 0            | :white_check_mark: |
| Test Case 2  | Edge ID is the last valid edge ID    | Returns edge with that ID         | :white_check_mark: |
| Test Case 3  | Edge ID is -1                        | Throws `IllegalArgumentException` | :white_check_mark: |
| Test Case 4  | Edge ID is equal to number of edges  | Throws `IllegalArgumentException` | :white_check_mark: |

### Method under test: `getHex(int hexId)`

|              | State of the System                | Expected output / behavior        | Implemented? |
|--------------|------------------------------------|-----------------------------------|----------|
| Test Case 5  | Hex ID is 0                        | Returns hex with ID 0             | :white_check_mark: |
| Test Case 6  | Hex ID is the last valid hex ID    | Returns hex with that ID          | :white_check_mark: |
| Test Case 7  | Hex ID is -1                       | Throws `IllegalArgumentException` | :white_check_mark: |
| Test Case 8  | Hex ID is equal to number of hexes | Throws `IllegalArgumentException` | :white_check_mark: |

### Method under test: `getNode(int nodeId)`

|              | State of the System                  | Expected output / behavior        | Implemented?       |
|--------------|--------------------------------------|-----------------------------------|--------------------|
| Test Case 9  | Node ID is 0                         | Returns node with ID 0            | :white_check_mark: |
| Test Case 10 | Node ID is the last valid node ID    | Returns node with that ID         | :white_check_mark: |
| Test Case 11 | Node ID is -1                        | Throws `IllegalArgumentException` | :white_check_mark: |
| Test Case 12 | Node ID is equal to number of nodes  | Throws `IllegalArgumentException` | :white_check_mark: |

### Method under test: `getNode(int nodeId)`

|              | State of the System                  | Expected output / behavior        | Implemented? |
|--------------|--------------------------------------|-----------------------------------|----------|
| Test Case 9  | Node ID is 0                         | Returns node with ID 0            | :white_check_mark: |
| Test Case 10 | Node ID is the last valid node ID    | Returns node with that ID         | :white_check_mark: |
| Test Case 11 | Node ID is -1                        | Throws `IllegalArgumentException` | :white_check_mark: |
| Test Case 12 | Node ID is equal to number of nodes  | Throws `IllegalArgumentException` | :white_check_mark: |


### Method under test: `distributeResourcesOnRoll(int diceRoll)`
|              | State of the System                                                    | Expected output / behavior                      | Implemented?       |
|--------------|------------------------------------------------------------------------|-------------------------------------------------|--------------------|
| Test Case 1  | Dice roll is 2 (minimum valid value) and matching hex has a settlement | Settlement owner receives 1 resource            | :white_check_mark: |
| Test Case 2  | Dice roll is 12 (maximum valid value) and matching hex has a city      | City owner receives 2 resources                 | :white_check_mark: |
| Test Case 3  | Dice roll is 1                                                         | Throws `IllegalArgumentException`               | :white_check_mark: |
| Test Case 4  | Dice roll is 13                                                        | Throws `IllegalArgumentException`               | :x:                |
| Test Case 5  | Dice roll matches no hex token numbers                                 | No resources distributed                        | :x:                |
| Test Case 6  | Dice roll matches a hex with no occupied adjacent nodes                | No resources distributed                        | :x:                |
| Test Case 7  | Dice roll matches multiple producing hexes                             | Resources distributed from all matching hexes   | :x:                |
| Test Case 8  | Dice roll matches a hex adjacent to multiple occupied nodes            | All eligible players receive resources          | :x:                |
| Test Case 9  | Dice roll matches a hex containing the robber                          | No resources distributed from that hex          | :x:                |
| Test Case 10 | Dice roll matches a multiple hexes with multiple occupied nodes each   | All eligible players receive resources          | :x:                |
