package com.slatepowered.veru.text;

import java.util.HashMap;
import java.util.Stack;

public class StyleStack extends Stack<Style> {

    /**
     * Combine all styles on the stack into one style.
     *
     * @return The combined style.
     */
    public Style buildStyle() {
        // todo: maybe optimize/cache this shit
        Style.StyleBuilder builder = Style.builder();
        builder.properties(new HashMap<>());

        for (Style style : this) {
            // copy properties
            if (style.getBold() != null) builder.bold(style.getBold());
            if (style.getItalic() != null) builder.italic(style.getItalic());
            if (style.getUnderline() != null) builder.underline(style.getUnderline());
            if (style.getStrikethrough() != null) builder.strikethrough(style.getStrikethrough());
            if (style.getObfuscated() != null) builder.obfuscated(style.getObfuscated());
            if (style.getColor() != null) builder.color(style.getColor());

            builder.properties.putAll(style.getProperties());
        }

        return builder.build();
    }

}
