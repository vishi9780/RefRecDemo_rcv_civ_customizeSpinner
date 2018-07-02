package responsehandling.app.com.civ;

import android.graphics.Color;


public class TextDrawer {
    private String text;
    private int backgroundColor;
    private int textColor;

    public TextDrawer() {
        backgroundColor = Color.BLUE;
        textColor = Color.WHITE;
    }

    //Setters
    public TextDrawer setText(String text) {
        this.text = text;
        return this;
    }

    public TextDrawer setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public TextDrawer setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    //Getters
    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public String getText() {
        return text;
    }
}
