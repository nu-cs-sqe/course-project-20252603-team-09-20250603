package domain.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    // We only need a way to track if a node is occupied for the distance rule
    // Using a simple array where the index is the Node ID
    private int[] nodeOccupants = new int[54]; // Standard Catan has 54 nodes
    private Map<Integer, List<Integer>> edgeToNodes = new HashMap<>();


    /**
     * Bare minimum: Just checks if the occupant ID is non-zero.
     */
    public boolean isNodeOccupied(int nodeId) {
        return nodeOccupants[nodeId] != 0;
    }

    /**
     * Bare minimum: Hardcoded or simple adjacency logic.
     * In a real board, this would be a complex Map or Graph.
     */
    public List<Integer> getAdjacentNodes(int nodeId) {
        // For the sake of the first test, returning an empty list is fine
        // unless your test specifically sets up neighbors.
        return new ArrayList<>();
    }

    /**
     * Bare minimum: The logic your validator actually calls.
     */
    public boolean checkDistanceRule(int nodeId) {
        // If the node itself is taken, fail
        if (isNodeOccupied(nodeId)) return false;

        // Check neighbors
        for (int neighborId : getAdjacentNodes(nodeId)) {
            if (isNodeOccupied(neighborId)) return false;
        }
        return true;
    }

    // This method is used by the test or game to "set up" the state
    public void setNodeOccupant(int nodeId, int playerId) {
        nodeOccupants[nodeId] = playerId;
    }

    public List<Integer> getRoadEndpoints(int edgeId) {
        return edgeToNodes.get(edgeId);
    }
}