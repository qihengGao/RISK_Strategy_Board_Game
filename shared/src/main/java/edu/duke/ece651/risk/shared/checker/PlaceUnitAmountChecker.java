package edu.duke.ece651.risk.shared.checker;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;

import java.util.HashSet;
import java.util.Map;

public class PlaceUnitAmountChecker extends PlaceRuleChecker{
    private int totalAmount;
    @Override
    protected void checkMyRule(RISKMap riskMap, Map<String, Integer> unitPlaceOrders, Long userId) {
        for (int unitAmount : unitPlaceOrders.values()){
            if (unitAmount<0){
                throw new IllegalArgumentException("The unit amount must be positive integer!");
            }
            if (unitAmount>totalAmount){
                throw new IllegalArgumentException("The unit amount exceeds your available amount!");
            }
            totalAmount-=unitAmount;
        }
        //check if used all totalAmounts
        if (totalAmount!=0){
            throw new IllegalArgumentException("You must place all your units!");
        }
    }
    public PlaceUnitAmountChecker(PlaceRuleChecker next, int totalAmount) {
        super(next);
        this.totalAmount = totalAmount;
    }
}
