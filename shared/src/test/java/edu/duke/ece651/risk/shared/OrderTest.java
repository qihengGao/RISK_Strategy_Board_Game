package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class OrderTest {
  @Test
  public void test_toString() {
    Order o = new AttackOrder((long) 0, "1", "2", "S", 10);
    assertEquals("Attack():1->2: S 10", o.toString());
  }

}
