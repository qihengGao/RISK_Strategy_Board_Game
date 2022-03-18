package edu.duke.ece651.risk.shared;

import java.util.Random;

public class DiceAttackResolver extends AttackResolver{

    private int diceFacetNumber;
    private Random random;

    public DiceAttackResolver(int diceFacetNumber) {
        this(diceFacetNumber, null);
    }
    
    public DiceAttackResolver(int diceFacetNumber, AttackResolver next){
        super(next);
        this.diceFacetNumber = diceFacetNumber;
        this.random = new Random(12345);
    }

    /**
     * determine the output of 1 on 1 battle between the attack soldier and the defend soldier
     * @note the one with the higher number win
     * @note resolve again if the numbers are equal
     * @return
     */
    @Override
    public int resolveCurrent() {
        int randomGeneratedDefender = random.nextInt(this.diceFacetNumber);
        int randomGeneratedAttacker = random.nextInt(this.diceFacetNumber);
        if (randomGeneratedDefender > randomGeneratedAttacker){
            return 1;//defender wins
        }
        else if (randomGeneratedDefender == randomGeneratedAttacker){
            return 0;//draw
        }
        else{
            return -1;//attacker wins
        }
    }
}
