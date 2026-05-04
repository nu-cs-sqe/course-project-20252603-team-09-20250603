| Test Case ID | Scenario | Input | Expected Result |
| :--- | :--- | :--- | :--- |
| **TC-PR-01** | Distance Rule (Too Close) | Placement at Node adjacent to existing settlement | **Fail** (IllegalPlacementException) |
| **TC-PR-02** | Distance Rule (Valid) | Placement at Node 2+ edges away | **Pass** |
| **TC-PR-03** | Snake Turn (Middle) | Player 3 finished 1st house (3-player game) | **Next Player is Player 3** |
| **TC-PR-04** | Snake Turn (End) | Player 1 finished 2nd house | **Phase Transitions to Turn 1** |