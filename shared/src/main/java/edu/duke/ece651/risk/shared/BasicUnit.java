package edu.duke.ece651.risk.shared;

/**
 * Basic Unit for Risk Game.
 */
public class BasicUnit implements Unit {
    private final String unitType;
    private int amount;

    /**
     * Contructor for the Basic Unit.
     * 
     * @param unitType
     * @param amount
     */
    public BasicUnit(String unitType, int amount) {
        this.unitType = unitType;
        this.amount = amount;
    }

    /**
     * Return the type name of the unit.
     * 
     * @return String
     */
    @Override
    public String getType() {
        return this.unitType;
    }

    /**
     * Get current amount of the unit.
     * 
     * @return int
     */
    @Override
    public int getAmount() {
        return this.amount;
    }

    /**
     * Try to increase the amount.
     * 
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
     * 
     * @param amoount
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

}
