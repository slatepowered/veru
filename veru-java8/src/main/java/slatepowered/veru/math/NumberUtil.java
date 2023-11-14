package slatepowered.veru.math;

/**
 * Utilities for working with numbers and parsing them.
 */
public class NumberUtil {

    /**
     * Removes any extra points from the given string.
     *
     * @param str The string.
     * @return The sanitized string.
     */
    public static String sanitizeDoubleString(String str) {
        str = str.replace('_', '.');

        int i = str.indexOf('.');
        if (i == -1)
            return str;

        return str.substring(0, i) + "." + str.substring(i + 1).replace(".", "0");
    }

}
