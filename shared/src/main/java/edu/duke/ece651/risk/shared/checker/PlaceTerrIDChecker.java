package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class PlaceTerrIDChecker extends PlaceRuleChecker{

    @Override
    protected void checkMyRule(RISKMap riskMap, Map<String, Integer> unitPlaceOrders, Long userId) {
        ArrayList<Territory> myTerrs = (ArrayList<Territory>) riskMap.getTerritoriesByOwnerID(userId);
        for (String terrName : unitPlaceOrders.keySet()){
            Territory t = riskMap.getTerritoryByName(terrName);
            if (!myTerrs.contains(t)){
                throw new IllegalArgumentException("You must place units in your own territories!");
            }
        }
    }
    public PlaceTerrIDChecker(PlaceRuleChecker next) {
        super(next);
    }
}
