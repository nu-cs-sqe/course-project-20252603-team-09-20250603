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
| Test Case 7 | Player count = 3, names = ["Cole", "Aryan"]                               | Invalid; not enough names                                                                | :x:          |
| Test Case 8 | Player count = 3, names = ["Cole", "Aryan", "Alvin", "Bennita"]           | Invalid; too many names                                                                  | :x:          |
### Method under test: `validatePlayerCount()` 
### Helper of setupPlayers()

|              | State of the System | Expected output                                       | Implemented?      |
|--------------|---------------------|-------------------------------------------------------|-------------------|
| Test Case 9  | Player count = 2    | Invalid; throws error "Must have 3-4 players to play" | :white_checkmark: |
| Test Case 10 | Player count = 3    | Valid; no error                                       | :white_checkmark: |
| Test Case 11 | Player count = 4    | Valid; no error                                       | :white_checkmark: |
| Test Case 12 | Player count = 5    | Invalid; throws error "Only 3-4 players to play"      | :white_checkmark: |

### Method under test: `validatePlayerName()`
### Helper of setupPlayers()

|              | State of the System                                                                   | Expected output               | Implemented? |
|--------------|---------------------------------------------------------------------------------------|-------------------------------|--------------|
| Test Case 13 | Name = "Cole"                                                                         | Valid; no errors              | :x:          |
| Test Case 14 | Names = "Alvin Li"                                                                    | Valid; no errors              | :x:          |
| Test Case 15 | Names = ""                                                                            | Invalid; blank name           | :x:          |
| Test Case 16 | Names = " "                                                                           | Invalid; whitespace only name | :x:          |
| Test Case 17 | Name = null                                                                           | Invalid; name cannot be null  | :x:          |
### Method under test: `assignColor()`

|              | State of the System | Expected output                        | Implemented? |
|--------------|---------------------|----------------------------------------|--------------|
| Test Case 18 | Player index = 0    | Assigns just first player with red     | :x:          |
| Test Case 19 | Player index = 1    | Assigns just second player with blue   | :x:          |
| Test Case 20 | Player index = 2    | Assigns just third player with orange  | :x:          |
| Test Case 21 | Player index = 4    | Assigns just fourth player with white  | :x:          |
| Test Case 22 | Total of 4 players  | All players are assigned unqiue colors | :x:          |
