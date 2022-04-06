package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;

public class V1UnitFactory implements AbstractUnitFactory{

    /**
     * create a territory of specific type and amount
     * @param amount
     * @return the created unit
     */
    @Override
    public Unit createNSoldiers(int amount) {
        return new BasicUnit("Soldier", amount);
    }
    
}
