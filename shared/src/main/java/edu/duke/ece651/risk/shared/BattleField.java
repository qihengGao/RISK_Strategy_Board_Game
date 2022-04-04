package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;
import edu.duke.ece651.risk.shared.unit.UnitComparator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeSet;

// summarize attack ->
// <src,dest,amount>
// src.id
// battleF(dest)
public class BattleField implements Serializable {
    private Territory territoryOfContest; // include defender territory ID
    private long currDefenderId; // current defender, battle still ongoing
    private TreeSet<Unit> currDefendingUnit; // current defending units
    private HashMap<Long, TreeSet<Unit>> attackers;
    private AttackResolver attackResolver;

    public HashMap<Long, TreeSet<Unit>> getAttackers() {
        return attackers;
    }

    public void setAttackResolver(AttackResolver attackResolver) {
        this.attackResolver = attackResolver;
    }

    public BattleField(Territory territoryOfContest, AttackResolver attackResolver){
        this.territoryOfContest = territoryOfContest;
        this.currDefenderId = territoryOfContest.getOwnerID();
        this.currDefendingUnit = territoryOfContest.getUnits();
        this.attackResolver = attackResolver;
        this.attackers = new HashMap<>();
    }

    public BattleField(Territory territoryOfContest) {
        this(territoryOfContest, new DiceAttackResolver(20));
    }

    public void resetAttackersList (){
        this.attackers.clear();
    }
    
    public void addAttacker(Long id, Unit toAdd) {
        if (!this.attackers.containsKey(id)) {
            this.attackers.put(id, new TreeSet<Unit>(new UnitComparator()));
        }
        TreeSet<Unit> unitsAlreadyIn = this.attackers.get(id);
        for (Unit u : unitsAlreadyIn){
            if (u.equals(toAdd)){
                u.tryIncreaseAmount(toAdd.getAmount());
            }
        }
        unitsAlreadyIn.add(toAdd);
    }

    public void fightAllBattle(Territory territory){

        for (long attackerId : this.attackers.keySet()) {
            TreeSet<Unit> attackers = this.attackers.get(attackerId);
            TreeSet<Unit> defenders = this.currDefendingUnit;

            int round = 0;
            while(attackers.size() > 0 && defenders.size() > 0){
                fightBetweenTwoUnits(attackers, defenders, round);
                round++;
            }
            if (defenders.size()==0){
                if (attackers.size() != 0) {
                    territory.tryChangeOwnerTo(attackerId);
                }
                territory.setUnits(attackers);
            }
        }
    }

    public void fightBetweenTwoUnits(TreeSet<Unit> attackers, TreeSet<Unit> defenders, int round) {
        //todo: change order of battle resolve
        Unit currAttacker = attackers.first();
        Unit currDefender = defenders.last();
        if (round%2==1){
            currAttacker = attackers.last();
            currDefender = defenders.first();
        }
        while (currAttacker.getAmount()>0 && currDefender.getAmount()>0){
            //defender wins fight
            if(attackResolver.resolveCurrent(currAttacker, currDefender)==1){
                currAttacker.tryDecreaseAmount(1);
            }
            //duel
            else if (attackResolver.resolveCurrent(currAttacker, currDefender)==0){
                currAttacker.tryDecreaseAmount(1);
                currDefender.tryDecreaseAmount(1);
            }
            //attacker wins fight
            else{
                currDefender.tryDecreaseAmount(1);
            }
        }
        if (currDefender.getAmount()==0){
            if (currAttacker.getAmount()==0){
                attackers.remove(currAttacker);
            }
            defenders.remove(currDefender);
        }
        else{
            attackers.remove(currAttacker);
        }
    }
}
