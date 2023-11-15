package slatepowered.veru.config;

import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.io.Writer;

@RequiredArgsConstructor
public class YamlConfigParser implements ConfigParser {

    private static final Yaml STANDARD_YAML;

    static {
        STANDARD_YAML = new Yaml();
    }

    public static YamlConfigParser of(Yaml yaml) {
        return new YamlConfigParser(yaml);
    }

    public static YamlConfigParser standard() {
        return of(STANDARD_YAML);
    }

    /**
     * The YAML instance used to parse.
     */
    protected final Yaml yaml;

    @Override
    public void load(Reader reader, Section section) throws Exception {
        section.putAll(yaml.load(reader));
    }

    @Override
    public void write(Writer writer, Section section) throws Exception {
        yaml.dump(section.toMap(), writer);
    }

}
