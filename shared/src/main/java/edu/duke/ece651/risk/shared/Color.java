package edu.duke.ece651.risk.shared;

public class Color<T>{
    private final String colorName;
    private final T colorValue;

    public Color(String _colorName, T _colorvalue){
        this.colorName = _colorName;
        this.colorValue = _colorvalue;
    }

    public String getColorName(){
        return this.colorName;
    }

    public T getColorValue(){
        return this.colorValue;
    }
}
