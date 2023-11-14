package slatepowered.veru.version;

/**
 * Represents a range of versions.
 */
public interface VersionRange {

    static ContinuousVersionRange continuous(Version min,
                                             Version max) {
        return new ContinuousVersionRange(
                min != null ? min.major : -1,
                min != null ? min.minor : 0,
                max != null ? max.major : -1,
                max != null ? max.minor : -1);
    }

    static ContinuousVersionRange atLeast(Version min) {
        return continuous(min, null);
    }

    static ContinuousVersionRange atMost(Version max) {
        return continuous(null, max);
    }

    /**
     * Check whether this range includes the given version.
     *
     * @param version The version.
     * @return If it is included.
     */
    boolean includes(Version version);

}
