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


