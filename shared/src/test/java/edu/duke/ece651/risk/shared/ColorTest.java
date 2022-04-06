package edu.duke.ece651.risk.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import edu.duke.ece651.risk.shared.territory.Color;
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

    @Test
    public void test_equals(){
        Color color1 = new Color("redName", "redValue");
        Color color2 = new Color("redName", "redValue");
        Color color3 = new Color("redName", "yellowValue");
        Color color4 = new Color("yellowName", "redValue");
        int color5 = 10;

        assertEquals(color1, color2);
        assertEquals(color2, color1);
        assertNotEquals(color1, color3);
        assertNotEquals(color1, color4);
        assertNotEquals(color1, color5);
    }

    @Test
    public void test_toString(){
        Color color1 = new Color("redName", "redValue");
        assertEquals("<redName: redValue>", color1.toString());
    }

    @Test
    public void test_hashCode(){
        Color color1 = new Color("redName", "redValue");
        Color color2 = new Color("redName", "redValue");
        Color color3 = new Color("redName", "yellowValue");
        Color color4 = new Color("yellowName", "redValue");

        assertEquals(color1.hashCode(), color2.hashCode());
        assertEquals(color2.hashCode(), color1.hashCode());
        assertNotEquals(color1.hashCode(), color3.hashCode());
        assertNotEquals(color1.hashCode(), color4.hashCode());
    }

  /**
  @Test
  static void assertEqualsIgnoreLineSeparator(String expected, String actual) {
    assertEquals(expected.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")),
                 actual.replaceAll("\\n|\\r\\n", System.getProperty("line.separator")));
  }
  */


}
