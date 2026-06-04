
### Method under test: `nextPlayer()`

| Test Case ID | State of the System                              | Expected output                       | Implemented?       |
| :--- |:-------------------------------------------------|:--------------------------------------|:-------------------|
| **TC-TM-01** | 3 players; before first call                     | `currentPlayerIndex` = 1 after one call | :white_check_mark: |
| **TC-TM-02** | 3 players; after 3 calls                         | `currentPlayerIndex` = 3              | :white_check_mark: |
| **TC-TM-03** | 3 players; after 4 calls (snake pivot)           | `currentPlayerIndex` = 3 (stays on last player) | :white_check_mark: |
| **TC-TM-04** | 4 players; after 8 calls (sequence exhausted)    | `currentPlayerIndex` = 1 (last player in snake order) | :white_check_mark: |
| **TC-TM-06** | 3 players; after 6 calls (sequence exhausted)    | `currentPlayerIndex` = 1 (last player in snake order) | :white_check_mark: |

When the setup sequence is exhausted, `currentPlayerIndex` stays on the last player who placed. That player goes first in normal play. A subsequent call to `nextPlayer()` marks setup complete without changing the index.

### Method under test: `setupStatus()`

| Test Case ID | State of the System                              | Expected output                       | Implemented?       |
| **TC-TM-05** | 4 players; 9 calls to nextPlayer()               | `setupStatus()` = true                | :white_check_mark: |

Setup is marked complete only after one additional `nextPlayer()` call once the snake sequence is empty.
