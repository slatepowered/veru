package slatepowered.veru.text;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class Style {

    public static class StyleBuilder {
        Boolean bold;
        Boolean italic;
        Boolean underline;
        Boolean strikethrough;
        Boolean obfuscated;
        TextColor color;
        Map<String, Object> properties;

        public Style build() {
            return new Style(bold, italic, underline, strikethrough, obfuscated, color, properties);
        }

        public StyleBuilder bold(Boolean bold) {
            this.bold = bold;
            return this;
        }

        public StyleBuilder italic(Boolean italic) {
            this.italic = italic;
            return this;
        }

        public StyleBuilder underline(Boolean underline) {
            this.underline = underline;
            return this;
        }

        public StyleBuilder strikethrough(Boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public StyleBuilder obfuscated(Boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public StyleBuilder color(TextColor color) {
            this.color = color;
            return this;
        }

        public StyleBuilder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public StyleBuilder property(String name, Object o) {
            if (this.properties == null) {
                this.properties = new LinkedHashMap<>();
            }

            this.properties.put(name, o);
            return this;
        }

        /**
         * Adds a nameless/anonymous property.
         */
        public StyleBuilder add(Object o) {
            return property("anon " + Long.toHexString(System.currentTimeMillis() ^ System.identityHashCode(o)), o);
        }
    }

    public static StyleBuilder builder() {
        return new StyleBuilder();
    }

    public static Style empty() {
        return new Style(null, null, null, null, null, null, null);
    }

    /* Styling */
    public Boolean bold;
    public Boolean italic;
    public Boolean underline;
    public Boolean strikethrough;
    public Boolean obfuscated;

    /* Color */
    public TextColor color;

    /* Other properties */
    public Map<String, Object> properties;

    protected void createPropertiesIfAbsent() {
        if (properties == null) {
            properties = new LinkedHashMap<>();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getProperty(String name) {
        if (properties == null || !properties.containsKey(name))
            return Optional.empty();
        return (Optional<T>) Optional.ofNullable(properties.get(name));
    }

}
