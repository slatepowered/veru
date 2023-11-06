package com.slatepowered.veru.text;

import java.awt.*;

/**
 * Represents a text color.
 */
public interface TextColor {

    static TrueTextColor of(Color color) {
        return TrueTextColor.of(color);
    }

    static TrueTextColor of(Color color, String name) {
        return TrueTextColor.of(color, name);
    }

    /**
     * @return Whether this color is a true (RGB) color.
     */
    boolean isTrue();

    /**
     * @return The true color if present, or null if not.
     */
    Color getTrueColor();

    /**
     * @return The name of this color.
     */
    String getName();

}
