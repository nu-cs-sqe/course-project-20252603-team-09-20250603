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

### Method under test: `public Map<ResourceType, Integer> getAdjacentResources(int nodeId)`

|          | Step 1                              | Step 2   | Step 3                                                                                             |
|----------|-------------------------------------|----------|----------------------------------------------------------------------------------------------------|
| Input 1  | Node occupant (state of node)       | Cases    | - Node object, null                                                                                |
| Input 2  | Node id                             | Interval | - MIN (0), MIN -1, MAX (53), MAX + 1                                                               |
| Input 3  | Adjacent number of hexes            | Interval | - MIN (1), MAX (3)                                                                                 |
| Output 1 | Signaling of unsuccessful operation | Cases    | -  IllegalStateException, "The node object is not valid" or "The node object is null"              |
| Output 2 | Type of resources                   | Cases    | -  ResourceType.WOOD, ResourceType.BRICK, ResourceType.SHEEP, ResourceType.WHEAT, ResourceType.ORE |
| Output 3 | Quantity of resources               | Interval | - MIN (1), MAX (3)                                                                                 |


### Step 4
