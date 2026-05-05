# GameInitializer BVA

Handles new game player setup, including player count validation, player name validation, color assignment, player creation, and prepare game setup

### Method under test: `setupPlayers()`

|             | State of the System                                                       | Expected output                                                                          | Implemented? |
|-------------|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | player count = 2, names = ["Cole", "Aryan"]                               | Invalid; does not create players because Catan requires 3 to 4 players                   | :x:          |
| Test Case 2 | Player count = 3, names = ["Cole", "Aryan", "Alvin"]                      | Valid; creates 3 player objecs of the given names                                        | :x:          |
| Test Case 3 | Player count = 4, names = ["Cole", "Aryan", "Alvin", "Bennita"]           | Valid; creates 4 player objects of the given names                                       | :x:          |
| Test Case 4 | Player count = 5, names = ["Cole", "Aryan", "Alvin", "Bennita", "Chris" ] | Invalid; does not create players because the game can only be played with 3 to 4 players | :x:          |
| Test Case 5 | Player count = 3, names = ["Cole", " ", "Alvin"]                          | Invalid; does not create players because a player name is blank                          | :x:          |
| Test Case 6 | Player count = 3, names = ["Cole", "Aryan"]                               | Invalid; does not create players because number of names does not match count            | :x:          |

### Method under test: `validatePlayerCount()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |                     |                 | :x: or :white_check_mark: |

### Method under test: `validatePlayerNames()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |                     |                 | :x: or :white_check_mark: |

### Method under test: `assignColor()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |                     |                 | :x: or :white_check_mark: |

### Method under test: `prepareGame()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |                     |                 | :x: or :white_check_mark: |