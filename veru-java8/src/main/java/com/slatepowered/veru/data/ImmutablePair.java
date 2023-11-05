package com.slatepowered.veru.data;

import lombok.Data;

/**
 * Represents a pair of values.
 *
 * @param <X> The type of the first value.
 * @param <Y> The type of the second value.
 */
@Data
public class ImmutablePair<X, Y> implements Pair<X, Y> {
    private final X first;
    private final Y second;

    public ImmutablePair<Y, X> reverse() {
        return new ImmutablePair<>(second, first);
    }

    @Override
    public Pair<X, Y> setFirst(X v) {
        return new ImmutablePair<>(v, second);
    }

    @Override
    public Pair<X, Y> setSecond(Y v) {
        return new ImmutablePair<>(first, v);
    }
}
