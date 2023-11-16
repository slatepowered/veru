package slatepowered.veru.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import lombok.RequiredArgsConstructor;

import java.io.Reader;
import java.io.Writer;

@RequiredArgsConstructor
public class TomlConfigParser implements ConfigParser {

    private static final TomlWriter STANDARD_TOML_WRITER;

    static {
        STANDARD_TOML_WRITER = new TomlWriter.Builder()
                .indentTablesBy(0)
                .indentValuesBy(0)
                .padArrayDelimitersBy(1)
                .build();
    }

    /**
     * The writer instance.
     */
    private final TomlWriter tomlWriter;

    public static TomlConfigParser standard() {
        return new TomlConfigParser(STANDARD_TOML_WRITER);
    }

    @Override
    public void load(Reader reader, Section section) throws Exception {
        Toml toml = new Toml().read(reader);
        section.putAll(toml.toMap());
    }

    @Override
    public void write(Writer writer, Section section) throws Exception {
        tomlWriter.write(section.toMap(), writer);
    }

}
