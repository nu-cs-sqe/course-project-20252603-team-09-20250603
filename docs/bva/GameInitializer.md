# GameInitializer BVA

Handles new game player setup, including player count validation, player name validation, color assignment, player creation, and prepare game setup

### Method under test: `setupPlayers()`

|             | State of the System                                                       | Expected output                                                                          | Implemented? |
|-------------|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | player count = 2, names = ["Cole", "Aryan"]                               | Invalid; does not create players because Catan requires 3 to 4 players                   | :x:          |
| Test Case 2 | Player count = 3, names = ["Cole", "Aryan", "Alvin"]                      | Valid; creates 3 player objecs of the given names                                        | :x:          |
| Test Case 3 | Player count = 4, names = ["Cole", "Aryan", "Alvin", "Bennita"]           | Valid; creates 4 player objects of the given names                                       | :x:          |
| Test Case 4 | Player count = 5, names = ["Cole", "Aryan", "Alvin", "Bennita", "Chris" ] | Invalid; does not create players because the game can only be played with 3 to 4 players | :x:          |
| Test Case 5 | Player count = 3, names = ["Cole", "", "Alvin"]                           | Invalid; does not create players because a player name is blank                          | :x:          |
| Test Case 6 | Player count = 3, names = ["Cole", "Aryan"]                               | Invalid; does not create players because number of names does not match count            | :x:          |

### Method under test: `validatePlayerCount()` 
### Helper of setupPlayers()

|              | State of the System | Expected output                                       | Implemented? |
|--------------|---------------------|-------------------------------------------------------|--------------|
| Test Case 7  | Player count = 2    | Invalid; throws error "Must have 3-4 players to play" | :x:          |
| Test Case 8  | Player count = 3    | Valid; no error                                       | :x:          |
| Test Case 9  | Player count = 4    | Valid; no error                                       | :x:          |
| Test Case 10 | Player count = 5    | Invalid; throws error "Only 3-4 players to play"      | :x:          |

### Method under test: `validatePlayerNames()`
### Helper of setupPlayers()

|              | State of the System                                                                   | Expected output               | Implemented? |
|--------------|---------------------------------------------------------------------------------------|-------------------------------|--------------|
| Test Case 11 | Player count = 3, names = ["Cole", "Aryan", "Alvin"]                                  | Valid; no errors              | :x:          |
| Test Case 12 | Player count = 4, names = ["Cole", "Aryan", "Alvin", "Bennita"]                       | Valid; no errors              | :x:          |
| Test Case 13 | Player count = 3, names = ["Cole", "Aryan"]                                           | Invalid; not enough names     | :x:          |
| Test Case 14 | Player count = 3, names = ["Cole", "Aryan", "Alvin", "Bennita"]                       | Invalid; too many names       | :x:          |
| Test Case 15 | Player count = 3, names = ["Cole", "", "Alvin"]                                       | Invalid; blank name           | :x:          |
| Test Case 16 | Player count = 3, names = ["Cole", " ", "Alvin"]                                      | Invalid; whitespace only name | :x:          |
| Test Case 17 | Player count = 3, names = ["Cole", "Aryan", "Alvin Li"]                               | Valid; no errors              | :x:          |
| Test Case 18 | Player count = 3, names = ["Cole Lu", "This is my name", "I want this to be my name"] | Valid; no errors              | :x:          |

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