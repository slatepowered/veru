package com.slatepowered.veru.string.formatv1;

import java.awt.*;

/**
 * Represents a formatting specification, such as ANSI/VT100.
 */
public interface Formatting {

    interface CommonFormatting extends Formatting {

        void enableStyle(StringBuilder b, CommonStyle style);
        void enableTrueColor(StringBuilder b, Color color);
        void enableCommonColor(StringBuilder b, CommonColor color);

        @Override
        default void enable(StringBuilder b, Object format) {
            if (format instanceof CommonStyle) enableStyle(b, (CommonStyle) format);
            if (format instanceof CommonColor) enableCommonColor(b, (CommonColor) format);
            if (format instanceof Color) enableTrueColor(b, (Color) format);

            throw new IllegalArgumentException("`" + format + "` is not a valid format specifier");
        }


    }

    /**
     * Ignores all formatting commands received, leaving the
     * string as plain text.
     */
    Formatting NONE = new Formatting() {
        @Override
        public void enable(StringBuilder b, Object format) { }
        @Override
        public void reset(StringBuilder b) { }
    };

    Formatting ANSI = new CommonFormatting() {
        @Override
        public void enableCommonColor(StringBuilder b, CommonColor color) {
            String code = null;
            switch (color) {
                case RED: code = com.slatepowered.veru.misc.ANSI.RED; break;
                case BLUE: code = com.slatepowered.veru.misc.ANSI.BLUE; break;
                case GREEN: code = com.slatepowered.veru.misc.ANSI.GREEN; break;
                case YELLOW: code = com.slatepowered.veru.misc.ANSI.YELLOW; break;
                case AQUA:
                case CYAN: code = com.slatepowered.veru.misc.ANSI.CYAN; break;
                case PURPLE: code = com.slatepowered.veru.misc.ANSI.PURPLE; break;
                case BLACK: code = com.slatepowered.veru.misc.ANSI.BLACK; break;
                case WHITE: code = com.slatepowered.veru.misc.ANSI.WHITE; break;
            }

            // todo: support dark colors
            if (code != null) {
                b.append(code);
            }
        }

        @Override
        public void enableStyle(StringBuilder b, CommonStyle style) {
            // todo
        }

        @Override
        public void enableTrueColor(StringBuilder b, Color color) {
            b.append(com.slatepowered.veru.misc.ANSI.getRGBForeground(color.getRed(), color.getGreen(), color.getBlue()));
        }

        @Override
        public void reset(StringBuilder b) {
            b.append(com.slatepowered.veru.misc.ANSI.RESET);
        }
    };

    /**
     * Enables the given formatting for the given string builder
     * until it is disabled or reset.
     *
     * @param b The builder.
     * @param format The format.
     */
    void enable(StringBuilder b, Object format);

    /**
     * Resets all current formatting in the given string builder.
     *
     * @param b The builder.
     */
    void reset(StringBuilder b);

}
