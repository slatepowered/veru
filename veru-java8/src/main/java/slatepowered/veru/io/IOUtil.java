package slatepowered.veru.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

    /**
     * Transfers the given input stream to the output stream
     * using buffering.
     *
     * @param inputStream The input stream.
     * @param outputStream The output stream.
     * @param bufferSize The buffer size.
     * @throws IOException Any IO exception.
     */
    public static void transfer(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len = inputStream.read(buffer);
        while (len != -1) {
            outputStream.write(buffer, 0, len);
            len = inputStream.read(buffer);
        }
    }

    /**
     * Transfers the given input stream to the output stream
     * using buffering, using {@link #transfer(InputStream, OutputStream, int)}
     * internally.
     *
     * It uses a buffer size of 1024.
     *
     * @param inputStream The input stream.
     * @param outputStream The output stream.
     * @throws IOException Any IO exception.
     */
    public static void transfer(InputStream inputStream, OutputStream outputStream) throws IOException {
        transfer(inputStream, outputStream, 1024);
    }

}
