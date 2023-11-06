package slatepowered.veru.data;

import java.util.function.Function;

/**
 * Represents a pair of values.
 *
 * @param <X> The type of the first value.
 * @param <Y> The type of the second value.
 */
public interface Pair<X, Y> {

    /**
     * Create an immutable pair containing the given
     * values.
     *
     * @param first The first value.
     * @param second The second value.
     * @param <X> The first value type.
     * @param <Y> The second value type.
     * @return The pair.
     */
    static <X, Y> Pair<X, Y> of(X first, Y second) {
        return new ImmutablePair<>(first, second);
    }

    /**
     * Get the first value in the pair.
     *
     * @return The first value.
     */
    X getFirst();

    /**
     * Get the second value in the pair.
     *
     * @return The second value.
     */
    Y getSecond();

    /**
     * Set the first value of the pair to the given
     * value.
     *
     * @param v The value.
     * @return The resulting pair, which may be this instance or a new pair.
     */
    Pair<X, Y> setFirst(X v);

    /**
     * Set the second value of the pair to the given
     * value.
     *
     * @param v The value.
     * @return The resulting pair, which may be this instance or a new pair.
     */
    Pair<X, Y> setSecond(Y v);

    /**
     * Create a dynamically mapping pair with
     * the given mapping functions.
     *
     * The value is not cached, instead it maps
     * each value every time it is queried.
     *
     * @param fMap The mapping for the first value.
     * @param sMap The mapping for the second value.
     * @param <X2> The resulting first type.
     * @param <Y2> The resulting second type.
     * @return The resulting mapped pair.
     */
    default <X2, Y2> Pair<X2, Y2> map(Function<X, X2> fMap, Function<Y, Y2> sMap) {
        final Pair<X, Y> pair = this;
        return new Pair<X2, Y2>() {
            @Override
            public X2 getFirst() {
                return fMap.apply(pair.getFirst());
            }

            @Override
            public Y2 getSecond() {
                return sMap.apply(pair.getSecond());
            }

            @Override
            public Pair<X2, Y2> setFirst(X2 v) {
                throw new UnsupportedOperationException("This pair is immutable");
            }

            @Override
            public Pair<X2, Y2> setSecond(Y2 v) {
                throw new UnsupportedOperationException("This pair is immutable");
            }
        };
    }

}
