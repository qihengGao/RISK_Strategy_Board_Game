package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiceAttackResolverTest {
    @Test
    public void test_resolveCurrent(){
        Random r = new Random(12345);
        int[] list = {-1,0,1,0};
        AttackResolver resolver = new DiceAttackResolver(2);
        for (int i : list) {
            assertEquals(i, resolver.resolveCurrent());
        }
    }
}
