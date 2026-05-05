# Player BVA 

Tracks a single player's identity and resources.

### Method under test: `getName()`

|              | State of the System        | Expected output              | Implemented? |
|--------------|----------------------------|------------------------------|--------------|
| Test Case 1  | Player name is "Bob"       | Returns the name "Bob"       | :x:          |
| Test Case 2  | Player name is "Jane Doe"  | Returns the name "Jane Doe"  | :x:          |

### Method under test: `getColor()`

|             | State of the System             | Expected output                       | Implemented? |
|-------------|---------------------------------|---------------------------------------|--------------|
| Test Case 3 | Player Bob's color is red       | Returns red for only player Bob       | :x:          |
| Test Case 4 | Player Jane Doe's color is blue | returns blue for only player Jane Doe | :x:          |

### Method under test: `getInventory()`

|             | State of the System   | Expected output                                          | Implemented? |
|-------------|-----------------------|----------------------------------------------------------|--------------|
| Test Case 5 | New player is created | Inventory contains 15 roads, 5 settlements, and 4 cities | :x:          |

### Method under test: `getVictoryPoints()`

|             | State of the System                    | Expected output | Implemented? |
|-------------|----------------------------------------|-----------------|--------------|
| Test Case 6 | New player is created                  | Return 0        | :x:          |
| Test Case 7 | Player has no buildings or bonuses yet | Return 0        | :x:          |
