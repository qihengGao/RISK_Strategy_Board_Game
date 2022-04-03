package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.order.Order;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class PathResourceMoveChecker extends ActionChecker {
    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
        Territory dst = riskMap.getTerritoryByName(moveOrder.getDestTerritory());
        HashSet<Territory> visited = new HashSet<Territory>();

        HashMap<Territory, Territory> parents = doDijkstra(riskMap, moveOrder.getPlayerID(), src);
        ArrayList<Territory> path = getPathFromSrcToDest(parents, src, dst);
        int cost = -1*getCostFromSrcToDest(path)*moveOrder.getUnitAmount();

        return riskMap.getOwners().get(moveOrder.getPlayerID()).tryAddOrRemoveFoodResource(cost);
    }

    /**
     * helper method to get the next node to compute path
     * @param distances
     * @param queue
     * @return the node to continuing computing path
     */
    private Territory getMinimumDistanceTerritory(
            HashMap<Territory, Integer> distances, HashSet<Territory> queue){
        int minimumDistance = Integer.MAX_VALUE;
        Territory toReturn = null;

        for(Territory territory: distances.keySet()){
            if(distances.get(territory) < minimumDistance && queue.contains(territory)){
                toReturn = territory;
                minimumDistance = distances.get(territory);
            }
        }
        return toReturn;
    }

    /**
     * helper method to use djikstra's algorithm
     * to get each territory's parent along the shortest path
     * @param riskMap
     * @param ownerId
     * @return hashmap for each territory's parent
     */
    protected HashMap<Territory, Territory> doDijkstra(RISKMap riskMap, long ownerId, Territory source){
        Iterable<Territory> territories = riskMap.getContinent();

        // a hashmap to store distance to the territory, initalize to infinity
        HashMap<Territory, Integer> distances = new HashMap<Territory, Integer>();
        for(Territory territory: territories){
            if(territory.getOwnerID().equals(ownerId)){
                distances.put(territory, Integer.MAX_VALUE);
            }
        }

        // set the distance from source -> source to 0
        distances.put(source, 0);

        // a hashset to record each territory's parent
        HashMap<Territory, Territory> parents = new HashMap<>();
        for(Territory territory: territories){
            if(territory.getOwnerID().equals(ownerId)) {
                parents.put(territory, null);
            }
        }

        HashSet<Territory> queue = new HashSet<>();
        for(Territory territory: territories){
            if(territory.getOwnerID().equals(ownerId)){
                queue.add(territory);
            }
        }

        while (!queue.isEmpty()){
            Territory currTerritory = this.getMinimumDistanceTerritory(distances, queue);
            queue.remove(currTerritory);

            Iterable<String> neighborStrs = currTerritory.getNeighbors();
            for(String neighborStr: neighborStrs){
                Territory neighbor = riskMap.getTerritoryByName(neighborStr);
                if(neighbor.getOwnerID().equals(ownerId)) {
                    if (distances.get(currTerritory) + neighbor.getSize() < distances.get(neighbor)) {
                        distances.put(neighbor, distances.get(currTerritory) + neighbor.getSize());
                        parents.put(neighbor, currTerritory);
                    }
                }
            }
        }
        return parents;
    }

    /**
     * get the shortest path from source to territory
     * @param parents
     * @param source
     * @param destination
     * @return an arraylist of path territories if path exists, null otherwise
     */
    protected ArrayList<Territory> getPathFromSrcToDest(HashMap<Territory, Territory> parents,
                                                        Territory source, Territory destination){
        if(!parents.keySet().contains(source) || !parents.keySet().contains(destination)){
            return null;
        }

        ArrayList<Territory> path = new ArrayList<>();
        path.add(destination);
        Territory parent = parents.get(destination);

        // get cost of path from source to destination in reverse
        while(parent != null && parent != source){
            path.add(parent);
            parent = parents.get(parent);
        }
        if(parent == null){
            return null;
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * return the cost to move from source to destination territory,
     * given that the path is already computed
     * @param path
     * @return cost
     */
    protected int getCostFromSrcToDest(ArrayList<Territory> path){
        int cost = 0;

        for(Territory territory: path){
            cost += territory.getSize();
        }
        return cost;
    }

    public PathResourceMoveChecker(ActionChecker next) {
        super(next);
    }

}
