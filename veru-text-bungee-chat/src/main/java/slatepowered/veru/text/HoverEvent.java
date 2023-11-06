package slatepowered.veru.text;

import lombok.Data;

@Data
public class HoverEvent {

    public enum Action {
        SHOW_TEXT,
        SHOW_ACHIEVEMENT,
        SHOW_ITEM,
        SHOW_ENTITY
    }

    /**
     * The action of this hover event.
     */
    private final Action action;

    /**
     * The component value.
     */
    private final TextComponent value;

}
