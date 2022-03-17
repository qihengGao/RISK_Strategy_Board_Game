package edu.duke.ece651.risk.shared;

public class SimpleAttackResolver extends AttackResolver{
    public SimpleAttackResolver() {
        super(null);
    }

    /**
     * determine the output of 1 on 1 battle between the attack soldier and the defend soldier
     * @note the one with the higher number win
     * @note resolve again if the numbers are equal
     * @return
     */
    @Override
    public int resolveCurrent() {
        return 0;//Soldiers always annihilate with each other
    }
}
