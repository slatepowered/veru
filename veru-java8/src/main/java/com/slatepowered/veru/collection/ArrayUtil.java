package com.slatepowered.veru.collection;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Utilities for working with arrays.
 */
public class ArrayUtil {

    /**
     * Represents a subsection/slice of an array with a given
     * start and end index.
     *
     * @param <T> The element type of the source array.
     */
    @Data
    public static class ArraySection<T> implements Iterable<T> {
        /**
         * The source array.
         */
        final T[] source;

        /**
         * The flat index of the start of the slice (inclusive).
         */
        final int start;

        /**
         * The flat index of the end of the slice (inclusive).
         */
        final int end;

        // Cache the length of this section.
        final int length;

        public ArraySection(T[] source, int start, int end) {
            this.source = source;
            this.start = start;
            this.end = end;
            this.length = (end - start) + 1;
        }

        /**
         * Get an element at the given index into the slice.
         *
         * @param i The index.
         * @return The element.
         * @throws ArrayIndexOutOfBoundsException If the index is out of bounds for the source array.
         */
        public T get(int i) {
            return source[start + i];
        }

        /**
         * @return The exclusive end index (endIndex + 1).
         */
        public int endExclusive() {
            return end + 1;
        }

        /**
         * @return The length of this slice.
         */
        public int length() {
            return length;
        }

        public ArraySection<T> sub(int start, int end) {
            return new ArraySection<>(source, this.start + start, this.start + end);
        }

        public ArraySection<T> withStart(int start) {
            return new ArraySection<>(source, start, end);
        }

        public ArraySection<T> withEnd(int end) {
            return new ArraySection<>(source, start, end);
        }

        public ArraySection<T> withRange(int start, int end) {
            return new ArraySection<>(source, start, end);
        }

        public ArraySection<T> forArray(T[] arr) {
            return new ArraySection<>(arr, start, end);
        }

        @SuppressWarnings("unchecked")
        public T[] repeat(int count) {
            final int l = length;
            T[] arr = (T[]) Array.newInstance(source.getClass().getComponentType(), l * count);
            for (int i = 0; i < count; i++) {
                System.arraycopy(source, this.start, arr, i * l, l);
            }

            return arr;
        }

        public ArraySection<T> findRepeatingSection() {
            return ArrayUtil.findRepeatingSection(this);
        }

        public List<ArraySection<T>> findNextRepeatingContinuous() {
            return ArrayUtil.findContinuousRepeatingSectionsForward(this);
        }

        public ArraySection<T> findContinuousRepeatingRange() {
            return ArrayUtil.findRange(findNextRepeatingContinuous());
        }

        public T[] arrayWithout() {
            return ArrayUtil.arrayWithout(this);
        }

        @Override
        public String toString() {
            return "[0x" + Integer.toHexString(System.identityHashCode(this.source)) + " range " + start + ":" + end + " len " + length + "]";
        }

        public String toStringWithElements() {
            return this + stringifyIterator(iterator()) + "";
        }

        @SuppressWarnings("unchecked")
        public T[] toArray() {
            T[] arr = (T[]) Array.newInstance(source.getClass().getComponentType(), length);
            System.arraycopy(source, start, arr, 0, length);
            return arr;
        }

        public Collection<T> toCollection() {
            return Collections.list(toEnumeration());
        }

        public Enumeration<T> toEnumeration() {
            return new Enumeration<T>() {
                int i = 0; // The current index

                @Override
                public boolean hasMoreElements() {
                    return i < length;
                }

                @Override
                public T nextElement() {
                    return get(i++);
                }
            };
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                int i = 0; // The current index

                @Override
                public boolean hasNext() {
                    return i < length;
                }

                @Override
                public T next() {
                    return get(i++);
                }
            };
        }
    }

    public static <T> ArraySection<T> wrap(T[] array) {
        return new ArraySection<>(array, 0, array.length - 1);
    }

    /**
     * Check how many elements from the given array at the start index
     * don't match the given section.
     *
     * @param arr The array to check from.
     * @param section The section to match with.
     * @param start The start index.
     * @return The amount of elements that DON'T match (0 for full match).
     */
    public static int matchForward(ArraySection<?> arr, int start, ArraySection<?> section) {
        int i = 0;
        int l = section.length();
        for (int l2 = arr.length() - start; i < l && i < l2; i++) {
            if (!Objects.equals(section.get(i), arr.get(start + i))) {
                break;
            }
        }

        return l - i;
    }

    /**
     * Check how many elements from the given array at the start index
     * don't match the given section.
     *
     * @param arr The array to check from.
     * @param section The list to match with.
     * @param start The start index.
     * @return The amount of elements that DON'T match (0 for full match).
     */
    public static int matchForward(ArraySection<?> arr, int start, List<?> section) {
        int i = 0;
        int l = section.size();
        for (int l2 = arr.length() - start; i < l && i < l2; i++) {
            if (!Objects.equals(section.get(i), arr.get(start + i))) {
                break;
            }
        }

        return l - i /*- 1*/;
    }

    /**
     * Finds the repeating section in the source array.
     *
     * @param source The source.
     * @param <T> The element type.
     * @return The repeating section indices.
     */
    public static <T> ArraySection<T> findRepeatingSection(ArraySection<T> source) {
        int bufferStart = 0;                // Where the following buffer starts
        List<T> buffer = new ArrayList<>(); // The current buffer

        while (true) {
            for (int i = bufferStart, l = source.length(); i < l; i++) {
                T currentElement = source.get(i);

                // try match current buffer
                if (!buffer.isEmpty() && matchForward(source, i, buffer) == 0) {
                    break;
                }

                buffer.add(currentElement);
            }

            if (buffer.size() != (source.length() - bufferStart)) {
                break;
            }

            bufferStart++;
            buffer.clear();
        }

        // return the array section
        return source.sub(bufferStart, bufferStart + buffer.size() - 1);
    }

    /**
     * Find the continuous repeating sections after the given section
     * matching the given array section.
     *
     * @param section The section.
     * @param <T> The element type.
     * @return The list of sections.
     */
    public static <T> List<ArraySection<T>> findContinuousRepeatingSectionsForward(ArraySection<T> section) {
        T[] arr   = section.source;
        int start = section.start;
        int len   = section.length();
        ArraySection<T> srcSec = wrap(arr);

        List<ArraySection<T>> list = new ArrayList<>();
        while (matchForward(srcSec, start, section) == 0) {
            list.add(new ArraySection<>(arr, start, start + len - 1));
            start += len;
        }

        return list;
    }

    /**
     * Find the full range section encapsulating all the sections.
     *
     * @param sections The sections.
     * @param <T> The value type.
     * @return The range section.
     */
    public static <T> ArraySection<T> findRange(List<ArraySection<T>> sections) {
        if (sections.isEmpty())
            return new ArraySection<>(null, 0, 0);

        T[] arr = sections.get(0).source;
        int start = Integer.MAX_VALUE;
        int end   = 0;
        for (int i = sections.size() - 1; i >= 0; i--) {
            ArraySection<T> section = sections.get(i);

            int ss = section.start;
            int se = section.end;

            if (ss < start) start = ss;
            if (se > end)   end   = se;
        }

        return new ArraySection<>(arr, start, end);
    }

    /**
     * Removes the range of the given section from the
     * source array of the section.
     *
     * @param section The section.
     * @param <T> The element type.
     * @return The result array.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] arrayWithout(ArraySection<T> section) {
        int removeCount = section.length();
        T[] arrSrc = section.source;
        T[] arrDst = (T[]) Array.newInstance(arrSrc.getClass().getComponentType(), arrSrc.length - removeCount);

        if (section.start > 0) {
            System.arraycopy(arrSrc, 0, arrDst, 0, section.start);
        }

        if (section.end < arrSrc.length - 1) {
            System.arraycopy(arrSrc, section.end + 1, arrDst, section.start, arrSrc.length - section.end - 1);
        }

        return arrDst;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] repeat(T[] arrSrc, int count) {
        final int l = arrSrc.length;
        T[] arr = (T[]) Array.newInstance(arrSrc.getClass().getComponentType(), l * count);
        for (int i = 0; i < count; i++) {
            System.arraycopy(arrSrc, 0, arr, i * l, l);
        }

        return arr;
    }

    /**
     * Stringifies the elements enumerated by the iterator
     * following the formatting of an array or collection.
     *
     * @param iterator The iterator.
     * @return The string.
     */
    public static String stringifyIterator(Iterator<?> iterator) {
        StringBuilder b = new StringBuilder("[");
        b.append(iterator.next());
        while (iterator.hasNext()) {
            b.append(", ").append(iterator.next());
        }

        b.append("]");

        return b.toString();
    }

}
