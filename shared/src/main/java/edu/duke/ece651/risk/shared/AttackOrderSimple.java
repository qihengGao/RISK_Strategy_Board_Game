package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.checker.*;
import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

public class AttackOrderSimple extends AttackOrder{
    private final ActionChecker attackChecker;

    public AttackOrderSimple(long ID, String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
        super(ID, srcTerritory, destTerritory, unitUnderOrder, unitAmount);
        this.attackChecker = new TerrExistChecker(new SrcOwnershipChecker(new ActionUnitChecker(new PathExistAttackChecker(null))));
    }

    @Override
    public String executeOrder(RISKMap riskMap) {
        // TODO change this to battlefield

        String check_message = attackChecker.checkMove(riskMap, this);
        if (check_message == null){
            Territory sourceTerritory = riskMap.getTerritoryByName(this.srcTerritory);
            Territory destinationTerritory = riskMap.getTerritoryByName(this.destTerritory);

            Unit sourceTerritoryUnit = sourceTerritory.getUnitByType(this.unitType);
            Unit destinationTerritoryUnit = destinationTerritory.getUnitByType(this.unitType);
            sourceTerritoryUnit.tryDecreaseAmount(this.unitAmount);
            Unit Attackers = new BasicUnit("Soldier", this.unitAmount);
            destinationTerritory.getBattleField().addAttacker(this.playerID, Attackers);
        }
        return check_message;
    }

}
