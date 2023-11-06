package com.slatepowered.veru.text;

import java.awt.*;

public enum TextColors implements TextColor {

    RED,
    DARK_RED,
    GREEN,
    DARK_GREEN,
    BLUE,
    DARK_BLUE,
    AQUA,
    DARK_AQUA,
    YELLOW,
    GOLD,
    PURPLE,
    DARK_PURPLE,
    WHITE,
    GRAY,
    DARK_GRAY,
    BLACK

    ;

    @Override
    public boolean isTrue() {
        return false;
    }

    @Override
    public Color getTrueColor() {
        return null;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

}
