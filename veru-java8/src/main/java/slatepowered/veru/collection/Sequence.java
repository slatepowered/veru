package slatepowered.veru.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Basically just an extension of {@link ArrayList} with a
 * couple utility methods.
 *
 * @param <T> The element type.
 */
public class Sequence<T> extends ArrayList<T> {

    /**
     * Creates a sequence from the given varargs array.
     *
     * @param arr The element array.
     * @param <T> The element type.
     * @return The sequence.
     */
    public static <T> Sequence<T> of(T... arr) {
        Sequence<T> sequence = new Sequence<>(arr.length);
        for (T e : arr) {
            sequence.add(e);
        }

        return sequence;
    }

    public Sequence(int initialCapacity) {
        super(initialCapacity);
    }

    public Sequence() {

    }

    public Sequence(Collection<? extends T> c) {
        super(c);
    }

    /**
     * Filters the elements in this sequence into another sequence.
     *
     * @param predicate The predicate/filter.
     * @return The result sequence.
     */
    public Sequence<T> filter(Predicate<T> predicate) {
        Sequence<T> sequence = new Sequence<>();
        for (T element : this) {
            if (predicate.test(element)) {
                sequence.add(element);
            }
        }

        return sequence;
    }

    /**
     * Maps the elements in this set to the result type using
     * the given mapping function.
     *
     * @param mappingFunc The mapping function.
     * @param <R> The result type.
     * @return The result sequence.
     */
    public <R> Sequence<R> map(Function<T, R> mappingFunc) {
        Sequence<R> sequence = new Sequence<>();
        for (T element : this) {
            sequence.add(mappingFunc.apply(element));
        }

        return sequence;
    }

    public Optional<T> issue(int index) {
        return (index < 0 || index >= size()) ? Optional.empty() : Optional.of(get(index));
    }

    public T getLast() {
        if (isEmpty())
            throw new IllegalArgumentException("Sequence is empty");
        return get(size() - 1);
    }

    public T getFirst() {
        if (isEmpty())
            throw new IllegalArgumentException("Sequence is empty");
        return get(0);
    }

    public Optional<T> last() {
        return issue(size() - 1);
    }

    public Optional<T> first() {
        return issue(0);
    }

}
