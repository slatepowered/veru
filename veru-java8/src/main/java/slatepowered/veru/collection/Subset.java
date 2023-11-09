package slatepowered.veru.collection;

import java.util.*;
import java.util.function.Predicate;

/**
 * Represents an, optionally not continuous, immutable sub-list of objects
 * taken from a source collection.
 *
 * @param <T> The component type.
 */
@SuppressWarnings("unchecked")
public class Subset<T> implements Iterable<T>, List<T> {

    // creates a primitive array from the given list
    private static int[] toIntArray(List<Integer> list) {
        final int l = list.size();
        int[] arr = new int[l];
        for (int i = 0; i < l; i++) {
            arr[i] = list.get(i);
        }

        return arr;
    }

    /**
     * The source list.
     */
    private final List<Object> source;

    /**
     * The list of indices.
     */
    private final int[] indices;

    public Subset(List<Object> source, int[] indices) {
        this.source = source;
        this.indices = indices;
    }

    /**
     * Check if the sub-list is empty.
     *
     * @return Whether the list is empty.
     */
    @Override
    public boolean isEmpty() {
        return indices.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        return lastIndexOf(o) != -1;
    }

    /**
     * Get the amount of components in the list.
     *
     * @return The amount.
     */
    @Override
    public int size() {
        return indices.length;
    }

    /**
     * Get the component at the given index.
     *
     * @param i The index.
     * @return The component.
     */
    @Override
    public T get(int i) {
        return (T) source.get(indices[i]);
    }

    public Optional<T> at(int i) {
        if (i < 0 || i >= size())
            return Optional.empty();
        return Optional.of((T) source.get(indices[i]));
    }

    public Optional<T> first() {
        return isEmpty() ? Optional.empty() : Optional.of(get(0));
    }

    public Optional<T> last() {
        return isEmpty() ? Optional.empty() : Optional.of(get(indices.length - 1));
    }

    /**
     * Filter the components in the list into another list.
     *
     * @param predicate The predicate.
     * @return The new sub-list.
     */
    public Subset<T> filter(Predicate<T> predicate) {
        List<Integer> newIndices = new ArrayList<>();
        final int l = this.indices.length;
        int c = 0;
        for (int i = 0; i < l; i++) {
            int idx = this.indices[i];
            Object component = source.get(idx);
            if (predicate.test((T) component)) {
                newIndices.add(idx);
            }
        }

        return new Subset<>(this.source, toIntArray(newIndices));
    }

    public List<T> collect() {
        final int l = size();
        List<T> list = new ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            list.add((T) source.get(indices[i]));
        }

        return list;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                return get(i++);
            }
        };
    }

    @Override
    public int indexOf(Object o) {
        final int l = size();
        for (int i = 0; i < l; i++) {
            T element = get(i);
            if (element.equals(o))
                return i;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size() - 1; i >= 0; i++) {
            T element = get(i);
            if (element.equals(o))
                return i;
        }

        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean contains = true;
        for (Object o : c) {
            if (lastIndexOf(o) != -1) {
                contains = false;
            }
        }

        return contains;
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                return get(i++);
            }

            @Override
            public boolean hasPrevious() {
                return i > 0;
            }

            @Override
            public T previous() {
                return get(--i);
            }

            @Override
            public int nextIndex() {
                return i + 1;
            }

            @Override
            public int previousIndex() {
                return i - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("A subset is immutable");
            }

            @Override
            public void set(T t) {
                throw new UnsupportedOperationException("A subset is immutable");
            }

            @Override
            public void add(T t) {
                throw new UnsupportedOperationException("A subset is immutable");
            }
        };
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null; // TODO
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public Object[] toArray() {
        return collect().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return collect().<T1>toArray(a);
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("A subset is immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("A subset is immutable");
    }

}
