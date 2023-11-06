package slatepowered.veru.string;

import slatepowered.veru.data.Values;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Strings {

    public static String repeat(String str, int times) {
        StringBuilder b = new StringBuilder(str.length() * times);
        for (int i = 0; i < times; i++)
            b.append(str);
        return b.toString();
    }

    /**
     * Join the given list of objects together into
     * a string with the given delimiter.
     *
     * @param list The objects.
     * @param delimiter The delimiter.
     * @return The joined string.
     */
    public static <T> String join(List<T> list, String delimiter) {
        StringBuilder b = new StringBuilder();
        final int l = list.size();
        for (int i = 0; i < l; i++) {
            Object o = list.get(i);
            if (i != 0) {
                b.append(delimiter);
            }

            b.append(o);
        }

        return b.toString();
    }

    /**
     * Count the amount of newlines in the given string
     * from index {@code start} to including {@code end}.
     *
     * @param str The string.
     * @param start The start index.
     * @param end The end index.
     * @return The amount of newlines in that segment.
     */
    public static int countLines(String str, int start, int end) {
        if (end == -1)
            end = str.length() - 1;
        final int end2 = Math.min(str.length() - 1, end);
        int count = 0;
        int i = start;
        for (; i <= end2; i++) {
            char c = str.charAt(i);
            if (c == '\n')
                count++;
        }

        if (i == str.length()) {
            count++;
        }

        return count;
    }

    /**
     * Declares a mapping from regular digits
     * to other scripts such as superscript or subscript.
     */
    @Data
    public static class DigitScript {
        /** The name of this digit script */
        private final String name;
        /** At what digit (0-9) does it start supporting */
        private final int digitBase;
        /** The map from  */
        private final String digitMap;

        public String translate(String in) {
            final int l = in.length();
            char[] result = new char[l];
            for (int i = 0; i < l; i++) {
                char source = in.charAt(i);
                result[i] = mapChar(source);
            }

            return new String(result);
        }

        /**
         * Map the given character to the resulting
         * character following this digit script.
         *
         * @param in The input character.
         * @return The resulting character or the input if the mapping failed.
         */
        public char mapChar(char in) {
            int n = Character.getNumericValue(in);
            if (n == -1)
                return in;
            char res = mapDigit(n);
            return res == 0 ? in : res;
        }

        public char mapDigit(int digit) {
            if (digit < digitBase)
                return 0;
            int idx = digit - digitBase;
            if (idx >= digitMap.length())
                return 0;
            return digitMap.charAt(idx);
        }
    }

    static final Map<String, DigitScript> digitScriptMap = new HashMap<>();

    public static DigitScript getDigitScript(String name) {
        return digitScriptMap.get(name);
    }

    public static void putDigitScript(DigitScript data) {
        digitScriptMap.put(data.name, data);
    }

    static {
        putDigitScript(new DigitScript("superscript", 0, "⁰¹²³⁴⁵⁶⁷⁸⁹"));
        putDigitScript(new DigitScript("subscript", 0, "₀₁₂₃₄₅₆₇₈₉"));
        putDigitScript(new DigitScript("encircled", 1, "①②③④⑤⑥⑦⑧⑨"));
    }

    /**
     * Replaces the placeholders in the string.
     * By format {@code %name%}. Gets the value
     * from the {@code values} by name.
     *
     * @param str The string.
     * @param values The values.
     * @return The result string.
     */
    public static String replacePlaceholders(String str, Values values) {
        // create objects
        StringReader reader = new StringReader(str);
        StringBuilder builder = new StringBuilder();

        // loop over characters
        char c;
        while ((c = reader.current()) != StringReader.DONE) {
            // check escape character
            if (c == '\\') {
                builder.append(reader.next());
                reader.next();
                continue;
            }

            // check for placeholder
            if (c == '%') {
                // collect name
                reader.next();
                String name = reader.collect(c1 -> c1 != '%');
                reader.next();

                // check for name
                if (!values.contains(name)) {
                    builder.append("%").append(name).append("%");
                    continue;
                }

                // get value
                Object val = values.get(name);

                // append value
                builder.append(val);

                // continue
                continue;
            }

            // append character
            builder.append(c);
            reader.next();
        }

        // return string
        return builder.toString();
    }

    /**
     * Parses the string and evaluates the expression
     *
     *
     * @param str The string.
     * @param values The value context.
     * @return The output string.
     */
    @SuppressWarnings("unchecked")
    public static String parseExtras(String str, Values values) {
        // create default context
        if (values == null)
            // TODO: defaults
            values = new Values();

        // create objects
        StringReader reader = new StringReader(str);
        StringBuilder builder = new StringBuilder();

        // loop over string
        char c;
        while ((c = reader.current()) != StringReader.DONE) {
            // check for escape character
            if (c == '\\') {
                builder.append(reader.next());
                reader.next();
                continue;
            }

            // check for eval character
            if (c == '#') {
                // collect name
                reader.next();
                String name = reader.collect(c1 -> c1 != '(' && c1 != ' ');
                // the value container
                Object v;
                // check for function call
                if (reader.current() == '(') {
                    // collect parameters
                    List<String> params = new ArrayList<>();
                    while ((reader.current() == '(' ||
                            reader.current() == ',') && reader.current() != ')') {
                        reader.next();
                        final boolean nest = reader.current() == '{';
                        if (nest) reader.next();
                        String val = reader.collect(c1 -> c1 != ',' && c1 != ')' && c1 != '}');
                        if (nest)
                            val = parseExtras(val, values);
                        if (reader.current() == '}')
                            reader.next();

                        params.add(val);
                    }

                    // advance past right parenthesis
                    reader.next();

                    // get the value and check
                    Object g = values.get(name);
                    if (g == null)
                        throw new IllegalArgumentException("No function by name " + name);
                    if (!(g instanceof Function))
                        throw new IllegalArgumentException("Value " + name + " is not a function");
                    Function<List<String>, Object> function = (Function<List<String>, Object>) g;

                    // yield the result
                    v = function.apply(params);
                } else {
                    // get variable value
                    v = values.get(name);
                }

                // append value
                builder.append(v);
            } else {
                // append literal character
                builder.append(c);
                // advance
                reader.next();
            }
        }

        // return string
        return builder.toString();
    }

}
