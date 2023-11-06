package com.slatepowered.veru.text;

public class LiteralComponent extends MutableComponent {

    /**
     * The text of this component.
     */
    private final String text;

    public LiteralComponent(String text) {
        this.text = text;
    }

    @Override
    public String getText(TextContext context) {
        return text;
    }

}
