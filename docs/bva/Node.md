## Node Class
int id -> node id, for use in id to node object map in board
int occupant -> player id (0 if not occupied)
string infraType -> "settlement", "city", or "" (empty string if nothing)

## Methods
getNodeOccupant() -> returns the player id of the node
buildSettlement(int playerId) -> sets node id to playerId and infraType to "settlement"
buildCity() -> changes infraType from "settlement" to "city"
getInfraType() -> returns "settlement" or "city" or ""

## BVA
### Method under test: `getNodeOccupant`
#### Inputs:
- Value of node occupant (state of node)
  - Type: cases
  - Candidates: 0, 1, 2, 3, 4
#### Outputs:
- Player id
  - Type: cases
  - Candidates: 0, 1, 2, 3, 4

- **TC1: Unoccupied node, return 0** ( :white_check_mark: )
    - **State of the system**: Node.occupant == 0
    - **Expected output**: 0

- **TC2: Node occupied by 1, return 1** ( :white_check_mark: )
    - **State of the system**: Node.occupant == 1
    - **Expected output**: 1

### Method under test: `buildSettlement(int playerId)`
TODO: distance rule? idk if the check belongs here
#### Inputs:
- State of the node -> if Node.playerId is currently 0 or not
  - Type: boolean
  - true/false
- Input player id
  - Type: cases
  - Cases: 1, 2, 3, 4
#### Outputs:
- State of the node -> node occupant should equal playerId
  - Type: cases 
  - Cases: 0, 1, 2, 3, 4
- State of the node -> node infraType equals "settlement"
  - Type: cases
  - Cases: "settlement"
- Signal for unsuccessful set occupant
  - Type: cases
  - IllegalStateException: "Cannot settle on an already-settled node."

- **TC1: Successful build settlement** ( :white_check_mark: )
    - **State of the system**: Node.occupant == 0, playerId == 1
    - **Expected output**: Node.occupant == 1, Node.infraType == "settlement"

- **TC2: Unsuccessful build settlement** ( :x: or :white_check_mark: )
    - **State of the system**: Node.occupant == 1, playerId == 2
    - **Expected output**: Node.occupant == 1, IllegalStateException: "Cannot settle on an already-settled node."

### Method under test: `buildCity()`
#### Inputs:
- State of the node -> value of the node occupant
  - Type: cases
  - Cases: 0, 1, 2, 3, 4
- State of the node -> value of the node infraType
  - Type: cases
  - Candidates: "settlement", "city", ""
#### Outputs:
- State of the node -> node occupant should stay the same
  - Type: cases
  - Cases: 0, 1, 2, 3, 4
- State of the node -> node infraType should be "city" if it's a valid upgrade
  - Type: cases
  - "city"
- Signal for unsuccessful upgrade
  - Type: cases
  - IllegalStateException: "Cannot upgrade an unsettled node to city."
  - IllegalStateException: "Cannot upgrade a city further."

- **TC1: Successful upgrade settlement to city"** ( :x: or :white_check_mark: )
  - **State of the system**: Node.occupant == 1, Node.infraType == "settlement"
  - **Expected output**: Node.occupant == 1, Node.infraType == "city"

- **TC2: Unsuccessful upgrade city to city** ( :x: or :white_check_mark: )
  - **State of the system**: Node.occupant == 2, Node.infraType == "city"
  - **Expected output**: Node.occupant == 2, Node.infraType == "city", IllegalStateException: "Cannot upgrade a city further."

- **TC3: Unsuccessful upgrade city to city** ( :x: or :white_check_mark: )
  - **State of the system**: Node.occupant == 0, Node.infraType == ""
  - **Expected output**: Node.occupant == 0, Node.infraType == "", IllegalStateException: "Cannot upgrade an unsettled node to city."

### Method under test: `getInfraType()`
#### Inputs:
- State of the node -> value of the node infraType
  - Type: cases
  - Candidates: "settlement", "city", ""
#### Outputs:
- infraType
  - Type: cases
  - Candidates: "settlement", "city", ""

- **TC1: Unoccupied node, return empty string** ( :x: or :white_check_mark: )
  - **State of the system**: Node.infraType == ""
  - **Expected output**: ""

- **TC2: Node occupied by settlement, return settlement** ( :x: or :white_check_mark: )
  - **State of the system**: Node.infraType == "settlement"
  - **Expected output**: "settlement"

- **TC3: Node occupied by city, return city** ( :x: or :white_check_mark: )
  - **State of the system**: Node.infraType == "city"
  - **Expected output**: "city"