package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.Serializable;
import java.util.HashMap;

// summarize attack ->
// <src,dest,amount>
// src.id
// battleF(dest)
public class BattleField implements Serializable {
    private Territory territoryOfContest; // include defender territory ID
    private long currDefenderId; // current defender, battle still ongoing
    private Unit currDefendingUnit; // current defending units
    private HashMap<Long, Unit> attackers;
    private AttackResolver attackResolver;

    public HashMap<Long, Unit> getAttackers() {
        return attackers;
    }

    public void setAttackResolver(AttackResolver attackResolver) {
        this.attackResolver = attackResolver;
    }

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

    public void resetAttackersList (){
        this.attackers = new HashMap<Long, Unit>();
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

    public void fightBattle(Territory territory, String type){

        for (long attackerId : this.attackers.keySet()) {
            int attacker_num = this.attackers.get(attackerId).getAmount();
            int defender_num = territory.getUnitByType(type).getAmount();
            while(attacker_num > 0 && defender_num > 0){
                //defender wins fight
                if(attackResolver.resolveCurrent()==1){
                    attacker_num--;
                }
                //duel
                else if (attackResolver.resolveCurrent()==0){
                    attacker_num--;
                    defender_num--;
                }
                //defender wins fight
                else{
                    defender_num--;
                }
            }
            if (defender_num==0){
                if (attacker_num != 0) {
                    territory.tryChangeOwnerTo(attackerId);
                }
                territory.getUnitByType(type).setAmount(attacker_num);
            }
            else{
                territory.getUnitByType(type).setAmount(defender_num);
            }
        }
    }
}
