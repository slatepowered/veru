package slatepowered.veru.version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class ContinuousVersionRange implements VersionRange {

    private int minMajor;    // The minimum major number (-1 to disable)
    private double minMinor; // The minimum minor number (0 to basically ignore)

    private int maxMajor;    // The maximum major number (-1 to disable)
    private double maxMinor; // The maximum minor number (-1 to ignore)

    @Override
    public boolean includes(Version version) {
        // check minimum bounds
        if (minMajor != -1) {
            if (version.major < minMajor) return false;
            if (version.major == minMajor && version.minor < minMinor) return false;
        }

        // check maximum bounds
        if (maxMajor != -1) {
            if (version.major > maxMajor) return false;
            if (maxMinor != -1 && version.major == maxMajor && version.minor > maxMinor) return false;
        }

        return true;
    }

}
