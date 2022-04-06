package edu.duke.ece651.risk.shared.unit;

import edu.duke.ece651.risk.shared.territory.BasicTerritory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Basic Unit for Risk Game.
 */
public class BasicUnit implements Unit {
    //unit information
    private final String unitType;
    private final int levelBound;
    private int level;
    private int amount;
    private int[] predefinedBonus = new int[] {0, 1, 3, 5, 8, 11, 15};
    private int[] predefinedAccumulativeCosts = new int[] {0, 3, 11, 30, 55, 90, 140};

    /**
     * Contructor for the Basic Unit.
     * 
     * @param unitType
     * @param amount
     */
    public BasicUnit(String unitType, int amount) {
        String[] info = unitType.split(" ");
        this.amount = amount;
        this.levelBound = 6;
        if (info.length == 1) {
            this.unitType = unitType;
            this.level = 0;
        } else { // unitType format is : unitType level levelNum
            this.unitType = info[0];
            this.level = Integer.parseInt(info[2]);
        }
    }

    public BasicUnit(String unitType, int amount, int level) {
        this(unitType, amount);
        this.level = level;
    }


    /**
     * setters/ getters
     * @param amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public int getLevelBound() {
        return this.levelBound;
    }
    @Override
    public int getLevel() {
        return this.level;
    }

    /**
     * @return the bonus of this unit in a fight
     */
    @Override
    public int getBonus() {
        return this.predefinedBonus[this.getLevel()];
    }

    /**
     * @param level
     * @return get the cost to upgrade to a level for this unit
     */
    // Precondition: the upgrade should be checked validity before calling this function
    @Override
    public int getCostToLevel(int level) {
        return this.predefinedAccumulativeCosts[level] - this.predefinedAccumulativeCosts[this.level];
    }

    /**
     * Return the type name of the unit.
     * @return String
     */
    @Override
    public String getType() {
        return this.unitType;
    }

    /**
     * Get current amount of the unit.
     * @return int
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    /**
    * Try to increase the amount.
     * @param amount
     * @return boolean, true if successful, else false
     */
    @Override
    public boolean tryIncreaseAmount(int amount) {
        this.amount += amount;
        return true;
    }

    /**
     * Try to decrease the amount.
     * @param amount
     * @return boolean, true if successful, else false
     */
    @Override
    public boolean tryDecreaseAmount(int amount) {
        if (amount > this.amount) {
            return false;
        }
        this.amount -= amount;
        return true;
    }

    /**
     * overrode to string to show info of this unit
     * @return
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getAmount());
        result.append(" ");
        result.append("lv.");
        result.append(this.getLevel());
        result.append(" ");
        result.append(this.getType());
        return result.toString();
    }

    /**
     * compare units
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicUnit basicUnit = (BasicUnit) o;
        return level == basicUnit.level && Objects.equals(unitType, basicUnit.unitType);
    }

    /**
     * overrode hashcode
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(unitType, level);
    }
}
