package com.slatepowered.veru.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

public class BungeeComponentTextSerializer implements TextSerializer<ComponentBuilder, TextSerializationContext<ComponentBuilder>, BaseComponent> {

    @Override
    public TextSerializationContext<ComponentBuilder> newContext() {
        return new TextSerializationContext<>();
    }

    @Override
    public ComponentBuilder newAccumulator() {
        return new ComponentBuilder();
    }

    @Override
    public BaseComponent finalize(TextSerializationContext<ComponentBuilder> ctx) {
        return new net.md_5.bungee.api.chat.TextComponent(ctx.accumulator().create());
    }

    @Override
    public ComponentBuilder next(TextSerializationContext<ComponentBuilder> ctx) {
        return new ComponentBuilder().retain(ComponentBuilder.FormatRetention.NONE);
    }

    @Override
    public void write(TextSerializationContext<ComponentBuilder> ctx) {
        ComponentBuilder builder = ctx.accumulator();
        TextComponent component = ctx.component();

        ctx.getStyleStack().push(component.getStyle());
        applyStyle(builder, ctx.getStyleStack().buildStyle());

        if /* client translatable */ (component instanceof TranslatableComponent) {
            builder.append(new net.md_5.bungee.api.chat.TranslatableComponent(((TranslatableComponent)component).getKey()));
        } /* any other */ else {
            builder.append(component.getText(ctx));
        }
    }

    @Override
    public void end(TextSerializationContext<ComponentBuilder> ctx) {
        ctx.getStyleStack().pop();
        ctx.lastAccumulator().append(ctx.accumulator().create());
    }

    // set the style of the component builder
    private void applyStyle(ComponentBuilder builder, Style style) {
        if (style.getBold() != null) builder.bold(style.getBold());
        if (style.getItalic() != null) builder.italic(style.getItalic());
        if (style.getUnderline() != null) builder.underlined(style.getUnderline());
        if (style.getStrikethrough() != null) builder.strikethrough(style.getStrikethrough());
        if (style.getObfuscated() != null) builder.obfuscated(style.getObfuscated());
        if (style.getColor() != null) builder.color(BungeeTextSerializerSupport.toBungeeChatColor(style.getColor()));

        for (Object o : style.getProperties().values()) {
            if (o instanceof ClickEvent) {
                builder.event((ClickEvent)o);
            } else if (o instanceof HoverEvent) {
                builder.event((HoverEvent)o);
            }
        }
    }

}
