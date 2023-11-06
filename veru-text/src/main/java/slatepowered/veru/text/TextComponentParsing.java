package slatepowered.veru.text;

import slatepowered.veru.string.StringReader;

import java.awt.*;

/**
 * Support methods for parsing a legacy+ like string
 * into text components.
 */
final class TextComponentParsing {

    public static CompoundComponent parseLegacyPlusLike(StringReader reader, char codeSymbol) {
        CompoundComponent component = new CompoundComponent(); // The compound component
        StringBuilder buf = new StringBuilder();               // The buffer for literals
        Style style = Style.empty();                           // The current style
        while (!reader.ended()) {
            char c = reader.curr();

            // escape char
            if (c == '\\') {
                buf.append(reader.next());
                reader.next();
                continue;
            }

            // color code
            if (c == codeSymbol) {
                char c2 = reader.next();

                // check for reset
                if (c2 == 'r') {
                    if (buf.length() != 0) {
                        MutableComponent comp = TextComponent.text(buf.toString());
                        comp.setStyle(style);
                        style = Style.empty();
                        buf.delete(0, buf.length());
                        component.append(comp);
                    }

                    reader.next();
                    continue;
                }

                TextColor color = null;

                // check for hex-code
                if (c2 == '#') {
                    String hexCode = reader.read(7);
                    if (hexCode.length() < 7) {
                        throw new IllegalArgumentException("Incomplete hex code for &# color code (expected 6 chars)");
                    }

                    color = TextColor.of(Color.decode(hexCode));
                } else {
                    // check for color chars
                    switch (c2) {
                        case '0': color = TextColors.BLACK; break;
                        case '1': color = TextColors.DARK_BLUE; break;
                        case '2': color = TextColors.DARK_GREEN; break;
                        case '3': color = TextColors.DARK_AQUA; break;
                        case '4': color = TextColors.DARK_RED; break;
                        case '5': color = TextColors.DARK_PURPLE; break;
                        case '6': color = TextColors.GOLD; break;
                        case '7': color = TextColors.GRAY; break;
                        case '8': color = TextColors.DARK_GRAY; break;
                        case '9': color = TextColors.BLUE; break;
                        case 'a': color = TextColors.GREEN; break;
                        case 'b': color = TextColors.AQUA; break;
                        case 'c': color = TextColors.RED; break;
                        case 'd': color = TextColors.PURPLE; break;
                        case 'e': color = TextColors.YELLOW; break;
                        case 'f': color = TextColors.WHITE; break;
                    }

                    reader.next();
                }

                if (color != null) {
                    if (buf.length() != 0) {
                        MutableComponent comp = TextComponent.text(buf.toString());
                        comp.setStyle(style);
                        style = Style.empty();
                        buf.delete(0, buf.length());
                        component.append(comp);
                    }

                    style.color = color;
                    continue;
                }

                // handle formatting chars
                switch (c2) {
                    case 'l': style.bold = true; continue;
                    case 'o': style.italic = true; continue;
                    case 'n': style.underline = true; continue;
                    case 'm': style.strikethrough = true; continue;
                    case 'k': style.obfuscated = true; continue;
                }

                // throw error
                throw new IllegalArgumentException("Unknown formatting code after ampersand `" + c2 + "`");
            }

            // append char
            buf.append(c);
            reader.next();
        }

        // add last segment with proper formatting
        if (buf.length() != 0) {
            MutableComponent comp = TextComponent.text(buf.toString());
            comp.setStyle(style);
            component.append(comp);
        }

        return component;
    }

}
