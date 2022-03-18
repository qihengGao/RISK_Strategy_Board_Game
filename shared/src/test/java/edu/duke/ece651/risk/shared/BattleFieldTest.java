package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Territory;
import edu.duke.ece651.risk.shared.unit.BasicUnit;
import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleFieldTest {

    @Test
    void addAttacker() {
        //terr1, unit 20, ownerID=3
        Territory t = new BasicTerritory("Test1");
        t.tryAddUnit(new BasicUnit("Unit", 20));
        t.tryChangeOwnerTo((long)3);
        assertEquals(t.getOwnerID(), 3);

        BattleField BF = new BattleField(t);
        BF.setAttackResolver(new SimpleAttackResolver());
        Unit toadd1 = new BasicUnit("Unit",10);
        Unit toadd2 = new BasicUnit("Unit",10);

        //add 20 attackers from player 0
        BF.addAttacker((long)0, toadd1);
        BF.addAttacker((long)0, toadd2);
        assertEquals(BF.getAttackers().get((long)0).getAmount(),20);//assert 20 attackers in battlefield

        //resolve battle by simple attack resolver
        BF.fightBattle(t, "Unit");
        BF = new BattleField(t, new SimpleAttackResolver());
        assertEquals(t.getOwnerID(), 3);
        assertEquals(t.getUnitByType("Unit").getAmount(), 0);

        //add 11 attackers from player 1
        Unit toadd3 = new BasicUnit("Unit",0);
        Unit toadd4 = new BasicUnit("Unit",1);
        BF.addAttacker((long)1, toadd3);
        BF.addAttacker((long)1, toadd4);
        assertNull(BF.getAttackers().get((long)0));
        assertEquals(BF.getAttackers().get((long)1).getAmount(),1);
        BF.fightBattle(t, "Unit");
        assertEquals(t.getOwnerID(), 1);
        assertEquals(t.getUnitByType("Unit").getAmount(), 1);
    }

    @Test
    void fightBattle() {
        //terr1, unit 20, ownerID=3
        Territory t = new BasicTerritory("Test1");
        t.tryAddUnit(new BasicUnit("Unit", 20));
        t.tryChangeOwnerTo((long)3);
        assertEquals(t.getOwnerID(), 3);

        BattleField BF = new BattleField(t);
        Unit toadd1 = new BasicUnit("Unit",10);
        Unit toadd2 = new BasicUnit("Unit",10);

        //add 20 attackers from player 0
        BF.addAttacker((long)0, toadd1);
        BF.addAttacker((long)0, toadd2);
        assertEquals(BF.getAttackers().get((long)0).getAmount(),20);//assert 20 attackers in battlefield

        //resolve battle by simple attack resolver
        BF.fightBattle(t, "Unit");
        BF = new BattleField(t);
        assertEquals(t.getOwnerID(), 3);
        assertEquals(t.getUnitByType("Unit").getAmount(), 9);

        //add 11 attackers from player 1
        Unit toadd3 = new BasicUnit("Unit",0);
        Unit toadd4 = new BasicUnit("Unit",1);
        BF.addAttacker((long)1, toadd3);
        BF.addAttacker((long)1, toadd4);
        assertNull(BF.getAttackers().get((long)0));
        assertEquals(BF.getAttackers().get((long)1).getAmount(),1);
        BF.fightBattle(t, "Unit");
        assertEquals(t.getOwnerID(), 3);
        assertEquals(t.getUnitByType("Unit").getAmount(), 9);
    }
}