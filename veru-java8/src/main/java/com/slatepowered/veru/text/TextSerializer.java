package com.slatepowered.veru.text;

/**
 * Serializes a tree of text components to a different format.
 *
 * @param <A> The accumulator type.
 * @param <R> The result type.
 */
public interface TextSerializer<A, C extends TextSerializationContext<A>, R> {

    /**
     * Creates a new context at the start of
     * serialization.
     *
     * @return The context.
     */
    C newContext();

    /**
     * Creates a new base accumulator at the start
     * of serialization.
     *
     * @return The accumulator.
     */
    A newAccumulator();

    /**
     * Builds the given accumulator to the final
     * result to be returned by the serializer.
     *
     * @param ctx The serialization context.
     * @return The final result.
     */
    R finalize(C ctx);

    /**
     * Called when the serialization moves deeper
     * into the component tree, looking at another component.
     *
     * The returned accumulator is used for serialization
     * of the children of that component. If null is returned,
     * the component is serialized and it moves back.
     *
     * @param ctx The serialization context.
     * @return The new accumulator.
     */
    A next(C ctx);

    /**
     * Serializes the component itself.
     *
     * @param ctx The context.
     */
    void write(C ctx);

    /**
     * End the serialization of the given component.
     *
     * This is expected to finalize the serialization
     * of the component and append the new accumulator to
     * the base accumulator if needed.
     *
     * @param ctx The serialization context.
     */
    void end(C ctx);

    default void serialize(C ctx, TextComponent component) {
        ctx.componentStack.push(component);
        ctx.accumulatorStack.push(next(ctx));
        write(ctx);

        if (component != null && component.getChildren() != null) {
            for (TextComponent child : component.getChildren()) {
                serialize(ctx, child);
            }
        }

        end(ctx);
        ctx.end();
    }

    default R serialize(TextComponent component) {
        C ctx = newContext();
        serialize(ctx, component);

        return finalize(ctx);
    }

}
