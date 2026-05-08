# GameInitializer BVA

Handles new game player setup, including player count validation, player name validation, color assignment, player creation, and prepare game setup

### Method under test: `setupPlayers()`

|             | State of the System                                                       | Expected output                                                                          | Implemented? |
|-------------|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | player count = 2, names = ["Cole", "Aryan"]                               | Invalid; does not create players because Catan requires 3 to 4 players                   | :x:          |
| Test Case 2 | Player count = 3, names = ["Cole", "Aryan", "Alvin"]                      | Valid; creates 3 player objects of the given names and assigns unique colors             | :x:          |
| Test Case 3 | Player count = 4, names = ["Cole", "Aryan", "Alvin Li", "Bennita"]        | Valid; creates 4 player objects of the given names and assigns unique colors             | :x:          |
| Test Case 4 | Player count = 5, names = ["Cole", "Aryan", "Alvin", "Bennita", "Chris" ] | Invalid; does not create players because the game can only be played with 3 to 4 players | :x:          |
| Test Case 5 | Player count = 4, names = ["Cole", "", "Alvin"]                           | Invalid; does not create players because name is blank                                   | :x:          |
| Test Case 6 | Player count = 0, names = null                                            | Invalid; does not create players because input is null                                   | :x:          |
| Test Case 7 | Duplicate name in names  input = ["Cole", "Alvin", "Alvin"]               | Invalid; does not create players because names should be unique                          | :x:          |

### Method under test: `validatePlayerCount()` 
### Helper of setupPlayers()

|              | State of the System | Expected output                                        | Implemented?      |
|--------------|---------------------|--------------------------------------------------------|-------------------|
| Test Case 9  | Player count = 2    | Invalid; throws error "Catan requires 3 to 4 players." | :white_checkmark: |
| Test Case 10 | Player count = 3    | Valid; no error                                        | :white_checkmark: |
| Test Case 11 | Player count = 4    | Valid; no error                                        | :white_checkmark: |
| Test Case 12 | Player count = 5    | Invalid; throws error "Catan requires 3 to 4 players." | :white_checkmark: |

### Method under test: `validatePlayerName()`
### Helper of setupPlayers()

|              | State of the System | Expected output               | Implemented?       |
|--------------|---------------------|-------------------------------|--------------------|
| Test Case 13 | Name = "Cole"       | Valid; no errors              | :white_checkmark:  |
| Test Case 14 | Name = "Alvin Li"   | Valid; no errors              | :white_checkmark:  |
| Test Case 15 | Name = ""           | Invalid; blank name           | :white_checkmark:  |
| Test Case 16 | Name = " "          | Invalid; whitespace only name | :white_checkmark:  |
| Test Case 17 | Name = null         | Invalid; name cannot be null  | :white_checkmark:  |
### Method under test: `assignColor()`

|              | State of the System | Expected output                        | Implemented?       |
|--------------|---------------------|----------------------------------------|--------------------|
| Test Case 18 | Player index = 0    | Assigns just first player with red     | :white_checkmark:  |
| Test Case 19 | Player index = 1    | Assigns just second player with blue   | :white_checkmark:  |
| Test Case 20 | Player index = 2    | Assigns just third player with orange  | :white_checkmark:  |
| Test Case 21 | Player index = 3    | Assigns just fourth player with white  | :white_checkmark:  |
| Test Case 22 | Player index = 4    | Invalid, no fifth player should exist  | :white_checkmark:  |
| Test Case 23 | Total of 4 players  | All players are assigned unique colors | :white_checkmark:  |
