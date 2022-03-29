package edu.duke.ece651.risk.shared.unit;

import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.Serializable;
import java.util.Comparator;

public class UnitComparator implements Comparator<Unit>, Serializable {
    private static final long serialVersionUID = -5972458403679726498L;

    /**
     * Compare by the unit type or their level if the types are same
     * @param u1
     * @param u2
     * @return
     */
    public int compare(Unit u1, Unit u2) {
        if (u1.getType().equals(u2.getType())) {
            return Integer.compare(u1.getLevel(), u2.getLevel());
        }
        return u1.getType().compareTo(u2.getType());
    }
}