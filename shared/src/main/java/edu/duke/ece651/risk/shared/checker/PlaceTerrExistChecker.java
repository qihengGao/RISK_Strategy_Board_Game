package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;

import java.util.Map;

public class PlaceTerrExistChecker extends PlaceRuleChecker{

    /**
     * check if the territory exist for this placement
     * @param riskMap
     * @param unitPlaceOrders
     * @param userId
     */
    @Override
    protected void checkMyRule(RISKMap riskMap, Map<String, Integer> unitPlaceOrders, Long userId) {
        for (String terrName : unitPlaceOrders.keySet()){
            if (riskMap.getTerritoryByName(terrName)==null){
                throw new IllegalArgumentException("Cannot find territory "+ terrName +"!");
            }
        }
    }
    public PlaceTerrExistChecker(PlaceRuleChecker next) {
        super(next);
    }
}
