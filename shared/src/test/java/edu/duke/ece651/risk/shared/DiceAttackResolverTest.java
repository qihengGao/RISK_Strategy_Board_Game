package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiceAttackResolverTest {
    @Test
    public void test_resolveCurrent(){
        AttackResolver resolver = new DiceAttackResolver(20);
        assertEquals(true, resolver.resolveCurrent());
    }
}
