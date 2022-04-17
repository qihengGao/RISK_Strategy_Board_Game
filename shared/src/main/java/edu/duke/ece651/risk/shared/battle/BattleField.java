package edu.duke.ece651.risk.shared.battle;

import edu.duke.ece651.risk.shared.map.RISKMap;
import edu.duke.ece651.risk.shared.territory.Owner;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.Unit;
import edu.duke.ece651.risk.shared.unit.UnitComparator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
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

    /**
     * change battle resolve rules
     * @param attackResolver
     */
    public void setAttackResolver(AttackResolver attackResolver) {
        this.attackResolver = attackResolver;
    }

    /**
     * Battlefield ctor
     * @param territoryOfContest
     * @param attackResolver
     */
    public BattleField(Territory territoryOfContest, AttackResolver attackResolver){
        this.territoryOfContest = territoryOfContest;
        this.currDefenderId = territoryOfContest.getOwnerID();
        this.currDefendingUnit = territoryOfContest.getUnits();
        this.attackResolver = attackResolver;
        this.attackers = new HashMap<>();
    }

    /**
     * Battlefield ctor
     * @param territoryOfContest
     */
    public BattleField(Territory territoryOfContest) {
        this(territoryOfContest, new DiceAttackResolver(20));
    }

    /**
     * clear the attackers list after all battles finished
     */
    public void resetAttackersList (){
        this.attackers.clear();
    }

    /**
     * add an attacker from a player (id) with Unit (toAdd)
     * @param id
     * @param toAdd
     */
    public void addAttacker(Long id, Unit toAdd) {
        addAttacker(new HashSet<Long>(), id, toAdd);
    }

    public void addAttacker(HashSet<Long> myAllies, Long id, Unit toAdd){
        for (Long allyID : myAllies){
            if (this.attackers.containsKey(allyID)){
                id = allyID;
            }
        }

        TreeSet<Unit> IDattackerList = this.attackers.getOrDefault(id, new TreeSet<>(new UnitComparator()));
        for (Unit u : IDattackerList){
            if (u.equals(toAdd)){
                u.tryIncreaseAmount(toAdd.getAmount());
            }
        }
        IDattackerList.add(toAdd);
        this.attackers.put(id, IDattackerList);
    }

    /**
     * resolve all battles in this battlefield
     * @param territory
     */
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

    /**
     * resolve the battle between two kinds of units
     * @param attackers
     * @param defenders
     * @param round
     */
    public void fightBetweenTwoUnits(TreeSet<Unit> attackers, TreeSet<Unit> defenders, int round) {
        //todo: change order of battle resolve
        Unit currAttacker = attackers.first();
        Unit currDefender = defenders.last();
        if (round%2==1){
            currAttacker = attackers.last();
            currDefender = defenders.first();
        }

//        System.out.println("round " + round + ": "+currAttacker.getType() + " (attacker) vs. " + currDefender.getType());

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
