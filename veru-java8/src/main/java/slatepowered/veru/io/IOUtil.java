package slatepowered.veru.io;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {

    /**
     * Read all available bytes from the given input stream.
     *
     * @param stream The stream.
     * @return The full byte array.
     */
    public static byte[] readAllBytes(InputStream stream) throws IOException {
        byte[] arr = new byte[stream.available()];
        stream.read(arr);
        return arr;
    }

}
