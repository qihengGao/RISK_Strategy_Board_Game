package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.battle.BattleField;
import edu.duke.ece651.risk.shared.battle.SimpleAttackResolver;
import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import edu.duke.ece651.risk.shared.unit.UnitComparator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class BattleFieldTest {

    @Test
    void addAttacker() {
        //terr1, unit 20, ownerID=3
        Territory t = new BasicTerritory("Test1");
        t.tryAddUnit(new BasicUnit("Unit", 20));
        t.tryChangeOwnerTo((long) 3);
        assertEquals(t.getOwnerID(), 3);

        BattleField BF = new BattleField(t);
        BF.setAttackResolver(new SimpleAttackResolver());
        Unit toadd1 = new BasicUnit("Unit", 10);
        Unit toadd2 = new BasicUnit("Unit", 10);

        //add 20 attackers from player 0
        BF.addAttacker((long) 0, toadd1);
        BF.addAttacker((long) 0, toadd2);
        assertEquals(BF.getAttackers().get((long) 0).size(), 1);//assert 20 attackers in battlefield
        assertSame(BF.getAttackers().get((long) 0).first(), toadd1);
        assertEquals(BF.getAttackers().get((long) 0).first().getAmount(), 20);

        //with ally info
        BF.addAttacker(new HashSet<Long>(Arrays.asList(0L,4L)), 2L, toadd2);
        assertEquals(BF.getAttackers().get((long) 0).size(), 1);//assert 30 attackers in battlefield
        assertSame(BF.getAttackers().get((long) 0).first(), toadd1);
        assertEquals(BF.getAttackers().get((long) 0).first().getAmount(), 30);
    }

    @Test
    void fightBetweenTwoUnits() {
        TreeSet<Unit> ts1 = new TreeSet<>(new UnitComparator());
        TreeSet<Unit> ts2 = new TreeSet<>(new UnitComparator());
        //ts1 add units
        ts1.add(new BasicUnit("Unit0", 5));
        ts1.add(new BasicUnit("Unit1", 10));
        ts1.add(new BasicUnit("Unit2", 10));
        ts2.add(new BasicUnit("Unit0", 10));
        ts2.add(new BasicUnit("Unit1", 10));
        ts2.add(new BasicUnit("Unit2", 10));

        //terr1, unit 20, ownerID=3
        Territory t = new BasicTerritory("Test1");
        BattleField BF = new BattleField(t);
        BF.setAttackResolver(new SimpleAttackResolver());
        BF.fightBetweenTwoUnits(ts1, ts2, 0);

        assertEquals(ts1.size(), 2);
        assertEquals(ts2.size(), 3);
        assertEquals(ts1.first().getType(), "Unit1");
        assertEquals(ts2.first().getType(), "Unit0");
        assertEquals(ts2.last().getAmount(), 5);

//        //level 1 units always win
//        BF.setAttackResolver(new DiceAttackResolver(2));
//        BF.fightBetweenTwoUnits(ts1, ts2, 0);
//
//        assertTrue(ts1.isEmpty());
//        assertEquals(ts2.first().getType(), "Unit level 1");

    }

    @Test
    void fightAllBattle_attacker_win() {
        TreeSet<Unit> ts1 = new TreeSet<>(new UnitComparator());
        ts1.add(new BasicUnit("Unit0", 5));
        ts1.add(new BasicUnit("Unit1", 10));
        ts1.add(new BasicUnit("Unit2", 10));
        Territory t = new BasicTerritory("Test1");
        t.tryChangeOwnerTo(3L);
        t.setUnits(ts1);
        BattleField BF = new BattleField(t);
        //ts1 add units

        BF.addAttacker(0L, new BasicUnit("Unit0", 10));
        BF.addAttacker(0L, new BasicUnit("Unit1", 10));
        BF.addAttacker(0L, new BasicUnit("Unit2", 10));

        assertEquals(t.getUnits().size(), 3);

        BF.setAttackResolver(new SimpleAttackResolver());

        assertEquals(BF.getAttackers().get(0L).size(), 3);

        BF.fightAllBattle(t);

        assertEquals(BF.getAttackers().get(0L).size(), 1);
        assertEquals(BF.getAttackers().get(0L).first().getAmount(), 5);

        System.out.println(t.getUnitByType("Unit2"));

        assertEquals(t.getUnits().iterator().next().getAmount(), 5);

        assertEquals(t.getOwnerID(), 0);

        BF.resetAttackersList();

        assertEquals(t.getUnits().iterator().next().getAmount(), 5);

    }

    @Test
    void fightAllBattle_defender_win() {
        TreeSet<Unit> ts1 = new TreeSet<>(new UnitComparator());
        ts1.add(new BasicUnit("Unit0", 10));
        ts1.add(new BasicUnit("Unit1", 10));
        ts1.add(new BasicUnit("Unit2", 10));
        Territory t = new BasicTerritory("Test1");
        t.tryChangeOwnerTo(3L);
        t.setUnits(ts1);
        BattleField BF = new BattleField(t);
        //ts1 add units

        BF.addAttacker(0L, new BasicUnit("Unit0", 5));
        BF.addAttacker(0L, new BasicUnit("Unit1", 10));
        BF.addAttacker(0L, new BasicUnit("Unit2", 10));

        assertEquals(t.getUnits().size(), 3);

        BF.setAttackResolver(new SimpleAttackResolver());

        assertEquals(BF.getAttackers().get(0L).size(), 3);

        BF.fightAllBattle(t);

        assertEquals(BF.getAttackers().get(0L).size(), 0);

        assertEquals(t.getUnits().first().getAmount(), 5);

        assertEquals(t.getOwnerID(), 3);

        BF.resetAttackersList();

        assertEquals(t.getUnits().first().getAmount(), 5);
    }

    @Test
    void fightBattle() {
        TreeSet<Unit> ts1 = new TreeSet<>(new UnitComparator());
        ts1.add(new BasicUnit("Unit0", 10));
        ts1.add(new BasicUnit("Unit1", 10));
        ts1.add(new BasicUnit("Unit2", 10));
        ts1.add(new BasicUnit("Unit3", 10));
        ts1.add(new BasicUnit("Unit4", 10));
        ts1.add(new BasicUnit("Unit5", 10));
        ts1.add(new BasicUnit("Unit6", 10));
        ts1.add(new BasicUnit("Unit7", 10));
        ts1.add(new BasicUnit("Unit8", 10));
        ts1.add(new BasicUnit("Unit9", 10));
        TreeSet<Unit> ts2 = new TreeSet<>(new UnitComparator());
        ts2.add(new BasicUnit("Unit0", 10));
        ts2.add(new BasicUnit("Unit1", 10));
        ts2.add(new BasicUnit("Unit2", 10));
        ts2.add(new BasicUnit("Unit3", 10));
        ts2.add(new BasicUnit("Unit4", 10));
        ts2.add(new BasicUnit("Unit5", 10));
        ts2.add(new BasicUnit("Unit6", 10));
        ts2.add(new BasicUnit("Unit7", 10));
        ts2.add(new BasicUnit("Unit8", 10));
        ts2.add(new BasicUnit("Unit9", 10));

        Territory t = new BasicTerritory("Test1");
        BattleField BF = new BattleField(t);

        for (int i = 0; i < 10; i++){
            BF.fightBetweenTwoUnits(ts1, ts2, i);
        }
    }
}