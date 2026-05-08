## Class: `PlacementValidator`
**Responsibilities:** Enforces distance and adjacency rules.

### Method under test: `validateSettlementPlacement(int nodeId)`

| Test Case ID | State of the System | Expected output |   Implemented?    |
|--------------|---------------------|-----------------|:-----------------:|
| **TC-PV-01** | Target Node is adjacent to an occupied Node | Throw `IllegalPlacementException` | :white_checkmark: |
| **TC-PV-02** | Target Node and all neighbors are empty | Success (No Exception) | :white_checkmark: |

### Method under test: `validateInitialRoad(int edgeId, int settlementNodeId)`

| Test Case ID | State of the System | Expected output |   Implemented?    |
|--------------|---------------------|-----------------|:-----------------:|
| **TC-PV-03** | Edge connects to the given Settlement Node | Success (No Exception) | :white_checkmark: |
| **TC-PV-04** | Edge does NOT connect to the Settlement Node | Throw `IllegalPlacementException` |        :x:        |