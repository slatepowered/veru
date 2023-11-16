package test.slatepowered.veru.io;

import org.junit.jupiter.api.Test;
import slatepowered.veru.io.IOUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadTest {

    @Test
    void testDownload() {
        IOUtil.download(IOUtil.parseURL("https://api.papermc.io/v2/projects/velocity/versions/3.2.0-SNAPSHOT/builds/294/downloads/velocity-3.2.0-SNAPSHOT-294.jar"), Paths.get("./test.jar"));
    }

}
