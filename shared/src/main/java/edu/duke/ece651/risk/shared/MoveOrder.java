package edu.duke.ece651.risk.shared;

public class MoveOrder extends Order {


    public MoveOrder(String srcTerritory, String destTerritory, String unitUnderOrder, int unitAmount) {
        super(srcTerritory, destTerritory, unitUnderOrder, unitAmount);
    }

    /**
     * execute a move order
     */
    @Override
    public void executeOrder(RISKMap riskMap) {
        Territory sourceTerritory = riskMap.getTerritoryByName(this.srcTerritory);
        Territory destinationTerritory = riskMap.getTerritoryByName(this.destTerritory);

        Unit sourceTerritoryUnit = sourceTerritory.getUnitByType(this.unitType);
        Unit destinationTerritoryUnit = destinationTerritory.getUnitByType(this.unitType);

        sourceTerritoryUnit.tryIncreaseAmount(this.unitAmount);
        destinationTerritoryUnit.tryDecreaseAmount(this.unitAmount);
    }
}
