package edu.duke.ece651.risk.shared.factory;

import static org.junit.jupiter.api.Assertions.*;

import edu.duke.ece651.risk.shared.unit.Unit;
import org.junit.jupiter.api.Test;

public class V1UnitFactoryTest {
  @Test
  public void test_createUnits() {
    AbstractUnitFactory f = new V1UnitFactory();
    Unit u = f.createNSoldiers(10);
    assertEquals(10, u.getAmount());
    assertEquals("Soldier", u.getType());
  }

}
