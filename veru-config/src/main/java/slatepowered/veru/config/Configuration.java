package slatepowered.veru.config;

import slatepowered.veru.io.IOUtil;
import slatepowered.veru.misc.Throwables;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * File-based configuration wrapped for ease of use.
 *
 * @author orbyfied
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Configuration implements Section {

    @FunctionalInterface
    public interface ResourceFinder {
        InputStream findResource(String name) throws Exception;
    }

    /**
     * The internal map.
     */
    Map<String, Object> map = new HashMap<>();

    /**
     * The configuration parser.
     */
    ConfigParser parser;

    /**
     * The resource finder.
     */
    ResourceFinder resourceFinder = Configuration.class::getResourceAsStream;

    /* Basic Getters. */

    public Configuration withParser(ConfigParser parser) {
        this.parser = parser;
        return this;
    }

    public Configuration withResourceFinder(ResourceFinder finder) {
        this.resourceFinder = finder;
        return this;
    }

    public ConfigParser getParser() {
        return parser;
    }

    public Map<String, Object> map() {
        return map;
    }

    /**
     * (Re)loads the configuration from
     * the file. If the file does not exist
     * it saves the defaults.
     * @return This.
     */
    public Configuration reloadOrDefault(Path file, String defaults) {
        // check if file exists
        if (!Files.exists(file))
            trySaveDefault(file, defaults, false);
        // load configuration
        tryReload(file);
        // return
        return this;
    }

    /**
     * (Re)loads the configuration from
     * the file. If the file does not exist
     * it saves the defaults.
     * @return This.
     */
    public Configuration reloadOrDefaultThrowing(Path file, String defaults) {
        // check if file exists
        if (!Files.exists(file))
            trySaveDefault(file, defaults, false);
        // load configuration
        reload(file);
        // return
        return this;
    }

    /**
     * (Re)loads the configuration from
     * the file.
     * @return This.
     */
    public Configuration reload(Path file) {
        // check if file exists
        if (!Files.exists(file))
            throw new IllegalArgumentException("Supposed configuration file " + file + " doesnt exist");

        try {
            // load configuration
            FileReader reader = new FileReader(file.toFile());
            parser.load(reader, this);
            reader.close();
        } catch (Exception e) {
            Throwables.sneakyThrow(e);
        }

        // return
        return this;
    }

    /**
     * (Re)loads the configuration from
     * the file or creates a new empty
     * file if it doesn't exist.
     * @return This.
     */
    public Configuration reloadOrCreate(Path file) {
        // check if file exists
        if (!Files.exists(file)) {
            try {
                if (!Files.exists(file.getParent()))
                    Files.createDirectories(file.getParent());
                Files.createFile(file);
            } catch (IOException e) {
                Throwables.sneakyThrow(e);
            }
        }

        try {
            // load configuration
            FileReader reader = new FileReader(file.toFile());
            parser.load(reader, this);
            reader.close();
        } catch (Exception e) {
            Throwables.sneakyThrow(e);
        }

        // return
        return this;
    }

    /**
     * (Re)loads the configuration from
     * the file.
     * @return This.
     */
    public Configuration tryReload(Path file) {
        // check if file exists
        if (!Files.exists(file))
            return this;

        try {
            // load configuration
            FileReader reader = new FileReader(file.toFile());
            parser.load(reader, this);
            reader.close();
        } catch (Exception e) {
            return this;
        }

        // return
        return this;
    }

    /**
     * Loads the configuration from the
     * given input stream.
     *
     * @param inputStream The input stream.
     * @return This.
     */
    public Configuration loadFrom(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "Input stream can not be null");

        try {
            // load configuration
            InputStreamReader reader = new InputStreamReader(inputStream);
            parser.load(reader, this);
            reader.close();
        } catch (Exception e) {
            Throwables.sneakyThrow(e);
        }

        return this;
    }

    /**
     * Saves the configuration to
     * the file.
     * @return This.
     * @throws IllegalArgumentException if the file is invalid, making
     *                                  it unable to be created
     * @throws IllegalStateException if the saving fails with an IOException
     */
    public Configuration save(Path file) {
        // check if the file doesnt exist
        file = file.toAbsolutePath();
        if (!Files.exists(file)) {
            try {
                // attempt to create it
                if (!Files.exists(file.getParent()))
                    Files.createDirectories(file.getParent());
                Files.createFile(file);
            } catch (Exception e) {
                throw new IllegalArgumentException("failed to create non-existent file " + file, e);
            }
        }

        try {
            // try to save
            FileWriter writer = new FileWriter(file.toFile());
            parser.write(writer, this);
            writer.close();
        } catch (Exception e) {
            throw new IllegalStateException("failed to save configuration to file " + file, e);
        }

        // return
        return this;
    }

    /**
     * Tries to save the default configuration,
     * given by a resource which should be in
     * your JAR file, named after the file.
     * @param defaults The resource path for the defaults.
     * @param override If an existent file should be
     *                 overwritten.
     * @return This.
     */
    public Configuration trySaveDefault(Path file, String defaults, boolean override) {
        // check override
        if (!override && Files.exists(file))
            return this;

        try {
            // open resource
            // todo: allow customization of how the resource is opened
            if (!defaults.startsWith("/"))
                defaults = "/" + defaults;
            InputStream is = resourceFinder.findResource(defaults);
            if (is == null)
                throw new IllegalArgumentException("Could not open resource '" + defaults + "'");

            // create file if non-existent
            file = file.toAbsolutePath();
            if (!Files.exists(file)) {
                if (!Files.exists(file.getParent()))
                    Files.createDirectories(file.getParent());
                Files.createFile(file);
            }

            // open file, write and close
            OutputStream out = Files.newOutputStream(file);
            IOUtil.transfer(is, out);
            is.close();
            out.close();
        } catch (Exception e) {
            throw new IllegalStateException("failed to save configuration to file " + file, e);
        }

        // return
        return this;
    }

    //////////////////////////////////////
    ////////// WRAPPED METHODS ///////////
    //////////////////////////////////////


    @Override
    public Section putAll(Map<String, Object> map) {
        this.map.putAll(map);
        return this;
    }

    @Override
    public Map<String, Object> toMap() {
        return map;
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
    public Configuration set(String key, Object value) {
        map.put(key, value);
        return this;
    }

    @Override
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    @Override
    public Section section(String key) {
        Object v = map.get(key);
        if (v == null) {
            Map<String, Object> map = new HashMap<>();
            this.map.put(key, map);
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

}