| Test Case ID | Scenario | Input | Expected Result | Implemented?                |
|:-------------| :--- | :--- | :--- |:----------------------------|
| **TC-TM-01** | Distance Rule (Too Close) | Placement at Node adjacent to existing settlement | **Fail** (IllegalPlacementException) | :white_check_mark:          |
| **TC-TM-02** | Distance Rule (Valid) | Placement at Node 2+ edges away | **Pass** | :x:                         |
| **TC-TM-03** | Snake Turn (Middle) | Player 3 finished 1st house (3-player game) | **Next Player is Player 3** | :white_check_mark:                            |
| **TC-TM-04** | Snake Turn (End) | Player 1 finished 2nd house | **Phase Transitions to Turn 1** | :x:                         |