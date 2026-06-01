### BVA Documentation: Development Card Activation

To thoroughly test the transition states of the `isActive` boolean and ensure players cannot bypass game rules, the test
cases target these specific boundaries:

### BVA Documentation: Development Card Activation

To thoroughly test the transition states of the `isActive` boolean and ensure players cannot bypass game rules, the test
cases target these specific boundaries:

| Test ID      | Scenario / Boundary Condition       | Input / State                      | Expected Outcome                                                                      | Implemented?      |
|:-------------|:------------------------------------|:-----------------------------------|:--------------------------------------------------------------------------------------|:------------------|
| **TC-DC-01** | Turn of Purchase (Lower Boundary)   | `isActive = false`                 | `getIsActive()` returns `false`. `doDevCardAction()` throws `IllegalActionException`. | :white_checkmark: |
| **TC-DC-02** | End of Turn Phase Change            | Turn ends for current player.      | Card status remains `false` until the player's next turn begins.                      | :white_checkmark: |
| **TC-DC-03** | Start of Next Turn (Upper Boundary) | Turn returns to the owning player. | `isActive` is updated to `true`.                                                      | :white_checkmark: |
| **TC-DC-KN** | Knight Card Execution               | Active KNIGHT card played.         | Increments player's knight count by 1 and updates the board's robber state.           | :white_checkmark: |
| **TC-DC-RB** | Road Building Card Execution        | Active ROAD_BUILDING card played.  | Deducts 2 roads from unbuilt inventory to place them on the game board.               | :white_checkmark: |
| **TC-DC-YP** | Year of Plenty Card Execution       | Active YEAR_OF_PLENTY card played. | Adds exactly 2 requested resources directly to the player's hand.                     | :white_checkmark: |
| **TC-DC-MO** | Monopoly Card Execution             | Active MONOPOLY card played.       | Sweeps all resources of a target type from opponent inventories into player's hand.   | :white_checkmark: |
| **TC-DC-VP** | Victory Point Card Passive Logic    | Active VICTORY_POINT card held.    | Permanently increments the player's calculatePoints() output by 1.                    | :white_checkmark: |