package edu.duke.ece651.risk.shared.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;

import edu.duke.ece651.risk.shared.territory.BasicTerritory;
import edu.duke.ece651.risk.shared.territory.Color;
import edu.duke.ece651.risk.shared.territory.Territory;
import org.junit.jupiter.api.Test;

public class BasicUnitTest {
  @Test
  public void test_constructors_and_getters() {
    Unit u = new BasicUnit("Soldier", 10);
    assertEquals("Soldier", u.getType());
    assertEquals(10, u.getAmount());

    Unit u1 = new BasicUnit("Soldier level 5", 9);
    assertEquals("Soldier", u1.getType());
    assertEquals(5, u1.getLevel());
    assertEquals(9, u1.getAmount());

    u1.setLevel(6);
    assertEquals(6, u1.getLevel());
  }

  @Test
  public void test_adders() {
    Unit u = new BasicUnit("Soldier", 10);
    assertTrue(u.tryIncreaseAmount(5));
    assertEquals(15, u.getAmount());
    assertTrue(u.tryDecreaseAmount(5));
    assertEquals(10, u.getAmount());
    assertFalse(u.tryDecreaseAmount(11));
    assertEquals(10, u.getAmount());
  }

  @Test
  public void test_tostring_and_equals() {
    Unit u1 = new BasicUnit("Soldier", 10);
    assertEquals("10 lv.0 Soldier", u1.toString());
    Unit u2 = new BasicUnit("Soldier", 20);
    Unit u3 = new BasicUnit("Soldier", 30);
    Unit u4 = new BasicUnit("Soldeir", 10);
    Territory t = new BasicTerritory("t1");
    HashSet<Unit> set = new HashSet<>();
    set.add(u1);
    set.add(u2);
    set.add(u3);
    assertTrue(set.contains(u3));
    assertEquals(1, set.size());

    assertFalse(u3.equals(null));
    assertTrue(u3.equals(u3));
    assertTrue(u1.equals(u3));
    assertFalse(u3.equals(t));
    assertFalse(u1.equals(u4));
  }

  @Test
  public void test_set_amount() {
    Unit u = new BasicUnit();
    u.setAmount(5);
    assertEquals(5, u.getAmount());
    u.setAmount(10);
    assertEquals(10, u.getAmount());
  }

  @Test
  public void test_upgradable_realated() {
    Unit u0 = new BasicUnit("Soldier", 10);
    Unit u1 = new BasicUnit("Soldier", 10, 1);
    Unit u2 = new BasicUnit("Soldier", 10, 2);
    Unit u3 = new BasicUnit("Soldier", 10, 3);
    Unit u4 = new BasicUnit("Soldier", 10, 4);
    Unit u5 = new BasicUnit("Soldier", 10, 5);
    Unit u6 = new BasicUnit("Soldier", 10, 6);
    ArrayList<Unit> units = new ArrayList<>();
    units.add(u0);
    units.add(u1);
    units.add(u2);
    units.add(u3);
    units.add(u4);
    units.add(u5);
    units.add(u6);
    int[] predefinedBonus = new int[] {0, 1, 3, 5, 8, 11, 15};
    for (int i = 0; i < units.size(); ++i) {
      Unit curr = units.get(i);
      assertEquals(i, curr.getLevel());
      assertEquals(6, u0.getLevelBound());
      assertEquals(predefinedBonus[i], curr.getBonus());
    }
    assertEquals(4, u4.getLevel());
    assertEquals(6, u4.getLevelBound());
    assertEquals(8, u4.getBonus());
  }

  @Test
  public void test_get_cost() {
    Unit u = new BasicUnit("Soldier", 10);
    int[] predefinedAccumulativeCosts = new int[] {0, 3, 11, 30, 55, 90, 140};
    for (int i = 0; i < predefinedAccumulativeCosts.length; ++i) {
      assertEquals(predefinedAccumulativeCosts[i], u.getCostToLevel(i));
    }
  }

  @Test
  public void test_comparator() {
    UnitComparator comparator = new UnitComparator();
    Unit u1 = new BasicUnit("a", 1);
    Unit u2 = new BasicUnit("a", 1);
    Unit u3 = new BasicUnit("b", 0);
    Unit u4 = new BasicUnit("a", 0, 3);
    assertEquals(0, comparator.compare(u1, u1));
    assertEquals(0, comparator.compare(u1, u2));
    assertEquals(-1, comparator.compare(u1, u3));
    assertEquals(-1, comparator.compare(u1, u4));
    u1.setOwnerId(0L);
    u2.setOwnerId(1L);
    assertEquals(-1, comparator.compare(u1, u2));
  }

  @Test
  public void test_ownerId() {
    Unit u = new BasicUnit("a level 2", 5);
    assertEquals(-1L, u.getOwnerId());
    u.setOwnerId(0L);
    assertEquals(0L, u.getOwnerId());
  }

  @Test
  public void test_color() {
    Unit u = new BasicUnit("a level 2", 5);
    assertEquals("5 lv.2 a", u.toString());
  }
}
