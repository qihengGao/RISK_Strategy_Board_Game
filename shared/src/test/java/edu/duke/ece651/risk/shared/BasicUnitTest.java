package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

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
}
