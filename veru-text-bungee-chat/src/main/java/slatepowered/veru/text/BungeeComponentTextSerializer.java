package slatepowered.veru.text;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import static slatepowered.veru.text.BungeeTextSerializerSupport.appendComponents;

public class BungeeComponentTextSerializer implements TextSerializer<ComponentBuilder, BungeeTextSerializationContext, BaseComponent> {

    public static BungeeComponentTextSerializer bungeeComponents() {
        return new BungeeComponentTextSerializer();
    }

    @Override
    public BungeeTextSerializationContext newContext() {
        return new BungeeTextSerializationContext();
    }

    @Override
    public ComponentBuilder newAccumulator() {
        return new ComponentBuilder("");
    }

    @Override
    public BaseComponent finalize(BungeeTextSerializationContext ctx) {
        return new net.md_5.bungee.api.chat.TextComponent(ctx.accumulator().create());
    }

    @Override
    public ComponentBuilder next(BungeeTextSerializationContext ctx) {
        return new ComponentBuilder("").retain(ComponentBuilder.FormatRetention.NONE);
    }

    @Override
    public void write(BungeeTextSerializationContext ctx) {
        ComponentBuilder builder = ctx.accumulator();
        TextComponent component = ctx.component();

        ctx.getStyleStack().push(component.getStyle());
        applyStyle(builder, ctx.getStyleStack().buildStyle());

        if /* client translatable */ (component instanceof TranslatableComponent) {
            BungeeTextSerializerSupport.appendComponents(builder, new net.md_5.bungee.api.chat.TranslatableComponent(((TranslatableComponent)component).getKey()));
        } /* any other */ else {
            String text = component.getText(ctx);
            if (text != null) {
                builder.append(text);
            }
        }
    }

    @Override
    public void end(BungeeTextSerializationContext ctx) {
        ctx.getStyleStack().pop();
        BungeeTextSerializerSupport.appendComponents(ctx.lastAccumulator(), ctx.accumulator().create());
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
            } else if (o instanceof slatepowered.veru.text.HoverEvent) {
                slatepowered.veru.text.HoverEvent e = (slatepowered.veru.text.HoverEvent) o;
                builder.event(new HoverEvent(HoverEvent.Action.valueOf(e.getAction().name()), new BaseComponent[] { this.serialize(e.getValue()) }));
            }
        }
    }

}
