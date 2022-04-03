package edu.duke.ece651.risk.shared.order;

import edu.duke.ece651.risk.shared.checker.*;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

public class AttackOrder extends Order {
    private final ActionChecker attackChecker;

    public AttackOrder(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
//        super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount, "Attack");
//        this.attackChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistAttackChecker(null))));

        this();
        this.playerID = ID;
        this.srcTerritory = srcTerritory;
        this.destTerritory = destTerritory;
        this.unitAmount = unitAmount;
        this.unitType = unitUnderOrder;
        this.orderType = "Attack";
    }

    public AttackOrder() {
        this.attackChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistAttackChecker(new PathResourceAttackChecker(null)))));
    }

    @Override
    public String executeOrder(RISKMap riskMap) {
        // TODO change this to battlefield

        String check_message = attackChecker.checkMove(riskMap, this);
        if (check_message == null){
            Territory sourceTerritory = riskMap.getTerritoryByName(this.srcTerritory);
            Territory destinationTerritory = riskMap.getTerritoryByName(this.destTerritory);

            Unit sourceTerritoryUnit = sourceTerritory.getUnitByType(this.unitType);
            sourceTerritoryUnit.tryDecreaseAmount(this.unitAmount);
            Unit Attackers = new BasicUnit(this.unitType, this.unitAmount);
            destinationTerritory.getBattleField().addAttacker(this.playerID, Attackers);
        }
        return check_message;
    }

}
