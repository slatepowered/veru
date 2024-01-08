package slatepowered.veru.temporal;

import lombok.Getter;
import slatepowered.veru.string.StringReader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializes durations.
 */
public class DurationParser {

    @Getter
    static class UnitDef implements Comparable<UnitDef> {
        final TemporalUnit unit; // The time unit
        final String alias;      // The alias
        final long nanos;        // The nanos per unit

        public UnitDef(TemporalUnit unit, String alias) {
            this.unit = unit;
            this.alias = alias;
            this.nanos = Durations.toNanos(unit.getDuration());
        }

        @Override
        public int compareTo(UnitDef o) {
            return unit.getDuration().compareTo(o.getUnit().getDuration());
        }
    }

    static final List<UnitDef> STANDARD_UNITS = new ArrayList<>();

    static {
        STANDARD_UNITS.add(new UnitDef(TimeUnits.YEAR, "y"));
        STANDARD_UNITS.add(new UnitDef(ChronoUnit.DAYS, "d"));
        STANDARD_UNITS.add(new UnitDef(ChronoUnit.HOURS, "h"));
        STANDARD_UNITS.add(new UnitDef(ChronoUnit.MINUTES, "m"));
        STANDARD_UNITS.add(new UnitDef(ChronoUnit.SECONDS, "s"));
    }

    /**
     * The list of units sorted from longest to shortest.
     */
    private List<UnitDef> units = STANDARD_UNITS;

    /**
     * The decimal format to use for the last unit.
     */
    private NumberFormat numberFormat;

    public DurationParser withUnit(TemporalUnit unit, String alias) {
        if (units == STANDARD_UNITS) {
            // clone list if were still using the shared
            // standard units instance as it is mutable
            units = new ArrayList<>(units);
        }

        units.add(new UnitDef(unit, alias));
        units.sort(UnitDef::compareTo);
        return this;
    }

    public DurationParser withNumberFormat(NumberFormat format) {
        this.numberFormat = format;
        return this;
    }

    /**
     * Stringify the given duration into a short form.
     *
     * @param duration The duration.
     * @return The short form string.
     */
    public String stringifyShort(Duration duration) {
        long durationNanos = Durations.toNanos(duration);

        StringBuilder b = new StringBuilder();
        int length = units.size();
        for (int i = 0; i < length; i++) {
            UnitDef unitDef = units.get(i);

            long unitNanos = unitDef.getNanos();
            double amount = (double)durationNanos / unitNanos;
//            System.out.println("unit: " + unitDef.getUnit() + " (" + unitDef.getAlias() + ") nanos: " + unitNanos + ", remaining nanos: " + durationNanos + ", amount = " + amount);

            if /* last unit */ (i >= length - 1 && numberFormat != null) {
                if (amount >= 0.001) {
                    b.append(numberFormat.format(amount)).append(unitDef.getAlias()).append(" ");
                }
            } else {
                amount = (long) Math.floor(amount);

                if (amount >= 1) {
                    b.append((long) amount).append(unitDef.getAlias()).append(" ");
                    durationNanos -= amount * unitNanos;
                }
            }
        }

        // delete trailing space if there is any text
        if (b.length() > 0) {
            b.deleteCharAt(b.length() - 1);
        }

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
            double amount = reader.collectDouble();
            String unitShort = reader.collect(c -> (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));

            // find the pair
            UnitDef def = null;
            for (UnitDef unitDef : units) {
                if (unitDef.getAlias().equals(unitShort)) {
                    def = unitDef;
                    break;
                }
            }

            if (def == null) {
                throw new IllegalArgumentException("Unrecognized unit `" + unitShort + "`");
            }

            TemporalUnit unit = def.getUnit();
            duration = duration.plus(Durations.fractional(amount, unit));
        }

        return duration;
    }

    public Duration parse(String str) {
        return parse(new StringReader(str));
    }

}
