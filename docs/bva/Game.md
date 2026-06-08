# Game BVA

Handles the progression of game

### Method under test: `handleMoveRobber()`

|             | State of the System                         | Expected output / behavior                                 | Implemented?       |
|-------------|---------------------------------------------|------------------------------------------------------------|--------------------|
| Test Case 1 | Roll is not 7                               | Robber does not move                                       | :white_check_mark: |
| Test Case 2 | Roll is 7 and no hex currently has robber   | Selected hex has robber                                    | :white_check_mark:                |
| Test Case 3 | Roll is 7 and robber starts on another hex  | Previous hex no longer has robber; selected hex has robber | :white_check_mark:                |
| Test Case 4 | Roll is 7 and selected hex already has robber | Throws `IllegalArgumentException`                           | :white_check_mark:                |
| Test Case 5 | Roll is 7 and robber moves                  | Exactly one hex has robber                                 | :white_check_mark:                |
| Test Case 6 | Roll is 7 and selected hex ID is invalid    | Throws `IllegalArgumentException`                          | :white_check_mark:                |
