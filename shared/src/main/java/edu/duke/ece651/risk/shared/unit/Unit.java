package edu.duke.ece651.risk.shared.unit;

import edu.duke.ece651.risk.shared.territory.Color;

import java.io.Serializable;

/**
 * Interface for game unit.
 */

public interface Unit extends Serializable{
    /**
     * Return the type name of the unit.
     * @return String
     */
    public String getType();

    /**
     * Return the level of current unit.
     * @return int
     */
    public int getLevel();

    /**
     * set the level of current unit.
     * @param level
     */
    public void setLevel(int level);

    /**
     * Return the limit of current unit level.
     * @return int
     */
    public int getLevelBound();

    /**
     * Return the bonus of the unit.
     * @return
     */
    public int getBonus();

    /**
     * Get current amount of the unit.
     * @return int
     */
    public int getAmount();

    /**
     * Get current amount of the unit.
     * @return int
     */
    public void setAmount(int amount);

    /**
     * Try to increase the amount.
     * @param amount
     * @return boolean, true if successful, else false
     */
    public boolean tryIncreaseAmount(int amount);

    /**
     * Try to decrease the amount.
     * @param amount
     * @return boolean, true if successful, else false
     */
    public boolean tryDecreaseAmount(int amount);

    /**
     * Return the cost to upgrade one unit of current to the level specified.
     * @param level
     * @return
     */
    public int getCostToLevel(int level);

    /**
     * Return the ownerId where the unit belongs to.
     * @return
     */
    public long getOwnerId();

    /**
     * Set current unit to a specified ownerId.
     * @param ownerId
     */
    public void setOwnerId(long ownerId);
}
