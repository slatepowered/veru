package slatepowered.veru.io;

import slatepowered.veru.misc.Throwables;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

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

    /**
     * 'Safely' parses a URL, basically just constructs
     * the URL silently rethrowing any errors.
     *
     * @param url The URL string.
     * @return The URL object.
     */
    public static URL parseURL(String url) {
        try {
            return new URL(url);
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
            throw new AssertionError();
        }
    }

    /**
     * Download the given URL to the given file path.
     *
     * @param url The URL.
     * @param output The output file.
     */
    public static void download(URL url, Path output) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
            FileUtil.createDirectoryIfAbsent(output.getParent());
            BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(output));
            outputStream.close();
            inputStream.close();
        } catch (Throwable t) {
            throw new RuntimeException("Error while downloading file " + url, t);
        }
    }

}
