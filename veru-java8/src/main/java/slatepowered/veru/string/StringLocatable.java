package slatepowered.veru.string;

/**
 * An object which can have a location in a string.
 */
public interface StringLocatable {

    StringLocation getLocation();

    StringLocatable setLocation(StringLocation location);

}