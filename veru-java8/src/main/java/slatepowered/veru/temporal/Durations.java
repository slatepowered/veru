package slatepowered.veru.temporal;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * Duration utilities.
 */
public final class Durations {

    /**
     * Create a duration with a fractional amount of the given time unit
     *
     * @param amount The amount.
     * @param unit The unit.
     * @return The duration.
     */
    public static Duration fractional(double amount, TemporalUnit unit) {
        if (amount % 1 == 0) {
            return Duration.of((long) amount, unit);
        }

        long fullAmount = (long) amount;
        double adjustment = amount - fullAmount;

        Duration unitDuration = unit.getDuration();
        long secs = unitDuration.getSeconds();
        int nanos = unitDuration.getNano();

        long totalUnitNanos = unitDuration.getSeconds() * 1_000_000_000 + unitDuration.getNano();
        long newUnitNanos = (long)(totalUnitNanos * adjustment);

        secs += newUnitNanos / 1_000_000_000;
        nanos += newUnitNanos % 1_000_000_000;
        return Duration.ofSeconds(secs, nanos);
    }

    public static long toNanos(Duration duration) {
        return duration.getSeconds() * 1_000_000_000 + duration.getNano();
    }

}
