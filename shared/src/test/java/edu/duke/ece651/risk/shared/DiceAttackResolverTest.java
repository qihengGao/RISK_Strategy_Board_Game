package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DiceAttackResolverTest {
    @Test
    public void test_defaultConstructor(){
        AttackResolver resolver = new DiceAttackResolver(10);
        // assertEquals(10, resolver.);
        System.out.println(resolver.resolveCurrent());
        
    }
}
