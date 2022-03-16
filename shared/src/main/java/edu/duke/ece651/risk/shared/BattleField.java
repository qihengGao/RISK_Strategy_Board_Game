package edu.duke.ece651.risk.shared;

import java.util.HashMap;

// summarize attack ->
// <src,dest,amount>
// src.id
// battleF(dest)
public class BattleField {
    private Territory territoryOfContest; // include defender territory ID
    private long currDefenderId; // current defender, battle still ongoing
    private Unit currDefendingUnit; // current defending units
    private HashMap<Long, Unit> attackers;
    private AttackResolver attackResolver;

    public BattleField(Territory territoryOfContest, AttackResolver attackResolver){
        this.territoryOfContest = territoryOfContest;
        this.currDefenderId = territoryOfContest.getOwnerID();
        this.currDefendingUnit = territoryOfContest.getUnitByType("Soldier");
        this.attackResolver = attackResolver;
        this.attackers = new HashMap<>();
    }

    public BattleField(Territory territoryOfContest) {
        this(territoryOfContest, new DiceAttackResolver(20));
    }
    
    public void addAttacker(Long id, Unit unit) {
        if (!this.attackers.containsKey(id)) {
            this.attackers.put(id, unit);
            return;
        }
        Unit unitAlreadyIn = this.attackers.get(id);
        unitAlreadyIn.tryIncreaseAmount(unit.getAmount());
        this.attackers.put(id, unitAlreadyIn);
    }

    public void fightBattle(Territory territory){
        for (long attackerId : this.attackers.keySet()) {
            Unit currAttackingUnit = this.attackers.get(attackerId);
            Unit currDefendingUnit = territory.getUnitByType("Soldier");
            while(currAttackingUnit.getAmount() > 0 && currDefendingUnit.getAmount() > 0){
                if(attackResolver.resolveCurrent()){
                    currAttackingUnit.tryDecreaseAmount(1);
                }
                else{
                    currDefendingUnit.tryDecreaseAmount(1);
                }
            }
            if(currDefendingUnit.getAmount() <= 0){
                territory.tryChangeOwnerTo(attackerId);
            }
        }
    }
}
