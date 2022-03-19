package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class OrderTest {
    
    @Test
    public void test_toString(){
        Order order = new AttackOrder(0L, "Test1", "Test", "Soldier", 10);
        String actual = order.toString();
        String expected = "Attack():Test1->Test: Soldier 10";
        assertEquals(expected, actual);
    }
}
