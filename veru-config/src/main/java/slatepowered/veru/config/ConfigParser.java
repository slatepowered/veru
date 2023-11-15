package slatepowered.veru.config;

import java.io.Reader;
import java.io.Writer;

/**
 * Represents a simple configuration parser/writer/format.
 */
public interface ConfigParser {

    /**
     * Load the content from the given input into the given section.
     *
     * @param reader The input reader.
     * @param section The section to output to.
     */
    void load(Reader reader, Section section) throws Exception;

    /**
     * Save the content of the given section to the given output.
     *
     * @param writer The output writer.
     * @param section The section with the input content.
     */
    void write(Writer writer, Section section) throws Exception;

}
