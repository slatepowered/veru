package test.slatepowered.veru.config;

import org.junit.jupiter.api.Test;
import slatepowered.veru.config.Configuration;
import slatepowered.veru.config.Section;
import slatepowered.veru.config.YamlConfigParser;

import java.nio.file.Paths;

public class YamlConfigTest {

    @Test
    void testYamlConfig() throws Throwable {
        Configuration configuration = new Configuration().withParser(YamlConfigParser.standard());
        configuration.loadFrom(getClass().getResourceAsStream("/test.yml"));

        System.out.println(configuration);
        System.out.println((Object) configuration.get("a", "b", "c"));
        configuration.set(Section.path("p.q.r"), 420);
        System.out.println(configuration);
    }

}
