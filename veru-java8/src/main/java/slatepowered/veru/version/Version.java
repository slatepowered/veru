package slatepowered.veru.version;

import lombok.Getter;

import java.util.Objects;

/**
 * Represents a comparable version type.
 */
@Getter
public class Version {

    /**
     * The major version number.
     */
    protected int major;

    /**
     * The minor version float, this is exclusively intended to be a way
     * of storing arbitrary minor version information and is not meant to be
     * a user-friendly way of representing it.
     */
    protected double minor;

    public Version() {

    }

    public Version(int major) {
        this(major, 0D);
    }

    public Version(int major, double minor) {
        this.major = major;
        this.minor = minor;
    }

    /**
     * Check whether this version is greater than the given version.
     *
     * @param version The version to check against.
     * @return Boolean.
     */
    public boolean isNewerThan(Version version) {
        if (this.major > version.major) return true;
        return this.major == version.major && this.minor > version.minor;
    }

    /**
     * Check whether this version is greater than or is equal to the given version.
     *
     * @param version The version to check against.
     * @return Boolean.
     */
    public boolean isNewerThanOrEquals(Version version) {
        if (this.major > version.major) return true;
        return this.major == version.major && this.minor >= version.minor;
    }

    /**
     * Check whether this version is older than the given version.
     *
     * @param version The version to check against.
     * @return Boolean.
     */
    public boolean isOlderThan(Version version) {
        return !isNewerThanOrEquals(version);
    }

    /**
     * Check whether this version is older than or equal to the given version.
     *
     * @param version The version to check against.
     * @return Boolean.
     */
    public boolean isOlderThanOrEquals(Version version) {
        return !isNewerThan(version);
    }

    /**
     * Check whether this version only has a major component
     * (minor is 0).
     *
     * @return If this version doesn't have a minor component.
     */
    public boolean isMajor() {
        return minor == 0D;
    }

    /**
     * Check whether this version has a minor component.
     *
     * @return Whether this version has a minor component.
     */
    public boolean isMinor() {
        return minor != 0D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version that = (Version) o;
        return major == that.major && Double.compare(that.minor, minor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor);
    }

    @Override
    public String toString() {
        return "Version " + major + (isMinor() ? "." + minor : "");
    }

}
