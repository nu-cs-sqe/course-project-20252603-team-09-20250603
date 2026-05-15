package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private int[] nodeOccupants = new int[54];
    private Map<Integer, List<Integer>> edgeToNodes = new HashMap<>();

    public boolean isNodeOccupied(int nodeId) {
        return nodeOccupants[nodeId] != 0;
    }

    public List<Integer> getAdjacentNodes(int nodeId) {
        return new ArrayList<>();
    }

    public boolean checkDistanceRule(int nodeId) {
        if (isNodeOccupied(nodeId)) {
            return false;
        }

        for (int neighborId : getAdjacentNodes(nodeId)) {
            if (isNodeOccupied(neighborId)) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getRoadEndpoints(int edgeId) {
        return edgeToNodes.get(edgeId);
    }
}