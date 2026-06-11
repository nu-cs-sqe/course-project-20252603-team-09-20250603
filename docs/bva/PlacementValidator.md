## Class: `PlacementValidator`
**Responsibilities:** Enforces distance and adjacency rules.

# PlacementValidator BVA

**Responsibilities:** Enforces distance and adjacency rules for settlement placement.

---

## Method under test: `validateSettlementPlacement(Node targetNode)`

Validates that a settlement can be placed at the target node by checking distance rule
1. Only build a settlement on an unoccupied intersection 
2. only if none of the 3 adjacent intersections contains a settlement or city.

| Test Case ID | State of the System                                      | Expected Output                      | Implemented? |
|--------------|----------------------------------------------------------|--------------------------------------|--------------|
| **TC-PV-01** | Target node is already occupied                          | Throws `IllegalPlacementException`   | ✅            |
| **TC-PV-02** | Target node is adjacent (via edge) to an occupied node   | Throws `IllegalPlacementException`   | ✅            |
| **TC-PV-03** | Target node and all edge-adjacent neighbors are empty    | No exception thrown                  | ✅            |



## Method under test: `validateInitialRoad(int edgeId, Node settlementNode)`
Ensures that a road placement connects to a owned settlement (node).

| Test Case ID | State of the System                              | Expected Output                           | Implemented? |
|--------------|--------------------------------------------------|-------------------------------------------|--------------|
| **TC-PV-04** | Edge ID matches settlement node's hash code      | No exception thrown                       | ✅            | 
| **TC-PV-05** | Edge ID does not match settlement node's hash code | Throws `IllegalPlacementException`      |   ✅          |
| **TC-PV-11** | Target edge is already occupied                  | Throws `IllegalPlacementException` ("Edge already occupied.") | ✅            |



## Method under test: `validateRegularRoad(int edgeId, Player player)`
Ensures that a play for normal road road is placed on an empty edge that connects to the building
player's own road, settlement, or city (not an opponent's, and not in open water).

| Test Case ID | State of the System                                              | Expected Output                    | Implemented? |
|--------------|-----------------------------------------------------------------|------------------------------------|--------------|
| **TC-PV-06** | Target edge is already occupied                                 | Throws `IllegalPlacementException` | :check_mark           |
| **TC-PV-07** | An endpoint of the edge holds the player's own settlement       | No exception thrown                |  :check_mark            |
| **TC-PV-08** | An edge sharing an endpoint holds one of the player's own roads | No exception thrown                |  :check_mark         |
| **TC-PV-09** | The edge connects to none of the player's roads or buildings    | Throws `IllegalPlacementException` |  :check_mark            |
| **TC-PV-10** | The edge connects only to another player's road or building     | Throws `IllegalPlacementException` |  :check_mark            |