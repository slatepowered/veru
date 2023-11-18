package slatepowered.veru.temporal;

import slatepowered.veru.data.Pair;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DurationParser {

    private final List<Pair<ChronoUnit, String>> UNITS = new ArrayList<>();

    {
        UNITS.add(Pair.of(ChronoUnit.YEARS, "y"));
        UNITS.add(Pair.of(ChronoUnit.DAYS, "d"));
        UNITS.add(Pair.of(ChronoUnit.HOURS, "h"));
        UNITS.add(Pair.of(ChronoUnit.MINUTES, "m"));
        UNITS.add(Pair.of(ChronoUnit.SECONDS, "s"));
    }

    /**
     * Stringify the given duration into a short form.
     *
     * @param duration The duration.
     * @return The short form string.
     */
    public String stringifyShort(Duration duration) {
        StringBuilder b = new StringBuilder();
        for (Pair<ChronoUnit, String> unitPair : UNITS) {
            ChronoUnit unit = unitPair.getFirst();
            String unitChar = unitPair.getSecond();

            long amt = duration.get(unit);
            if (amt > 0) {
                duration = duration.minus(amt, unit);
                b.append(amt).append(unitChar).append(" ");
            }
        }

        b.delete(0, b.length() - 2);
        return b.toString();
    }

}
