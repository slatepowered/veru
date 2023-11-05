package com.slatepowered.veru.data;


import java.util.*;

/**
 * General purpose, nested map-like data structure
 * designed to
 */
public class Values {

    private static String toStringPretty(Object val) {
        if (val instanceof String)
            return "\"" + val + "\"";
        return Objects.toString(val);
    }

    /**
     * Puts the key-value pairs given in the varargs
     * array into the given values object.
     *
     * The array is expected to have each even index contain
     * a key and the next (odd) index to contain the value.
     * Example:  0   1   2   3   4   5
     *        [ "a", 6, "b", 8, "c", 9 ]
     *      = { "a" = 6, "b" = 8, "c" = 9 }
     *
     * @param values The values.
     * @param args The varargs key-value array.
     * @return The given values instance back.
     */
    public static Values ofVarargs(Values values, Object... args) {
        String key = null;
        int l = args.length;
        for (int i = 0; i < l; i++) {
            Object obj = args[i];
            if (i % 2 == 0) {
                key = (String) obj;
            } else {
                values.put(key, obj);
                key = null;
            }
        }

        return values;
    }

    /**
     * @see Values#ofVarargs(Values, Object...)
     */
    public static Values ofVarargs(Object... args) {
        return ofVarargs(new Values(), args);
    }

    //////////////////////////////////////

    public Values() {
        map = new HashMap<>();
    }

    public Values(int size) {
        map = new HashMap<>(size);
    }

    public Values(HashMap<Object, Object> map) {
        this.map = map;
    }

    public Values(Map<String, Object> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }

    // the internal map
    HashMap<Object, Object> map;

    public int getSize() {
        return map.size();
    }

    public HashMap<Object, Object> getMap() {
        return map;
    }

    public Values putAll(Values values) {
        map.putAll(values.getMap());
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Values putAll(Map map) {
        this.map.putAll(map);
        return this;
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    public List<Map.Entry<Object, Object>> entries() {
        return new ArrayList<>(map.entrySet());
    }

    public Set<Object> keySet() {
        return map.keySet();
    }

    public List<Object> keys() {
        return new ArrayList<>(map.keySet());
    }

    public Collection<Object> valueCollection() {
        return map.values();
    }

    public List<Object> values() {
        return new ArrayList<>(map.values());
    }

    public Values setFlat(Object key, Object val) {
        if (val == this)
            throw new IllegalArgumentException("cannot put this recursively, attempted under key '" + key + "'");
        this.map.put(key, val);
        return this;
    }

    public Values set(String key, Object val) {
        // check value isnt this
        if (val == this)
            throw new IllegalArgumentException("cannot put this recursively, attempted under key '" + key + "'");

        // split and check key
        String[] path = key.split("\\.");
        if (path.length == 0)
            throw new IllegalArgumentException("invalid key: '" + key + "'");
        // traverse key
        Object f = traversePath(path);
        // assign
        assignObject(f, last(path), val);

        // return
        return this;
    }

    public Values put(Object key, Object val) {
        return setFlat(key, val);
    }

    @SuppressWarnings("unchecked")
    public <V> V get(String key) {
        // split and check key
        String[] path = key.split("\\.");
        if (path.length == 0)
            throw new IllegalArgumentException("invalid key: '" + key + "'");
        // traverse key
        Object f = traversePath(path);
        // index object
        return (V) indexObject(f, last(path));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <V> V get(String key, Class<V> vClass) {
        // split and check key
        String[] path = key.split("\\.");
        if (path.length == 0)
            throw new IllegalArgumentException("invalid key: '" + key + "'");
        String pl = last(path);
        // traverse key
        Object f = traversePath(path);
        // index object
        V v = (V) indexObject(f, pl);
        if (v instanceof Map && vClass == Values.class) {
            // wrap map to values object and replace
            v = (V) new Values((Map)v);
            assignObject(f, pl, v);
        }

        // return
        return v;
    }

    @SuppressWarnings("unchecked")
    public <V> V getFlat(Object key) {
        return (V) map.get(key);
    }

    public <V> V getFlat(Object key, Class<V> vClass) {
        return getFlat(key);
    }

    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object val) {
        return map.containsValue(val);
    }

    @SuppressWarnings("unchecked")
    public <V> V getOrDefaultFlat(Object key, V def) {
        if (!map.containsKey(key))
            return def;
        return (V) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public <V> V getOrDefault(String key, V def) {
        // split and check key
        String[] path = key.split("\\.");
        if (path.length == 0)
            throw new IllegalArgumentException("invalid key: '" + key + "'");
        // traverse key
        Object f = traversePath(path);
        // check if key is present
        // otherwise return default
        if (!hasObject(f, last(path)))
            return def;
        // index object
        return (V) indexObject(f, last(path));
    }

    public <V> V getOrDefault(String key, Class<V> vClass, V def) {
        return getOrDefault(key, def);
    }

    @SuppressWarnings("rawtypes")
    boolean hasObject(Object obj, String key) {
        if (obj instanceof Values) {
            return ((Values)obj).contains(key);
        } else if (obj instanceof Map) {
            return ((Map)obj).containsKey(key);
        }

        throw new IllegalArgumentException("object of type " + (obj == null ? "null" : obj.getClass().getName()) + " is not indexable by string");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void assignObject(Object obj, String key, Object val) {
        if (obj instanceof Values) {
            ((Values)obj).setFlat(key, val);
            return;
        } else if (obj instanceof Map) {
            ((Map)obj).put(key, val);
            return;
        }

        throw new IllegalArgumentException("object of type " + (obj == null ? "null" : obj.getClass().getName()) + " is not indexable by string");
    }

    @SuppressWarnings("rawtypes")
    Object indexObject(Object obj, String key) {
        if (obj instanceof Values) {
            return ((Values)obj).getFlat(key);
        } else if (obj instanceof Map) {
            return ((Map)obj).get(key);
        }

        throw new IllegalArgumentException("object of type " + (obj == null ? "null" : obj.getClass().getName()) + " is not indexable by string");
    }

    Object traversePath(String[] path) {
        // start at this
        Object curr = this;
        // iterative traversal
        int l = path.length -
                /* one to last, as last object must be indexed manually */ 1;
        for (int i = 0; i < l; i++)
            curr = indexObject(curr, path[i]);
        // return one to last object
        return curr;
    }

    // returns the last item of an array
    private <V> V last(V[] vs) {
        if (vs.length == 0)
            throw new IllegalArgumentException("invalid array of size 0");
        return vs[vs.length - 1];
    }

    /////////////////////////////

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Values values = (Values) o;
        return Objects.equals(map, values.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("{ ");
        int i = 0;
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            // handle trailing comma
            if (i != 0)
                b.append(", ");
            b.append(toStringPretty(entry.getKey()));
            b.append(" : ");
            b.append(toStringPretty(entry.getValue()));

            i++;
        }

        return b.append(" }").toString();
    }

}
