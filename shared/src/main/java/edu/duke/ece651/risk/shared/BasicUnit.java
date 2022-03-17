package edu.duke.ece651.risk.shared;

/**
 * Basic Unit for Risk Game.
 */
public class BasicUnit implements Unit {
    private final String unitType;


    private int amount;

    public void setAmount(int amount) {
        this.amount = amount;
    }

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getAmount());
        result.append(" ");
        result.append(this.getType());
        return result.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + unitType.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BasicUnit other = (BasicUnit) obj;
        if (!unitType.equals(other.unitType)) {
            return false;
        }
        return true;
    }

}
