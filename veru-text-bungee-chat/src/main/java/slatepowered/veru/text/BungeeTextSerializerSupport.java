package slatepowered.veru.text;

import slatepowered.veru.misc.Throwables;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BungeeTextSerializerSupport {

    /** Whether this version supports true color */
    private static boolean supportsTrueColor;

    /** The parts field in the ComponentBuilder */
    private static boolean hasAppendComponentMethod;
    private static Field FIELD_ComponentBuilder_parts;
    private static Field FIELD_ComponentBuilder_current;

    static {
        try {
            ChatColor.class.getMethod("of", Color.class);
            supportsTrueColor = true;
        } catch (NoSuchMethodException e) {
            supportsTrueColor = false;
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }

        try {
            FIELD_ComponentBuilder_parts = ComponentBuilder.class.getDeclaredField("parts");
            FIELD_ComponentBuilder_parts.setAccessible(true);

            try {
                FIELD_ComponentBuilder_current = ComponentBuilder.class.getDeclaredField("current");
                FIELD_ComponentBuilder_current.setAccessible(true);
                hasAppendComponentMethod = false;
            } catch (NoSuchFieldException e) {
                try {
                    ComponentBuilder.class.getMethod("append", BaseComponent[].class);
                    ComponentBuilder.class.getMethod("append", BaseComponent.class);
                    hasAppendComponentMethod = true;
                } catch (NoSuchMethodException e2) {
                    throw new IllegalStateException("bruh");
                }
            }
        } catch (Throwable t) {
            throw new ExceptionInInitializerError(t);
        }
    }

    public static boolean isSupportsTrueColor() {
        return supportsTrueColor;
    }

    public static ChatColor toBungeeChatColor(TextColor color) {
        if (color == null)
            return null;

        if (color.isTrue()) {
            if (!supportsTrueColor) {
                // todo: map true color -> common color
            } else {
                // create true color
                return ChatColor.of(color.getTrueColor());
            }
        } else if (color instanceof TextColors) {
            // handle named/common text colors
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

    // Old ComponentBuilder support: finalizes and adds the current/previous part
    private static ComponentBuilder finalizeCurrent(ComponentBuilder b, BaseComponent newComponent) {
        try {
            addPartsToComponentBuilderLegacy(b, (BaseComponent) FIELD_ComponentBuilder_current.get(b));
            FIELD_ComponentBuilder_current.set(b, newComponent);
            return b;
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
            return null;
        }
    }

    // Old ComponentBuilder support: adds the given parts to the part array
    @SuppressWarnings("unchecked")
    private static ComponentBuilder addPartsToComponentBuilderLegacy(ComponentBuilder b, BaseComponent... component) {
        try {
            List<BaseComponent> parts = (List<BaseComponent>) FIELD_ComponentBuilder_parts.get(b);
            parts.addAll(Arrays.asList(component));
            return b;
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
            return null;
        }
    }

    // Cross-version ComponentBuilder support: appends the given parts
    public static ComponentBuilder appendComponents(ComponentBuilder b, BaseComponent... components) {
        if (hasAppendComponentMethod) {
            return b.append(components);
        }

        finalizeCurrent(b, components[components.length - 1]);
        return addPartsToComponentBuilderLegacy(b, Arrays.copyOf(components, components.length - 1));
    }

}
