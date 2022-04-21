package edu.duke.ece651.risk.shared.territory;

import java.io.Serializable;


//save the color information
public class Color implements Serializable {

    private String colorName;
    private String colorValue;

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public Color() {
    }

    public Color(String _colorName){
        this.colorName = _colorName;
        this.colorValue = _colorName;
    }

    public Color(String _colorName, String _colorvalue){
        this.colorName = _colorName;
        this.colorValue = _colorvalue;
    }

    /**
     * get color name
     * @return color name
     */
    public String getColorName(){
        return colorName;
    }

    /**
     * get color value
     * @return color value
     */
    public String getColorValue(){
        return colorValue;
    }

    /**
     * equal comparator
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if(obj.getClass().equals(this.getClass())){
            Color c = (Color)obj;
            return this.colorName == c.colorName && this.colorValue == c.colorValue;
        }
        return false;
    }

    /**
     * toString
     * @return printed value of the object
     */
    @Override
    public String toString(){
        return "<" + this.colorName + ": " + this.colorValue + ">";
    }

    /**
     * hashcode
     * @return hashcode of the object
     */
    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
}
