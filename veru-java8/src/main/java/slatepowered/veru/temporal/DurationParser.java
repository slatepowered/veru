package slatepowered.veru.temporal;

import slatepowered.veru.data.Pair;
import slatepowered.veru.string.StringReader;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

public class DurationParser {

    private final List<Pair<ChronoUnit, String>> units = new ArrayList<>();

    {
        units.add(Pair.of(ChronoUnit.YEARS, "y"));
        units.add(Pair.of(ChronoUnit.DAYS, "d"));
        units.add(Pair.of(ChronoUnit.HOURS, "h"));
        units.add(Pair.of(ChronoUnit.MINUTES, "m"));
        units.add(Pair.of(ChronoUnit.SECONDS, "s"));
    }

    /**
     * Stringify the given duration into a short form.
     *
     * @param duration The duration.
     * @return The short form string.
     */
    public String stringifyShort(Duration duration) {
        StringBuilder b = new StringBuilder();
        for (Pair<ChronoUnit, String> unitPair : units) {
            ChronoUnit unit = unitPair.getFirst();
            String unitChar = unitPair.getSecond();

            long amt = duration.get(unit);
            if (amt > 0) {
                duration = duration.minus(amt, unit);
                b.append(amt).append(unitChar).append(" ");
            }
        }

        b.deleteCharAt(b.length() - 2);
        return b.toString();
    }

    /**
     * Parses a duration from the given string reader.
     *
     * @param reader The reader.
     * @return The duration.
     */
    public Duration parse(StringReader reader) {
        Duration duration = Duration.ZERO;
        while (StringReader.isDigit(reader.curr(), 10)) {
            long amount = reader.collectLong();
            String unitShort = reader.collect(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));

            // find the pair
            Pair<ChronoUnit, String> pair = null;
            for (Pair<ChronoUnit, String> pair1 : units) {
                if (pair1.getSecond().equals(unitShort)) {
                    pair = pair1;
                    break;
                }
            }

            if (pair == null) {
                throw new IllegalArgumentException("Unrecognized unit `" + unitShort + "`");
            }

            TemporalUnit unit = pair.getFirst();
            duration = duration.plus(Duration.of(amount, unit));
        }

        return duration;
    }

    public Duration parse(String str) {
        return parse(new StringReader(str));
    }

}
