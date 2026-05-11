# Hex BVA

Represents a single tile on the board.

### Method under test: `getHasRobber()`

Step 1: Domain: State of object (Hex class instance), Output: Yes/No, (we will use a Boolean datatype so we cannot have a Null or non True/False value)
Step 2: Equivalence Classes: Input: Hex class object, Output: Boolean value (True/False)
Step 3: Test Cases: True value, False value, some other value that is neither true nor false (not valid)
Step 4:

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | HasRobber = True    | True            | :white_check_mark:        |
| Test Case 2  | HasRobber = False   | False           | :white_check_mark:        |

### Method under test: `setHasRobber()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | setHasRobber(true)  | HasRobber true  | :white_check_mark:        |
| Test Case 2  | setHasRobber(false) | HasRobber false | :white_check_mark:        |

### Method under test: `getTerrainType()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | TerrainType = null  | null            | :white_check_mark:        |
| Test Case 2  | TerrainType = Forest| Forest          | :white_check_mark:        |

### Method under test: `setTerrainType()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | setTerrainType("Forest") | TerrainType Forest | :white_check_mark:   |
| Test Case 2  | setTerrainType(null) | TerrainType null | :x:                       |

### Method under test: `getTokenNumber()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | TokenNumber = 0     | 0               | :white_check_mark:        |
| Test Case 2  | TokenNumber = 2     | 2               | :white_check_mark:        |
| Test Case 3  | TokenNumber = 12    | 12              | :white_check_mark:        |

### Method under test: `setTokenNumber()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | setTokenNumber(1)   | IllegalArgumentException | :white_check_mark: |
| Test Case 2  | setTokenNumber(2)   | TokenNumber 2   | :white_check_mark:        |
| Test Case 3  | setTokenNumber(12)  | TokenNumber 12  | :white_check_mark:        |
| Test Case 4  | setTokenNumber(13)  | IllegalArgumentException | :white_check_mark: |


### Method under test: `distributeResources()`

1. Domain: State of object (Hex class instance), Output: Void, Call to player class method - for player to collect resources
2. Equivalence Classes: Input: Hex class object, Cases: Hex has robber, Hex has no robber, Hex corner nodes are empty, 1 full, all full, Output: Void, Call to player class method - for player to collect resources
3. Test Cases: Hex with no robber, Hex with robber, Hex with no terrain type ...

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  | HasRobber = false, TerrainType = Forest, 1 adjacent node occupied | Occupied node owner collects 1 Forest resource | :white_check_mark: |
| Test Case 2  | HasRobber = true, TerrainType = Forest, 1 adjacent node occupied | No player collects resources | :white_check_mark: |
| Test Case 3  | HasRobber = false, TerrainType = Forest, adjacent nodes empty | No player collects resources | :white_check_mark: |
| Test Case 4  | HasRobber = false, TerrainType = Fields, all 6 adjacent nodes occupied | Each occupied node owner collects resources | :white_check_mark: |
| Test Case 5  | HasRobber = false, TerrainType = null, 1 adjacent node occupied | No player collects resources | :white_check_mark: |
| Test Case 6  | HasRobber = false, TerrainType = Mountains, 1 adjacent city node occupied | Occupied node owner collects 2 Mountains resources | :white_check_mark: |
