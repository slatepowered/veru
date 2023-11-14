package slatepowered.veru.runtime;

import lombok.Getter;
import slatepowered.veru.math.NumberUtil;
import slatepowered.veru.version.Version;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Java version.
 */
@Getter
public class JavaVersion extends Version {

    /**
     * Maps all major version numbers to a Java version object.
     */
    private static final Map<Integer, JavaVersion> majorVersionMap = new HashMap<>();

    public static final JavaVersion UNKNOWN = new JavaVersion(VersionType.UNKNOWN, "Unknown", -1, -1, -1);

    /* All currently supported Java versions up until Java 18 */
    public static final JavaVersion JAVA_8 = new JavaVersion(VersionType.MAJOR, "Java 8", 52, 8, 0D).registerMajor();
    public static final JavaVersion JAVA_9 = new JavaVersion(VersionType.MAJOR, "Java 9", 53, 9, 0D).registerMajor();
    public static final JavaVersion JAVA_10 = new JavaVersion(VersionType.MAJOR, "Java 10", 54, 10, 0D).registerMajor();
    public static final JavaVersion JAVA_11 = new JavaVersion(VersionType.MAJOR, "Java 11", 55, 11, 0D).registerMajor();
    public static final JavaVersion JAVA_12 = new JavaVersion(VersionType.MAJOR, "Java 12", 56, 12, 0D).registerMajor();
    public static final JavaVersion JAVA_13 = new JavaVersion(VersionType.MAJOR, "Java 13", 57, 13, 0D).registerMajor();
    public static final JavaVersion JAVA_14 = new JavaVersion(VersionType.MAJOR, "Java 14", 58, 14, 0D).registerMajor();
    public static final JavaVersion JAVA_15 = new JavaVersion(VersionType.MAJOR, "Java 15", 59, 15, 0D).registerMajor();
    public static final JavaVersion JAVA_16 = new JavaVersion(VersionType.MAJOR, "Java 16", 60, 16, 0D).registerMajor();
    public static final JavaVersion JAVA_17 = new JavaVersion(VersionType.MAJOR, "Java 17", 61, 17, 0D).registerMajor();
    public static final JavaVersion JAVA_18 = new JavaVersion(VersionType.MAJOR, "Java 18", 62, 18, 0D).registerMajor();
    public static final JavaVersion JAVA_19 = new JavaVersion(VersionType.MAJOR, "Java 19", 63, 19, 0D).registerMajor();
    public static final JavaVersion JAVA_20 = new JavaVersion(VersionType.MAJOR, "Java 20", 64, 20, 0D).registerMajor();
    public static final JavaVersion JAVA_21 = new JavaVersion(VersionType.MAJOR, "Java 21", 65, 21, 0D).registerMajor();
    public static final JavaVersion JAVA_22 = new JavaVersion(VersionType.MAJOR, "Java 22", 66, 22, 0D).registerMajor();

    public static final JavaVersion CURRENT = getRuntimeVersion();

    static {
        if (CURRENT.isOlderThan(JAVA_8)) {
            System.err.println("WARNING: You are using a Java version older than Java (1.)8");
            System.err.println("WARNING: This is unsupported by this project, please upgrade to *at least* Java 8");
            System.err.println("WARNING: Current version: " + CURRENT.getName() + ": " + CURRENT);
        }
    }

    /**
     * Get or create a major version for the given version number.
     *
     * @param major The major version number.
     * @return The version.
     */
    public static JavaVersion getMajor(int major) {
        return majorVersionMap.computeIfAbsent(major, __ -> {
            if (major <= 7) {
                return new JavaVersion(VersionType.UNSUPPORTED, "Java " + major, 44 + major, major, 0D);
            } else {
                return new JavaVersion(VersionType.MAJOR, "Java " + major, 44 + major, major, 0D);
            }
        });
    }

    /**
     * Make a new version with the given name for the given
     * major and minor version numbers.
     *
     * @param name The name.
     * @param major The major version.
     * @param minor The minor version.
     * @return The version object.
     */
    public static JavaVersion make(String name, int major, double minor) {
        if (minor == 0D) {
            return getMajor(major);
        }

        JavaVersion majorVersion = getMajor(major);
        return new JavaVersion(VersionType.MINOR, name, majorVersion.classFileVersion, major, minor);
    }

    /**
     * Try and get the Java version from the given string.
     *
     * @param str The string to parse.
     * @return The Java version object or {@link #UNKNOWN} if the string is in correct format but the version could not be resolved.
     * @throws IllegalArgumentException If the string is in an unsupported format.
     */
    public static JavaVersion fromString(String str) throws IllegalArgumentException {
        int i;

        if ((i = str.indexOf('.')) != -1) {
            String majorStr = str.substring(0, i);
            String minorStr = str.substring(i + 1);
            int major = Integer.parseInt(majorStr);

            // check for 1.x -> x
            if (major == 1) {
                return fromString(minorStr);
            }

            return make("Java " + str, Integer.parseInt(majorStr), Double.parseDouble(NumberUtil.sanitizeDoubleString(minorStr)));
        }

        if ((i = str.indexOf('-')) != -1) {
            return fromString(str.substring(0, i));
        }

        try {
            int major = Integer.parseInt(str);
            return getMajor(major);
        } catch (NumberFormatException ignored) {

        }

        throw new IllegalArgumentException("Unknown Java version string format");
    }

    // calculate the current runtime version
    private static JavaVersion getRuntimeVersion() {
        String str = System.getProperty("java.version");
        return fromString(str);
    }

    public enum VersionType {
        /**
         * Couldn't resolve the Java version from some source.
         */
        UNKNOWN,

        /**
         * Anything below Java 1.7
         */
        UNSUPPORTED,

        /**
         * A major Java version
         */
        MAJOR,

        /**
         * A minor Java version
         */
        MINOR
    }

    public JavaVersion(VersionType type, String name, int classFileVersion,
                       int major, double minor) {
        super(major, minor);
        this.type = type;
        this.name = name;
        this.classFileVersion = classFileVersion;
    }

    public JavaVersion() {
        super();
    }

    /**
     * The type of this version.
     */
    private VersionType type;

    /**
     * The display name of the version.
     */
    private String name;

    /**
     * The class file version.
     */
    private int classFileVersion;

    // register this java version as a major version
    // to the major version map
    private JavaVersion registerMajor() {
        if (type == VersionType.MAJOR) {
            majorVersionMap.put(major, this);
        }

        return this;
    }

    /**
     * Get the major version for this version object.
     *
     * @return The major version.
     */
    public JavaVersion toMajor() {
        return getMajor(this.major);
    }

    public boolean isSupported() {
        return type != VersionType.UNSUPPORTED;
    }

    @Override
    public String toString() {
        return type + " Java version: " + major + " (" + (minor != 0D ? minor + " " : "") + "classFile: " + classFileVersion + ")";
    }

}
