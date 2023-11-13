package test.slatepowered.veru.reflect;

import org.junit.jupiter.api.Test;
import slatepowered.veru.reflect.Classloading;

import java.net.URL;
import java.nio.file.Paths;

public class ClassloadingTest {

    @Test
    public static void main(String[] args) throws Exception {
        final URL jar = Paths.get("./testJar.jar").toUri().toURL();
        final String className = "org.slf4j.Logger"; // The class name to test whether it has loaded
        final ClassLoader loader = ClassLoader.getSystemClassLoader();

        Classloading.addURLs(loader, jar);
        Class.forName(className);
        System.out.println("Successfully loaded JAR " + jar + " into loader " + loader);
        System.out.println("Tested with className: " + className);
    }

}
