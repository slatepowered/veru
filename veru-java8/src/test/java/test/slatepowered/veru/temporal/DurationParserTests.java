package test.slatepowered.veru.temporal;

import org.junit.jupiter.api.Test;
import slatepowered.veru.temporal.DurationParser;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.*;

public class DurationParserTests {

    @Test
    void test_Emit() {
        final DurationParser durationParser = new DurationParser();

        Duration a = Duration.of(6000, HOURS);
        Duration b = Duration.of(10000, MINUTES);
        Duration c = Duration.of(69696, SECONDS);
        Duration d = Duration.ofSeconds(6000, 492234234);

        // 1: default settings
        System.out.println("   Default settings:");
        System.out.println(durationParser.stringifyShort(a));
        System.out.println(durationParser.stringifyShort(b));
        System.out.println(durationParser.stringifyShort(c));
        System.out.println(durationParser.stringifyShort(d));

        // 2: decimal settings
        durationParser.withNumberFormat(new DecimalFormat("0.0"));
        System.out.println("   Decimal settings:");
        System.out.println(durationParser.stringifyShort(a));
        System.out.println(durationParser.stringifyShort(b));
        System.out.println(durationParser.stringifyShort(c));
        System.out.println(durationParser.stringifyShort(d));

    }

    @Test
    void test_Parse() {
        final DurationParser durationParser = new DurationParser();
        System.out.println(durationParser.parse("6m5s").getSeconds());
    }

}
