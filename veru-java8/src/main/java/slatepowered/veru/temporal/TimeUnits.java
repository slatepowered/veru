package slatepowered.veru.temporal;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public final class TimeUnits {

    @SuppressWarnings("unchecked")
    public static TemporalUnit ofSeconds(long secs) {
        return new TemporalUnit() {
            final Duration duration = Duration.of(secs, ChronoUnit.SECONDS);

            @Override
            public Duration getDuration() {
                return duration;
            }

            @Override
            public boolean isDurationEstimated() {
                return false;
            }

            @Override
            public boolean isDateBased() {
                return secs > 60 * 60 * 24; // longer than a day
            }

            @Override
            public boolean isTimeBased() {
                return secs < 60 * 60 * 24; // shorter than a day
            }

            @Override
            public <R extends Temporal> R addTo(R temporal, long amount) {
                return (R) temporal.plus(secs, ChronoUnit.SECONDS);
            }

            @Override
            public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
                return temporal1Inclusive.until(temporal2Exclusive, this);
            }
        };
    }

    /**
     * A year defined as exactly 365 days.
     */
    public static final TemporalUnit YEAR = ofSeconds(60 * 60 * 24 * 365);

}
