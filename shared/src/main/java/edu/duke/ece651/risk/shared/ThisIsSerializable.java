package edu.duke.ece651.risk.shared;

import java.io.Serializable;
import java.util.Comparator;

class ThisIsSerializable implements Comparator<Territory>, Serializable {
    private static final long serialVersionUID = -5972458403679726498L;

    public int compare(Territory arg0, Territory arg1) {
        return arg0.getName().compareTo(arg1.getName());
    }
}