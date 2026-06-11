# GameInitializer BVA

Handles new game player setup, including player count validation, player name validation, color assignment, player creation, and prepare game setup

### Method under test: `setupPlayers()`

|             | State of the System                                                       | Expected output                                                                          | Implemented? |
|-------------|---------------------------------------------------------------------------|------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | player count = 2, names = ["Cole", "Aryan"]                               | Invalid; does not create players because Catan requires 3 to 4 players                   | :white_check_mark:|
| Test Case 2 | Player count = 3, names = ["Cole", "Aryan", "Alvin"]                      | Valid; creates 3 player objects of the given names and assigns unique colors             | :white_check_mark:|
| Test Case 3 | Player count = 4, names = ["Cole", "Aryan", "Alvin Li", "Bennita"]        | Valid; creates 4 player objects of the given names and assigns unique colors             | :white_check_mark:|
| Test Case 4 | Player count = 5, names = ["Cole", "Aryan", "Alvin", "Bennita", "Chris" ] | Invalid; does not create players because the game can only be played with 3 to 4 players | :white_check_mark:|
| Test Case 5 | Player count = 4, names = ["Cole", "", "Alvin"]                           | Invalid; does not create players because name is blank                                   | :white_check_mark:|
| Test Case 6 | Player count = 0, names = null                                            | Invalid; does not create players because input is null                                   | :white_check_mark:|
| Test Case 7 | Duplicate name in names  input = ["Cole", "Alvin", "Alvin"]               | Invalid; does not create players because names should be unique                          | :white_check_mark:|

### Method under test: `validatePlayerCount()` 
### Helper of setupPlayers()

|              | State of the System | Expected output                                        | Implemented?      |
|--------------|---------------------|--------------------------------------------------------|-------------------|
| Test Case 9  | Player count = 2    | Invalid; throws error "Catan requires 3 to 4 players." | :white_check_mark: |
| Test Case 10 | Player count = 3    | Valid; no error                                        | :white_check_mark: |
| Test Case 11 | Player count = 4    | Valid; no error                                        | :white_check_mark: |
| Test Case 12 | Player count = 5    | Invalid; throws error "Catan requires 3 to 4 players." | :white_check_mark: |

### Method under test: `validatePlayerName()`
### Helper of setupPlayers()

|              | State of the System | Expected output               | Implemented?       |
|--------------|---------------------|-------------------------------|--------------------|
| Test Case 13 | Name = "Cole"       | Valid; no errors              | :white_check_mark:  |
| Test Case 14 | Name = "Alvin Li"   | Valid; no errors              | :white_check_mark:  |
| Test Case 15 | Name = ""           | Invalid; blank name           | :white_check_mark:  |
| Test Case 16 | Name = " "          | Invalid; whitespace only name | :white_check_mark:  |
| Test Case 17 | Name = null         | Invalid; name cannot be null  | :white_check_mark:  |

### Method under test: `assignColor(int index)`

|              | State of the System | Expected output                              | Implemented? |
|--------------|---------------------|----------------------------------------------|--------------|
| Test Case 18 | Player index = 0    | Returns `RED`                                | :white_check_mark: |
| Test Case 19 | Player index = 1    | Returns `BLUE`                               | :white_check_mark: |
| Test Case 20 | Player index = 2    | Returns `ORANGE`                             | :white_check_mark: |
| Test Case 21 | Player index = 3    | Returns `WHITE`                              | :white_check_mark: |
| Test Case 22 | Player index = -1   | Throws `IllegalArgumentException`            | :white_check_mark: |
| Test Case 23 | Player index = 4    | Throws `IllegalArgumentException`            | :white_check_mark: |
| Test Case 24 | Four players are created | All players receive unique colors       | :white_check_mark: |

### Method under test: `setupGame()`

|              | State of the System          | Expected output                                                                       | Implemented?       |
|--------------|------------------------------|---------------------------------------------------------------------------------------|--------------------|
| Test Case 24 | Valid list of 3 player names | Returns a non-null `Game` in `SETUP` phase with 3 players, a board, and a turn manager | :white_check_mark:  |
