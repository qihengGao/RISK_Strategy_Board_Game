package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ColorTest {

    @Test
    public void test_defaultConstructor(){
        Color color = new Color("red");
        assertEquals("red", color.getColorName());
        assertEquals("red", color.getColorValue());
    }

    @Test
    public void test_overrideConstructor(){
        Color color = new Color("redName", "redValue");
        assertEquals("redName", color.getColorName());
        assertEquals("redValue", color.getColorValue());
    }

    @Test
    public void test_getters(){
        String colorName = "colorName";
        String colorValue = "colorValue";
        Color color = new Color(colorName, colorValue);
        assertEquals(colorName, color.getColorName());
        assertEquals(colorValue, color.getColorValue());
    }
}
