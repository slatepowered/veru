package slatepowered.veru.config;

import slatepowered.veru.string.StringReader;

import java.util.*;
import java.util.function.Supplier;

import static slatepowered.veru.config.ConfigPathSupport.*;

/**
 * A section of a configuration.
 */
@SuppressWarnings("rawtypes")
public interface Section {

    /**
     * Parses an array of path elements from the given
     * string.
     *
     * Each element should be separated by a dot in the string.
     *
     * @param str The string to parse.
     * @return The object array representing the path.
     */
    static Object[] path(String str) {
        return path(new StringReader(str));
    }

    /**
     * Parses an array of path elements from the given
     * string reader.
     *
     * Each element should be separated by a dot in the string.
     *
     * @param reader The string reader to parse from.
     * @return The object array representing the path.
     */
    static Object[] path(StringReader reader) {
        return ConfigPathSupport.parsePath(new ArrayList<>(), reader).toArray();
    }

    @SuppressWarnings("unchecked")
    static Section memory(final Map<String, Object> map) {
        return new Section() {
            @Override
            public Map<String, Object> toMap() {
                return map;
            }

            @Override
            public Section putAll(Map<String, Object> otherMap) {
                map.putAll(otherMap);
                return this;
            }

            @Override
            public Set<String> getKeys() {
                return map.keySet();
            }

            @Override
            public Collection<Object> getValues() {
                return map.values();
            }

            @Override
            public <T> T get(String key) {
                return (T) map.get(key);
            }

            @Override
            public <T> T getOrDefault(String key, T def) {
                return (T) map.getOrDefault(key, def);
            }

            @Override
            public <T> T getOrSupply(String key, Supplier<T> def) {
                if (!map.containsKey(key))
                    return def.get();
                return (T) map.get(key);
            }

            @Override
            public Section set(String key, Object value) {
                map.put(key, value);
                return this;
            }

            @Override
            public boolean contains(String key) {
                return map.containsKey(key);
            }

            @Override
            public Section section(String key) {
                Object v = get(key);
                if (v == null) {
                    Map<String, Object> map = new HashMap<>();
                    set(key, map);
                    return Section.memory(map);
                } else if (v instanceof Map) {
                    return Section.memory((Map) v);
                } else if (v instanceof Section) {
                    return (Section) v;
                }

                throw new IllegalStateException("Value by key '" + key + "' is not a section or map");
            }

            @Override
            public String toString() {
                return map.toString();
            }

            @Override
            public int hashCode() {
                return map.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj == this) return true;
                if (!(obj instanceof Section)) return false;
                return map.equals(((Section)obj).toMap());
            }
        };
    }

    /////////////////////////////////////

    /**
     * Convert this section to a map.
     *
     * @return The map.
     */
    Map<String, Object> toMap();

    /**
     * Put all entries from the given map into this section.
     *
     * @param map The map to put.
     * @return This.
     */
    default Section putAll(Map<String, Object> map) {
        map.forEach(this::set);
        return this;
    }

    /**
     * Get the keys of this section.
     *
     * @return The keys.
     */
    Set<String> getKeys();

    /**
     * Get the values of this section.
     *
     * @return The values.
     */
    Collection<Object> getValues();

    /**
     * Get one value by key.
     *
     * @param key The key.
     * @param <T> The value type.
     * @return The value.
     */
    <T> T get(String key);

    /**
     * Get the given path in this section.
     *
     * Each string in the path is interpreted as a key into
     * a map/section and each integer number is interpreted as
     * an index into an array.
     *
     * @param path The path.
     * @param <T> The value type.
     * @return The value or null if absent.
     */
    @SuppressWarnings("unchecked")
    default <T> T get(Object... path) {
        Object current = this;
        for (Object key : path) {
            current = pathNext(current, key);
        }

        return (T) current;
    }

    /**
     * Try to get the given key in this section.
     *
     * @param key The key.
     * @param <T> The value type.
     * @return An optional containing the value or empty if absent.
     */
    default <T> Optional<T> issue(String key) {
        return contains(key) ? Optional.of(get(key)) : Optional.empty();
    }

    /**
     * Try to get the given path in this section.
     *
     * Each string in the path is interpreted as a key into
     * a map/section and each integer number is interpreted as
     * an index into an array.
     *
     * @param path The path.
     * @param <T> The value type.
     * @return An optional containing the value or empty if absent.
     */
    @SuppressWarnings("unchecked")
    default <T> Optional<T> issue(Object... path) {
        Object current = this;
        final int length = path.length;
        for (int i = 0; i < length - 1; i++) {
            current = pathNext(current, path[i]);
        }

        return pathNextOptional(current, path[path.length - 1]);
    }

    /**
     * Get one value by key or the
     * default value if absent.
     *
     * @param key The key.
     * @param def The default value.
     * @param <T> The value type.
     * @return The value.
     */
    <T> T getOrDefault(String key, T def);

    /**
     * Get one value by key or the
     * default value from the supplier if absent.
     *
     * @param key The key.
     * @param def The default value supplier.
     * @param <T> The value type.
     * @return The value.
     */
    <T> T getOrSupply(String key, Supplier<T> def);

    /**
     * Set the value at the given key to this.
     *
     * @param key The key.
     * @param value The value.
     */
    Section set(String key, Object value);

    /**
     * Set the value at the given path to this.
     *
     * Each string in the path is interpreted as a key into
     * a map/section and each integer number is interpreted as
     * an index into an array.
     *
     * @param path The path.
     * @param value The value.
     */
    default Section set(Object[] path, Object value) {
        Object current = this;
        final int length = path.length;
        for (int i = 0; i < length - 1; i++) {
            current = pathNextOrCreate(current, path[i]);
        }

        pathSet(current, path[path.length - 1], value);
        return this;
    }

    /**
     * Check if this section contains
     * the provided key.
     *
     * @param key The key.
     * @return If it contains.
     */
    boolean contains(String key);


    /**
     * Check if this section contains
     * the provided path.
     *
     * Each string in the path is interpreted as a key into
     * a map/section and each integer number is interpreted as
     * an index into an array.
     *
     * @param path The path.
     * @return If it contains.
     */
    default boolean contains(Object... path) {
        Object current = this;
        final int length = path.length;
        for (int i = 0; i < length - 1; i++) {
            current = pathNext(current, path[i]);
            if (current == null) {
                return false;
            }
        }

        return pathContains(current, path[path.length - 1]);
    }

    /**
     * Get or create a section by key.
     *
     * @param key The key.
     * @return The section.
     */
    Section section(String key);

    /**
     * Get or create a section by the given path.
     *
     * Each string in the path is interpreted as a key into
     * a map/section and each integer number is interpreted as
     * an index into an array.
     *
     * @param path The path.
     * @return The section.
     */
    default Section section(Object... path) {
        Object current = this;
        Object last = null;
        for (Object o : path) {
            last = current;
            current = pathNextOrCreate(current, o);
        }

        if (!(current instanceof Section)) {
            current = Section.memory(new HashMap<>());
            pathSet(last, path[path.length - 1], current);
        }

        return (Section) current;
    }

    /**
     * Get the value by the given key then convert it to an
     * appropriate value.
     *
     * @param key The key.
     * @return The result value.
     */
    @SuppressWarnings("unchecked")
    default Object getMapped(String key) {
        Object v = get(key);
        if (v instanceof Map) {
            return Section.memory((Map) v);
        } else if (v instanceof Section) {
            return v;
        }

        return v;
    }

}
