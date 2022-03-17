package edu.duke.ece651.risk.shared;

import edu.duke.ece651.risk.shared.unit.Unit;

import java.io.Serializable;
import java.util.Comparator;

class unitCompactor implements Comparator<Unit>, Serializable {
    private static final long serialVersionUID = -5972458403679726498L;

    public int compare(Unit arg0, Unit arg1) {
        return arg0.getType().compareTo(arg1.getType());
    }
}