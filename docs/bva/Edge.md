## domain.Edge Class
int id -> edge id, for use in id to edge object map in board
int occupant -> player id (0 if not occupied)

## Methods
getEdgeOccupant()
buildRoad(int playerId) (setter for edge occupant)

### Method under test: `getEdgeOccupant`
#### Inputs:
- Value of edge occupant (state of edge)
    - Type: cases
    - Candidates: 0, 1, 2, 3, 4
#### Outputs:
- Player id
    - Type: cases
    - Candidates: 0, 1, 2, 3, 4

- **TC1: Unoccupied edge, return 0** ( :white_check_mark: )
    - **State of the system**: domain.Edge.occupant == 0
    - **Expected output**: 0

- **TC2: domain.Edge occupied by 1, return 1** ( :white_check_mark: )
    - **State of the system**: domain.Edge.occupant == 1
    - **Expected output**: 1

### Method under test: `buildRoad(int playerId)`
TODO: validation of cases for edge placement (e.g. floating road, going through opponent infra, etc.)
#### Inputs:
- State of the node -> if domain.Edge.playerId is currently 0 or not
    - Type: boolean
    - true/false
- Input player id
    - Type: cases
    - Cases: 1, 2, 3, 4
#### Outputs:
- State of the node -> domain.Edge occupant should equal playerId
    - Type: cases
    - Cases: 0, 1, 2, 3, 4
- Signal for unsuccessful set occupant
    - Type: cases
    - IllegalStateException: "Cannot build a road on an occupied edge."

- **TC1: Successful build road** ( :white_check_mark: )
    - **State of the system**: domain.Edge.occupant == 0, playerId == 1
    - **Expected output**: domain.Edge.occupant == 1

- **TC2: Unsuccessful build road** ( :white_check_mark: )
    - **State of the system**: domain.Edge.occupant == 1, playerId == 2
    - **Expected output**: domain.Edge.occupant == 1, IllegalStateException: "Cannot build a road on an occupied edge."
