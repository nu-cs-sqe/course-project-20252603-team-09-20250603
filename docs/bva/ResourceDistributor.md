
******# BVA Analysis for ResourceDistributor.md


### Steps
- Step 0: Understand problem
- Step 1: Identify all input spaces and output spaces
- Step 2: Identify data type used in BVA Catalog
- Step 3: Select concrete values using BVA Catalog

### Method under test: `public Map<ResourceType, Integer> getAdjacentResources(int nodeId)`
*Returns a map of type of resource to number of resources to count of resource for every hex adjacent to the node with the given nodeId*

|          | Step 1                                     | Step 2     | Step 3                                                                                                                                                                                                                          |
|----------|--------------------------------------------|------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Input 1  | The nodeId                                 | Interval   | - MIN (0), MAX (53), MAX+1, MIN-1                                                                                                                                                                                               |
| Input 2  | The number of adjacent hexes to given node | Interval   | - MIN (1), MAX (3)                                                                                                                                                                                                              |
| Output 1 | The state of the map                       | Collection | - ~~empty collection<br/>~~- contains exactly one element<br/>- contains more than one element<br/>- ~~max list size~~<br/>- ~~contains duplicate elements<br/>-~~ ~~using the first element~~<br/>- ~~using the last element~~ |
| Output 2 | Type of resource                           | Case       | - ResourceType.WOOD, ResourceType.BRICK, ResourceType.SHEEP, ResourceType.WHEAT, ResourceType.ORE, ResourceType.Desert                                                                                                          |
| Output 3 | Count of each resource                     | Interval   | - MIN (1), MAX (3)                                                                                                                                                                                                              |
| Output 4 | Signaling of unsuccessful operation        | Cases      | - IllegalStateException, "The node id does exist."                                                                                                                                                                              |

### Step 4

| Test    | System under test          | Expected output               | Implemented?               |
|---------|----------------------------|-------------------------------|----------------------------|
| Test 1  | Node Id 0, 1 hex adjacent  | Brick: 1                      | :x: or :white_check_mark:  |
| Test 2  | Node Id 53, 1 hex adjacent | Ore: 1                        | :x: or :white_check_mark:  |
| Test 3  | Node Id -1                 | IllegalStateException         | :x: or :white_check_mark:  |
| Test 4  | Node Id 54                 | IllegalStateException         | :x: or :white_check_mark:  |
| Test 5  | Node Id 2, 2 hex adjacent  | Brick: 1, wood: 1             | :x: or :white_check_mark:  |
| Test 6  | Node Id 10, 3 hex adjacent | Brick: 1, wood: 2             | :x: or :white_check_mark:  |
| Test 7  | Node Id 9, 3 hex adjacent  | Brick: 1, wheat: 1, ore: 1    | :x: or :white_check_mark:  |
| Test 8  | Node Id 19, 3 hex adjacent | wood: 1, wheat: 1, ore: 1     | :x: or :white_check_mark:  |
| Test 9  | Node Id 40, 3 hex adjacent | sheep: 3                      | :x: or :white_check_mark:  |
| Test 10 | Node Id 32, 3 hex adjacent | desert: 1, sheep: 1, brick: 1 | :x: or :white_check_mark:  |

