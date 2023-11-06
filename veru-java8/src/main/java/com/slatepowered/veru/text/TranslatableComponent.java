package com.slatepowered.veru.text;

public class TranslatableComponent extends MutableComponent {

    /**
     * The translation key.
     */
    private final String key;

    public TranslatableComponent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getText(TextContext context) {
        return context.getService(TranslationService.class).getOrDefault(key, key);
    }

}
