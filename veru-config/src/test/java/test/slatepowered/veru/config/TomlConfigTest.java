package test.slatepowered.veru.config;

import org.junit.jupiter.api.Test;
import slatepowered.veru.config.Configuration;
import slatepowered.veru.config.TomlConfigParser;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TomlConfigTest {

    @Test
    void testB4asicTomlConfig() {
        Configuration configuration = new Configuration().withParser(TomlConfigParser.standard());
        configuration.loadFrom(getClass().getResourceAsStream("/test.toml"));

        System.out.println(configuration);
        System.out.println(configuration.section("a").<Object>get("b"));

        configuration.section("e").set("f", "h");
        configuration.save(Paths.get("./test.toml"));
    }

}
