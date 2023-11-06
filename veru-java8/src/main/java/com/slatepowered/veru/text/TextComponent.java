package com.slatepowered.veru.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a slice of text which contains information
 * about the styling and content of that text.
 *
 * This can include child components making it a nested tree-like
 * structure.
 */
public abstract class TextComponent {

    public static CompoundComponent of(TextComponent... components) {
        return new CompoundComponent(new ArrayList<>(Arrays.asList(components)));
    }

    public static CompoundComponent of(List<TextComponent> components) {
        return new CompoundComponent(components);
    }

    public static LiteralComponent literal(String text) {
        return new LiteralComponent(text);
    }

    public static TranslatableComponent translatable(String key) {
        return new TranslatableComponent(key);
    }

    public static MutableComponent supplied(Function<TextContext, String> function) {
        return new MutableComponent() {
            @Override
            public String getText(TextContext context) {
                return function.apply(context);
            }
        };
    }

    /**
     * Get the child components of this component.
     *
     * @return The children.
     */
    public abstract List<TextComponent> getChildren();

    /**
     * Get the style set by this component, should return
     * an empty style if no style is set.
     *
     * @return The style.
     */
    public abstract Style getStyle();

    /**
     * Get the plain (not stylized) text for this
     * component in the given context.
     *
     * @return The text.
     */
    public abstract String getText(TextContext context);

}
