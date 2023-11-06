package com.slatepowered.veru.text;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class BungeeTextSerializerSupport {

    /** Whether this version supports true color */
    private static boolean supportsTrueColor;

    static {
        try {
            ChatColor.class.getMethod("of", Color.class);
            supportsTrueColor = true;
        } catch (NoSuchMethodException e) {
            supportsTrueColor = false;
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    public static boolean isSupportsTrueColor() {
        return supportsTrueColor;
    }

    public static ChatColor toBungeeChatColor(TextColor color) {
        if (color.isTrue()) {
            if (!supportsTrueColor) {
                // todo: map true color -> common color
            } else {
                // create true color
                return ChatColor.of(color.getTrueColor());
            }
        } else if (color instanceof TextColors) {
            TextColors tc = (TextColors) color;
            ChatColor cc = null;
            switch (tc) {
                case RED: cc = ChatColor.RED; break;
                case DARK_RED: cc = ChatColor.DARK_RED; break;
                case GREEN: cc = ChatColor.GREEN; break;
                case DARK_GREEN: cc = ChatColor.DARK_GREEN; break;
                case BLUE: cc = ChatColor.BLUE; break;
                case DARK_BLUE: cc = ChatColor.DARK_BLUE; break;
                case AQUA: cc = ChatColor.AQUA; break;
                case DARK_AQUA: cc = ChatColor.DARK_AQUA; break;
                case YELLOW: cc = ChatColor.YELLOW; break;
                case GOLD: cc = ChatColor.GOLD; break;
                case PURPLE: cc = ChatColor.LIGHT_PURPLE; break;
                case DARK_PURPLE: cc = ChatColor.DARK_PURPLE; break;
                case WHITE: cc = ChatColor.WHITE; break;
                case GRAY: cc = ChatColor.GRAY; break;
                case DARK_GRAY: cc = ChatColor.DARK_GRAY; break;
                case BLACK: cc = ChatColor.BLACK; break;
            }

            return cc;
        }

        // throw error for unsupported
        throw new UnsupportedOperationException("Unmappable color " + color + " of type ");
    }

}
