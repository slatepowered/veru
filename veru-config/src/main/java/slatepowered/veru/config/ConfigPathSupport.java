package slatepowered.veru.config;

import slatepowered.veru.string.StringReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Support for configuration paths.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
final class ConfigPathSupport {

    /**
     * Check whether the given character is a valid
     * path element character.
     */
    static boolean isValidPathChar(char c) {
        return c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                || (c >= '0' && c <= '9');
    }

    /**
     * Parses an array of path elements from the given
     * string reader.
     *
     * Each element should be separated by a dot in the string.
     *
     * @param list The list to parse into.
     * @param reader The string reader to parse from.
     * @return The list back.
     */
    static List<Object> parsePath(List<Object> list, StringReader reader) {
        while (!reader.ended() && isValidPathChar(reader.curr())) {
            // parse element object
            Object element = StringReader.isDigit(reader.curr(), 10) ?
                    reader.collectInt() :
                    reader.collect(ConfigPathSupport::isValidPathChar);
            list.add(element);

            if (reader.curr() == '.') {
                reader.next();
            }
        }

        return list;
    }

    /**
     * Get the next value.
     *
     * @param current The current value.
     * @param key The key.
     * @return The next value.
     */
    static Object pathNext(Object current, Object key) {
        // check for string key
        if (key instanceof String) {
            if (!(current instanceof Section)) {
                throw new IllegalArgumentException("Expected section to index by string key: `" + key + "`");
            }

            current = ((Section) current).getMapped((String) key);
            return current;
        }

        // check for integer index
        if (key instanceof Integer || key instanceof Long) {
            if (!(current instanceof List)) {
                throw new IllegalArgumentException("Expected list to index by integer key: " + key);
            }

            int index = ((Number) key).intValue();
            List list = (List) current;

            return (index < 0 || index >= list.size()) ? null : list.get(index);
        }

        throw new IllegalArgumentException("Unsupported key in path: `" + key + "`");
    }

    /**
     * Get the next value in an optional.
     *
     * @param current The current value.
     * @param key The key.
     * @return The next value.
     */
    static <T> Optional<T> pathNextOptional(Object current, Object key) {
        // check for string key
        if (key instanceof String) {
            if (!(current instanceof Section)) {
                throw new IllegalArgumentException("Expected section to index by string key: `" + key + "`");
            }

            String strKey = (String) key;
            Section section = (Section) current;

            return section.contains(strKey) ? Optional.of(section.get(strKey)) : Optional.empty();
        }

        // check for integer index
        if (key instanceof Integer || key instanceof Long) {
            if (!(current instanceof List)) {
                throw new IllegalArgumentException("Expected list to index by integer key: " + key);
            }

            int index = ((Number) key).intValue();
            List list = (List) current;

            return (index < 0 || index >= list.size()) ? Optional.empty() : (Optional<T>) Optional.of(list.get(index));
        }

        throw new IllegalArgumentException("Unsupported key in path: `" + key + "`");
    }

    /**
     * Get or create the next value.
     *
     * @param current The current value.
     * @param key The key.
     * @return The next value.
     */
    static Object pathNextOrCreate(Object current, Object key) {
        Object last = current;

        // check for string key
        if (key instanceof String) {
            if (!(current instanceof Section)) {
                throw new IllegalArgumentException("Expected section to index by string key: `" + key + "`");
            }

            // get the value
            current = ((Section) current).getMapped((String) key);
            if (current == null) {
                // create new section
                current = Section.memory(new HashMap<>());
                ((Section)last).set((String) key, current);
            }

            return current;
        }

        // check for integer index
        if (key instanceof Integer || key instanceof Long) {
            if (!(current instanceof List)) {
                throw new IllegalArgumentException("Expected list to index by integer key: " + key);
            }

            int index = ((Number) key).intValue();

            // get the element
            current = ((List) current).get(index == -1 ? 0 : index);
            if (current == null) {
                // create new list and register it
                current = new ArrayList<>();
                List list = (List) last;
                if (index == -1) list.add(current);
                else list.set(index, current);
            }

            return current;
        }

        throw new IllegalArgumentException("Unsupported key in path: `" + key + "`");
    }

    /**
     * Set the next value.
     *
     * @param current The current value.
     * @param key The key.
     * @param value The value to set.
     */
    static void pathSet(Object current, Object key, Object value) {
        // check for string key
        if (key instanceof String) {
            if (!(current instanceof Section)) {
                throw new IllegalArgumentException("Expected section to index by string key: `" + key + "`");
            }

            // set the value
            ((Section)current).set((String) key, value);
            return;
        }

        // check for integer index
        if (key instanceof Integer || key instanceof Long) {
            if (!(current instanceof List)) {
                throw new IllegalArgumentException("Expected list to index by integer key: " + key);
            }

            int index = ((Number) key).intValue();

            // set the element
            List list = (List) current;
            if (index == -1) list.add(value);
            else list.set(index, value);

            return;
        }

        throw new IllegalArgumentException("Unsupported key in path: `" + key + "`");
    }

    /**
     * Check whether the given key is present in the current object.
     *
     * @param current The current.
     * @param key The key.
     * @return Whether it is present.
     */
    static boolean pathContains(Object current, Object key) {
        // check for string key
        if (key instanceof String) {
            if (!(current instanceof Section)) {
                throw new IllegalArgumentException("Expected section to index by string key: `" + key + "`");
            }

            // check the value
            return ((Section)current).contains((String) key);
        }

        // check for integer index
        if (key instanceof Integer || key instanceof Long) {
            if (!(current instanceof List)) {
                throw new IllegalArgumentException("Expected list to index by integer key: " + key);
            }

            // check the index
            int index = ((Number) key).intValue();
            List list = (List) current;
            return index == -1 || !(index < 0 || index >= list.size());
        }

        throw new IllegalArgumentException("Unsupported key in path: `" + key + "`");
    }

}
