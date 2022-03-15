package edu.duke.ece651.risk.shared;

import java.util.HashSet;

public class PathExistAttackChecker extends ActionChecker{
    public PathExistAttackChecker(ActionChecker next) {
        super(next);
    }

    @Override
    protected String checkMyRule(RISKMap riskMap, Order moveOrder) {
        Territory src = riskMap.getTerritoryByName(moveOrder.getSrcTerritory());
        Territory dst = findEnemyNeighbor(src, moveOrder.getDestTerritory());
        if (dst == null) {
            return "You cannot attack " + moveOrder.getDestTerritory() + " from " + moveOrder.getSrcTerritory() + "!";
        }
        if (dst.getOwnerID()== src.getOwnerID()){
            return "You cannot attack your own territory!";
        }
        return null;
    }

    private Territory findEnemyNeighbor(Territory src, String dstName){
        Territory dst = null;
        for (Territory t : src.getNeighbors()){
            if (t.getName()==dstName){
                dst = t;
            }
        }
        return dst;
    }
}
