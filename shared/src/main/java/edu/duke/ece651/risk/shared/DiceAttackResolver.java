package edu.duke.ece651.risk.shared;

import java.util.Random;

public class DiceAttackResolver extends AttackResolver{

    public int diceFacetNumber;

    public DiceAttackResolver(int diceFacetNumber) {
        this(diceFacetNumber, null);
    }
    
    public DiceAttackResolver(int diceFacetNumber, AttackResolver next){
        super(next);
        this.diceFacetNumber = diceFacetNumber;
    }

    /**
     * determine the output of 1 on 1 battle between the attack soldier and the defend soldier
     * @note the one with the higher number win
     * @note resolve again if the numbers are equal
     */
    @Override
    public boolean resolveCurrent() {
        Random rand = new Random(12345); // TODO: need seed
        int randomGeneratedDefender = rand.nextInt(this.diceFacetNumber);
        int randomGeneratedAttacker = rand.nextInt(this.diceFacetNumber);
        if(randomGeneratedAttacker > randomGeneratedDefender){
            return false;
        }        
        else if(randomGeneratedDefender > randomGeneratedAttacker){
            return true;
        }
        return resolveCurrent();
    }
}
