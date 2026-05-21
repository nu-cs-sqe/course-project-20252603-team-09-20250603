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

|             | State of the System                    | Expected output | Implemented? |
|-------------|----------------------------------------|-----------------|--------------|
| Test Case 6 | New player is created                  | Return 0        |:white_check_mark:|
