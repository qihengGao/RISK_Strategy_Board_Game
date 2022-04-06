package edu.duke.ece651.risk.shared.territory;

import java.io.Serializable;


//save the color information
public class Color implements Serializable {

    private final String colorName;
    private final String colorValue;

    public Color(String _colorName){
        this.colorName = _colorName;
        this.colorValue = _colorName;
    }

    public Color(String _colorName, String _colorvalue){
        this.colorName = _colorName;
        this.colorValue = _colorvalue;
    }

    public String getColorName(){
        return colorName;
    }

    public String getColorValue(){
        return colorValue;
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass().equals(this.getClass())){
            Color c = (Color)obj;
            return this.colorName == c.colorName && this.colorValue == c.colorValue;
        }
        return false;
    }

    @Override
    public String toString(){
        return "<" + this.colorName + ": " + this.colorValue + ">";
    }

    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }
}
