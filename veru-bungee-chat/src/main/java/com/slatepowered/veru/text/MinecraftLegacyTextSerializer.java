package com.slatepowered.veru.text;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.lang.reflect.Field;

public class MinecraftLegacyTextSerializer implements TextSerializer<StringBuilder, TextSerializationContext<StringBuilder>, String> {

    @Override
    public TextSerializationContext<StringBuilder> newContext() {
        return new TextSerializationContext<>();
    }

    @Override
    public StringBuilder newAccumulator() {
        return new StringBuilder();
    }

    @Override
    public String finalize(TextSerializationContext<StringBuilder> ctx) {
        return ctx.accumulator().toString();
    }

    @Override
    public StringBuilder next(TextSerializationContext<StringBuilder> ctx) {
        return ctx.accumulator() == null ? new StringBuilder() : newAccumulator();
    }

    @Override
    public void write(TextSerializationContext<StringBuilder> ctx) {
        // apply the style
        ctx.accumulator().append(ChatColor.RESET);
        Style style = ctx.component().getStyle();
        ctx.getStyleStack().push(style);
        applyStyle(ctx.accumulator(), ctx.getStyleStack().buildStyle());

        String text = ctx.component().getText(ctx);
        if (text != null) {
            ctx.accumulator().append(text);
        }
    }

    @Override
    public void end(TextSerializationContext<StringBuilder> ctx) {
        ctx.accumulator().append(ChatColor.RESET);

        // reapply style
        ctx.getStyleStack().pop();
    }

    // applies (enables) the given style
    // using ChatColor in the given string builder
    public void applyStyle(StringBuilder b, Style style) {
        b.append(BungeeTextSerializerSupport.toBungeeChatColor(style.getColor()));

        if (style.getBold() == Boolean.TRUE) b.append(ChatColor.BOLD);
        if (style.getItalic() == Boolean.TRUE) b.append(ChatColor.ITALIC);
        if (style.getUnderline() == Boolean.TRUE) b.append(ChatColor.UNDERLINE);
        if (style.getStrikethrough() == Boolean.TRUE) b.append(ChatColor.STRIKETHROUGH);
        if (style.getObfuscated() == Boolean.TRUE) b.append(ChatColor.MAGIC);
    }

}
