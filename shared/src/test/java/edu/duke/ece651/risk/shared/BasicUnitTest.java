package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class BasicUnitTest {
  @Test
  public void test_constructors_and_getters() {
    Unit u = new BasicUnit("Soldier", 10);
    assertEquals("Soldier", u.getType());
    assertEquals(10, u.getAmount());
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
    assertEquals("10 Soldier", u1.toString());
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
}
