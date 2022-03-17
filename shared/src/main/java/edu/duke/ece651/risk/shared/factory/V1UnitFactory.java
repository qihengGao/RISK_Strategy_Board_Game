package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.BasicUnit;
import edu.duke.ece651.risk.shared.Unit;

public class V1UnitFactory implements AbstractUnitFactory{

    @Override
    public Unit createNSoldiers(int amount) {
        return new BasicUnit("Soldier", amount);
    }
    
}
