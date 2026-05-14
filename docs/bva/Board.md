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

| Test    | System under test          | Expected output               | Implemented?              |
|---------|----------------------------|-------------------------------|---------------------------|
| Test 1  | Node Id 0, 1 hex adjacent  | Brick: 1                      | :white_check_mark:        |
| Test 2  | Node Id 53, 1 hex adjacent | Ore: 1                        | :white_check_mark:        |
| Test 3  | Node Id -1                 | IllegalStateException         | :white_check_mark:        |
| Test 4  | Node Id 54                 | IllegalStateException         | :white_check_mark:        |
| Test 5  | Node Id 2, 2 hex adjacent  | Brick: 1, wood: 1             | :white_check_mark:        |
| Test 6  | Node Id 10, 3 hex adjacent | Brick: 1, wood: 2             | :white_check_mark:        |
| Test 7  | Node Id 44, 3 hex adjacent | Brick: 1, wheat: 1, ore: 1    | :white_check_mark:        |
| Test 8  | Node Id 19, 3 hex adjacent | wood: 1, wheat: 1, ore: 1     | :x: or :white_check_mark: |
| Test 9  | Node Id 40, 3 hex adjacent | sheep: 3                      | :x: or :white_check_mark: |
| Test 10 | Node Id 32, 3 hex adjacent | desert: 1, sheep: 1, brick: 1 | :x: or :white_check_mark: |


