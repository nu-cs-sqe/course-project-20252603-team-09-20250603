# System Design

| Class | Responsibilities | Key Attributes | Methods |
| :--- | :--- | :--- | :--- |
| **Hex** | Represents a single tile on the board. | `int id`, `TerrainType type`, `int tokenNum`, `boolean hasRobber` | `getHasRobber()`, `setHasRobber()`, `getTokenNum()`, `setTokenNum()`, `getTerrainType()`, `setTerrainType()` |
| **Player** | Tracks a single player's identity, score, and assets. | `String name`, `Color color`, `Inventory inventory`, `int victoryPoints`, `ResourceHand hand`, `DevCardHand devhand` | `getVictoryPoints()`, `calculateVictoryPoints()`, `getName()`, `getColor()`, `canBuild(Type)`, `Build(Type, Node/Edge)`, `MoveRobber(hex)`, `getResource/useResource`, `getDevCard/useDevCard` |
| **ResourceHand** | Manages the player's current resource cards. | `Map<ResourceType, Integer> counts` | `ReceiveResource(type, amount)`, `SpendResource(type, amount)`, `HasSufficientResources(cost)`, `getTotalResourceCount()`, `stealRandomResource()`, `stealResource(p, type)` |
| **InfrastructureInventory** | Manages the count of remaining physical pieces. | `int roads`, `int settlements`, `int cities` | `hasInfrastructure(type)`, `BuildInfrastructure(type)`, `addBackInfrastructure(type)` |
| **Board** | The map container that tracks connectivity and piece locations. | `List<Hex> tiles`, `Map<Integer, Node> nodes`, `Map<Integer, Edge> edges`, `Map<Integer, Node> nodeToHex`, `Map<Integer, Hex> hexToNode`, `List<List<Edge>> graph` | `getHexFromRoll(roll)`, `getNodesFromHex(hexID)`, `checkDistanceRule(nodeID)`, `checkLongestRoad()`, `isValidRoadPlacement(edgeID, p)`, `placeInfrastructure(nodeID, p, type)`, `placeRoad(edgeID, p)`, `moveRobber(newHexID)` |
| **Node** | A vertex where 3 hexes meet (Settlement/City spot). | `int id`, `Integer occupantID`, `InfrastructureType type` | `getNodeOccupant()`, `getInfrastructureType()`, `setNodeOccupant(p, type)`, `upgradeSettlementToCity()` |
| **Edge** | A boundary between 2 hexes (Road spot). | `int id`, `Integer occupantID` | `getRoadOccupant()`, `setRoadOccupant(p)`, `getRoadEndpoints()` |
| **DevCardHand** | Manages the player's development cards (current and new). | `Map<DevCardType, Integer> validCards`, `Map<DevCardType, Integer> newCards` | `ReceiveDevCard(type, amount)`, `SpendDevCard(type, amount)`, `hasLargestArmy()` |
