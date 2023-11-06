package slatepowered.veru.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class MutableComponent extends TextComponent {

    public MutableComponent() {
        this.children = new ArrayList<>();
    }

    public MutableComponent(List<TextComponent> children) {
        this.children = children;
    }

    /**
     * The children of this component.
     */
    protected final List<TextComponent> children;

    /**
     * The style currently applied.
     */
    protected Style style = Style.empty();

    @Override
    public Style getStyle() {
        return style == null ? (style = Style.empty()) : style;
    }

    public MutableComponent setStyle(Style style) {
        this.style = style;
        return this;
    }

    @Override
    public List<TextComponent> getChildren() {
        return children;
    }

    public MutableComponent append(TextComponent child) {
        this.children.add(child);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <C extends TextComponent> C get(int idx) {
        return (C) children.get(idx);
    }

    public <C extends MutableComponent> MutableComponent edit(int idx, Consumer<C> consumer) {
        C component = get(idx);
        if (consumer != null)
            consumer.accept(component);
        return this;
    }

    /* Style and property modifications */

    public MutableComponent set(String name, Object val) {
        style.createPropertiesIfAbsent();
        style.properties.put(name, val);
        return this;
    }

    public MutableComponent add(Object val) {
        style.createPropertiesIfAbsent();
        style.properties.put("anon " + Long.toHexString(System.currentTimeMillis() ^ System.identityHashCode(val)), val);
        return this;
    }

    public MutableComponent color(TextColor color) {
        getStyle().color = color;
        return this;
    }

    public MutableComponent bold(Boolean b) {
        getStyle().bold = b;
        return this;
    }

    public MutableComponent italic(Boolean b) {
        getStyle().italic = b;
        return this;
    }

    public MutableComponent underline(Boolean b) {
        getStyle().underline = b;
        return this;
    }

    public MutableComponent strikethrough(Boolean b) {
        getStyle().strikethrough = b;
        return this;
    }

    public MutableComponent obfuscated(Boolean b) {
        getStyle().obfuscated = b;
        return this;
    }

}
