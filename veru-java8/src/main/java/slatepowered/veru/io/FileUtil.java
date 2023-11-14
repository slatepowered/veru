package slatepowered.veru.io;

import slatepowered.veru.functional.ThrowingCallable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Utilities for working with files and paths.
 */
public class FileUtil {

    /**
     * Deletes the given directory with all files inside it.
     *
     * @param path The path.
     */
    public static void deleteDirectory(Path path) throws IOException {
        if (!Files.isDirectory(path))
            throw new IllegalArgumentException("Path is not a directory");

        Files.list(path).forEach((ThrowingCallable<Path>) FileUtil::delete);
        Files.delete(path);
    }

    /**
     * Deletes the given path.
     *
     * @param path The path to delete.
     */
    public static void delete(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            deleteDirectory(path);
        } else {
            Files.delete(path);
        }
    }

    /**
     * Deletes the given file path if it exists.
     *
     * @param path The path.
     */
    public static void deleteIfPresent(Path path) throws IOException {
        if (!Files.exists(path))
            return;
        delete(path);
    }

    /**
     * Applies the given consumer to the relative path of
     * all files in the given path to the given root path recursively.
     *
     * @param path The root path.
     * @param relativePathConsumer The relative path consumer.
     */
    public static void walkAndAcceptRelative(Path path, Consumer<Path> relativePathConsumer) throws IOException {
        Files.walk(path).forEach(p -> relativePathConsumer.accept(path.relativize(p)));
    }

    /**
     * Creates the given file and all it's parents if it does not exist.
     *
     * @param path The file path.
     * @return The same file path.
     */
    public static Path createIfAbsent(Path path) throws IOException {
        if (!Files.exists(path)) {
            Path parent = path.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Files.createFile(path);
        }

        return path;
    }

    /**
     * Creates the given directory and all it's parents if it does not exist.
     *
     * @param path The file path.
     * @return The same file path.
     */
    public static Path createDirectoryIfAbsent(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        return path;
    }

    /**
     * Create or overwrite the given file with the given text
     * content using the UTF-8 encoding.
     *
     * @param path The file path.
     * @param text The text content.
     * @return The file path back.
     */
    public static Path overwriteWithString(Path path, String text) throws IOException {
        createIfAbsent(path);
        OutputStream stream = Files.newOutputStream(path);
        stream.write(text.getBytes(StandardCharsets.UTF_8));
        stream.close();
        return path;
    }

    /**
     * Reads the full contents of the given file as a UTF-8 string if it exists.
     *
     * @param path The file path.
     * @return The text content.
     */
    public static String readString(Path path) throws IOException {
        InputStream stream = Files.newInputStream(path);
        byte[] bytes = IOUtil.readAllBytes(stream);
        stream.close();
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
