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
    - Candidates: null or Players 1, 2, 3, 4
#### Outputs:
- Players
    - Type: cases
    - Candidates: null or Players 1, 2, 3, 4

- **TC1: Unoccupied edge, return 0** ( :white_check_mark: )
    - **State of the system**: Edge.occupant == null
    - **Expected output**: null

- **TC2: domain.Edge occupied by 1, return 1** ( :white_check_mark: )
    - **State of the system**: Edge.occupant == player1
    - **Expected output**: player1

### Method under test: `buildRoad(int playerId)`
#### Inputs:
- State of the node -> if Edge.playerId is currently null or not
    - Type: boolean
    - true/false
- Input player
    - Type: cases
    - Cases: Players 1, 2, 3, 4
#### Outputs:
- State of the node -> domain.Edge occupant should equal player
    - Type: cases
    - Cases: null or Players 1, 2, 3, 4
- Signal for unsuccessful set occupant
    - Type: cases
    - IllegalStateException: "Cannot build a road on an occupied edge."

- **TC1: Successful build road** ( :white_check_mark: )
    - **State of the system**: Edge.occupant == null, player == player1
    - **Expected output**: Edge.occupant == player1

- **TC2: Unsuccessful build road** ( :white_check_mark: )
    - **State of the system**: Edge.occupant == player1, player == player2
    - **Expected output**: Edge.occupant == player1, IllegalStateException: "Cannot build a road on an occupied edge."
