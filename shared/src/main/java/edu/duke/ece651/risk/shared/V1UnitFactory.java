package edu.duke.ece651.risk.shared;

public class V1UnitFactory implements AbstractUnitFactory{

    @Override
    public Unit createNSoldiers(int amount) {
        return new BasicUnit("Soldier", amount);
    }
    
}
