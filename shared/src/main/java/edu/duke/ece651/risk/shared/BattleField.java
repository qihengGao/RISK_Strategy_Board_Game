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

    public BattleField(Territory territoryOfContest){
        this.territoryOfContest = territoryOfContest;
        this.currDefenderId = territoryOfContest.getOwnerID();
        this.currDefendingUnit = territoryOfContest.getUnitByType("Soldier");
        this.attackers = new HashMap<>();
    }

    
    public void addAttacker() {
        
    }

    //TODO
    public String fightBattle(){
        for (long attackerId : this.attackers.keySet()) {
            
        }
        return null;
    }
}
