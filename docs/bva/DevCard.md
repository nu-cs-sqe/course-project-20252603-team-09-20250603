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
| **TC-DC-04** | Action Execution on Active Card     | `isActive = true`                  | `doDevCardAction()` executes successfully without exceptions.                         | :x:               |
| **TC-DC-05** | Post-Action State (Out of Bounds)   | Card is used.                      | Card is removed or marked spent; subsequent calls to `doDevCardAction()` fail.        | :x:               |