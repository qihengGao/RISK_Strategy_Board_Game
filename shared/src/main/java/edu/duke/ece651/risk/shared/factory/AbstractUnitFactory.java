package edu.duke.ece651.risk.shared.factory;

import edu.duke.ece651.risk.shared.Unit;

/**
 * Interface for unit factory.
 */
public interface AbstractUnitFactory {
    Unit createNSoldiers(int amount);
}
