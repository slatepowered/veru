package com.slatepowered.veru.text;

import java.util.Stack;

/**
 * The text context/stack used in serialization.
 *
 * @param <A> The accumulator.
 */
public class TextSerializationContext<A> extends TextContext {

    /**
     * The stack of accumulators.
     */
    protected final Stack<A> accumulatorStack = new Stack<>();

    /**
     * The stack of components.
     */
    protected final Stack<TextComponent> componentStack = new Stack<>();

    /**
     * The style stack.
     */
    protected final StyleStack styleStack = new StyleStack();

    public A accumulator() {
        if (componentStack.size() < 1)
            return null;
        return accumulatorStack.peek();
    }

    public A lastAccumulator() {
        if (componentStack.size() < 2)
            return null;
        return accumulatorStack.get(accumulatorStack.size() - 2);
    }

    public Stack<A> getAccumulatorStack() {
        return accumulatorStack;
    }

    public TextComponent component() {
        if (componentStack.size() < 1)
            return null;
        return componentStack.peek();
    }

    public TextComponent lastComponent() {
        if (componentStack.size() < 2)
            return null;
        return componentStack.get(componentStack.size() - 2);
    }

    public Stack<TextComponent> getComponentStack() {
        return componentStack;
    }

    public StyleStack getStyleStack() {
        return styleStack;
    }

    public void end() {
        componentStack.pop();
        accumulatorStack.pop();
    }

}
