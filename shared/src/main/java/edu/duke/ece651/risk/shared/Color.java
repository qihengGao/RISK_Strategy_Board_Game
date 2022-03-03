package edu.duke.ece651.risk.shared;

public class Color{
    private final String colorName;
    private final String colorValue;

    public Color(String _colorName, String _colorvalue){
        this.colorName = _colorName;
        this.colorValue = _colorvalue;
    }

    public String getColorName(){
        return this.colorName;
    }

    public String getColorValue(){
        return this.colorValue;
    }
}
