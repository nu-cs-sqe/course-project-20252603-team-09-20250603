| Test Case ID | State of the System | Expected output | Implemented?       |
| :--- | :--- | :--- |:-------------------|
| **TC-TM-01** | 3 players; `placementsCount` = 0 (Start) | `currentPlayerIndex` = 1 | :white_check_mark: |
| **TC-TM-02** | 3 players; `placementsCount` = 2 (Round 1 End) | `currentPlayerIndex` = 2 (Stays same) | :white_check_mark: |
| **TC-TM-03** | 3 players; `placementsCount` = 3 (Round 2 Start) | `currentPlayerIndex` = 2 (Reverses) | :white_check_mark: |
| **TC-TM-04** | 3 players; `placementsCount` = 5 (Setup End) | `currentPlayerIndex` = 0 | :x:                |