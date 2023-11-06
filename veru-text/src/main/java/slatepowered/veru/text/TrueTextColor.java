package slatepowered.veru.text;

import java.awt.*;

/**
 * A true text color.
 */
public class TrueTextColor implements TextColor {

    public static TrueTextColor of(Color color) {
        return new TrueTextColor(color, color.getRed() + "," + color.getGreen() + "," + color.getGreen());
    }

    public static TrueTextColor of(Color color, String name) {
        return new TrueTextColor(color, name);
    }

    private final Color trueColor; // The RGB color
    private final String name;     // The name of this text color

    private TrueTextColor(Color trueColor, String name) {
        this.trueColor = trueColor;
        this.name = name;
    }

    @Override
    public boolean isTrue() {
        return true;
    }

    @Override
    public Color getTrueColor() {
        return trueColor;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TrueTextColor(" + name + ")";
    }

}
