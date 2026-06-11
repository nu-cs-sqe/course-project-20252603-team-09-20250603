### BVA Documentation: Development Card Activation

To thoroughly test the transition states of the `isActive` boolean and ensure players cannot bypass game rules, the test
cases target these specific boundaries:

| Test ID                     | Scenario / Boundary Condition       | Input / State                      | Expected Outcome                                                                      | Implemented?      |
|:----------------------------|:------------------------------------|:-----------------------------------|:--------------------------------------------------------------------------------------|:------------------|
| **TC-DC-01**                | Turn of Purchase (Lower Boundary)   | `isActive = false`                 | `getIsActive()` returns `false`. `doDevCardAction()` throws `IllegalActionException`. | :white_checkmark: |
| **TC-DC-02**                | End of Turn Phase Change            | Turn ends for current player.      | Card status remains `false` until the player's next turn begins.                      | :white_checkmark: |
| **TC-DC-03**                | Start of Next Turn (Upper Boundary) | Turn returns to the owning player. | `isActive` is updated to `true`.                                                      | :white_checkmark: |
| **TC-DC-KN**                | Knight Card Execution               | Active KNIGHT card played.         | Increments player's knight count by 1 and updates the board's robber state.           | :white_checkmark: |
| **TC-DC-RB**                | Road Building Card Execution        | Active ROAD_BUILDING card played.  | Deducts 2 roads from unbuilt inventory to place them on the game board.               | :white_checkmark: |
| **TC-DC-VERIFY-WRONG-TYPE** | Shared Type Guard Mismatch Boundary | Active card is played through the wrong action method. | `verifyCardIsPlayable(...)` throws `IllegalStateException` on card/action type mismatch. | :white_checkmark: |
| **TC-DC-YP**                | Year of Plenty Card Execution       | Active YEAR_OF_PLENTY card played. | Adds exactly 2 requested resources directly to the player's hand.                     | :white_checkmark: |
| **TC-DC-YP-INACTIVE**       | Year of Plenty Inactive Lower Boundary | `YEAR_OF_PLENTY` card is still inactive. | Playing the card throws `IllegalActionException`.                                      | :white_checkmark: |
| **TC-DC-YP-WRONG-TYPE**     | Year of Plenty Type Mismatch Boundary | Active non-`YEAR_OF_PLENTY` card is routed to Year of Plenty logic. | Action throws `IllegalStateException`.                                                 | :white_checkmark: |
| **TC-DC-MO**                | Monopoly Card Execution             | Active MONOPOLY card played.       | Sweeps all resources of a target type from opponent inventories into player's hand.   | :white_checkmark: |
| **TC-DC-MO-INACTIVE**       | Monopoly Inactive Lower Boundary | `MONOPOLY` card is still inactive. | Playing the card throws `IllegalActionException`.                                      | :white_checkmark: |
| **TC-DC-MO-WRONG-TYPE**     | Monopoly Type Mismatch Boundary | Active non-`MONOPOLY` card is routed to Monopoly logic. | Action throws `IllegalStateException`.                                                 | :white_checkmark: |
| **TC-DC-VP**                | Victory Point Card Passive Logic    | Active VICTORY_POINT card held.    | Permanently increments the player's calculatePoints() output by 1.                    | :white_checkmark: |
### BVA Documentation: Drawing a Development Card

A development card costs **1 Ore + 1 Wool (Sheep) + 1 Grain (Wheat)**. These cases target the boundary
between affording and not affording the draw, verifying the player is only charged on a successful draw.

| Test ID               | Scenario / Boundary Condition           | Input / State                                            | Expected Outcome                                                                                 | Implemented?      |
|:----------------------|:----------------------------------------|:---------------------------------------------------------|:-------------------------------------------------------------------------------------------------|:------------------|
| **TC-DC-DRAW-OK**      | Exact Cost (On Boundary)                | Player holds exactly 1 Ore, 1 Sheep, 1 Wheat.            | Card is added to hand and all three resources are deducted to 0.                                  | :white_checkmark: |
| **TC-DC-DRAW-SURPLUS** | Surplus Resources (Above Boundary)      | Player holds extra Ore plus unrelated resources.         | Card is drawn; only the cost is deducted, surplus and unrelated resources remain untouched.       | :white_checkmark: |
| **TC-DC-DRAW-NONE**    | No Resources (Below Boundary)           | Player holds no resources.                               | Draw throws `IllegalStateException`; hand stays empty.                                            | :white_checkmark: |
| **TC-DC-DRAW-PARTIAL** | Missing One Resource (Just Below)       | Player holds 1 Ore + 1 Sheep but no Wheat.               | Draw throws `IllegalStateException`; no card gained and the resources held are not charged.        | :white_checkmark: |
