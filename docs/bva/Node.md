## domain.Node Class
int id -> node id, for use in id to node object map in board
Player occupant -> player object or null
InfraType infraType -> SETTLEMENT, CITY, null

## Methods
getNodeOccupant() -> returns the player of the node
buildSettlement(Player player) -> sets node occupant to player and infraType to settlement
buildCity() -> changes infraType from settlement to city
getInfraType() -> returns settlement or city or null

## BVA
### Method under test: `getNodeOccupant`
#### Inputs:
- Node occupant (state of node)
  - Type: cases
  - Candidates: player object or null
#### Outputs:
- Player
  - Type: cases
  - Candidates: player object or null

- **TC1: Unoccupied node, return null** ( :white_check_mark: )
    - **State of the system**: Node.occupant == null
    - **Expected output**: null

- **TC2: Occupied node, return player** ( :white_check_mark: )
    - **State of the system**: Node.occupant == player1
    - **Expected output**: player1

### Method under test: `buildSettlement(int playerId)`
TODO: distance rule? idk if the check belongs here
#### Inputs:
- State of the node -> if Node.occupant is currently null or not
  - Type: boolean
  - true/false
- Player
  - Type: cases
  - Cases: Players 1, 2, 3, 4
#### Outputs:
- State of the node -> node occupant should equal player
  - Type: cases 
  - Cases: Players 1, 2, 3, 4
- State of the node -> node infraType equals settlement
  - Type: cases
  - Cases: settlement
- Signal for unsuccessful set occupant
  - Type: cases
  - IllegalStateException: "Cannot settle on an already-settled node."

- **TC1: Successful build settlement** ( :white_check_mark: )
    - **State of the system**: Node.occupant == null, player == player1
    - **Expected output**: Node.occupant == player1, Node.infraType == settlement

- **TC2: Unsuccessful build settlement** ( :white_check_mark: )
    - **State of the system**: Node.occupant == player1, player == player2
    - **Expected output**: Node.occupant == player1, IllegalStateException: "Cannot settle on an already-settled node."

### Method under test: `buildCity(int playerId)`
#### Inputs:
- State of the node -> node occupant
  - Type: cases
  - Cases: null or Players 0, 1, 2, 3, 4
- State of the node -> value of the node infraType
  - Type: cases
  - Candidates: settlement, city, null
- Input player
  - Type: cases
  - Cases: Players 1, 2, 3, 4
#### Outputs:
- State of the node -> node occupant should stay the same
  - Type: cases
  - Cases: null or Players 1, 2, 3, 4
- State of the node -> node infraType should be city if it's a valid upgrade
  - Type: cases
  - city
- Signal for unsuccessful upgrade
  - Type: cases
  - IllegalStateException: "Cannot upgrade an unsettled node to city."
  - IllegalStateException: "Cannot upgrade a city further."
  - IllegalStateException: "Cannot build a city on an already-settled node."

- **TC1: Successful upgrade settlement to city"** ( :white_check_mark: )
  - **State of the system**: Node.occupant == player1, Node.infraType == settlement
  - **Expected output**: Node.occupant == player1, Node.infraType == city

- **TC2: Unsuccessful upgrade city to city** ( :white_check_mark: )
  - **State of the system**: Node.occupant == player1, Node.infraType == city
  - **Expected output**: Node.occupant == player1, Node.infraType == city, IllegalStateException: "Cannot upgrade a city further."

- **TC3: Unsuccessful upgrade unoccupied node to city** ( :white_check_mark: )
  - **State of the system**: Node.occupant == null, Node.infraType == null
  - **Expected output**: Node.occupant == null, Node.infraType == null, IllegalStateException: "Cannot upgrade an unsettled node to city."

- **TC4: Unsuccessful upgrade city on someone else's settlement** ( :white_check_mark: )
  - **State of the system**: Node.occupant == player1, Node.infraType == settlement, player == player2
  - **Expected output**: Node.occupant == player1, Node.infraType == settlement, IllegalStateException: "Cannot build a city on an already-settled node."

### Method under test: `getInfraType()`
#### Inputs:
- State of the node -> value of the node infraType
  - Type: cases
  - Candidates: settlement, city, null
#### Outputs:
- infraType
  - Type: cases
  - Candidates: settlement, city, null

- **TC1: Unoccupied node, return empty string** ( :white_check_mark: )
  - **State of the system**: Node.infraType == null
  - **Expected output**: null

- **TC2: domain.Node occupied by settlement, return settlement** ( :white_check_mark: )
  - **State of the system**: Node.infraType == settlement
  - **Expected output**: settlement

- **TC3: domain.Node occupied by city, return city** ( :white_check_mark: )
  - **State of the system**: Node.infraType == city
  - **Expected output**: city