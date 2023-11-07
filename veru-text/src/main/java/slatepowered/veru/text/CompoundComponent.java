package slatepowered.veru.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CompoundComponent extends MutableComponent {

    /**
     * All tagged components registered.
     */
    private Map<String, TextComponent> taggedComponents;

    public CompoundComponent() {
        super();
    }

    public CompoundComponent(List<TextComponent> children) {
        super(children);
    }

    @Override
    public String getText(TextContext context) {
        return null;
    }

    public CompoundComponent append(TextComponent child, String tag) {
        if (tag != null) {
            if (taggedComponents == null)
                taggedComponents = new HashMap<>();
            taggedComponents.put(tag, child);
        }

        super.append(child);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <C extends TextComponent> C get(String tag) {
        if (taggedComponents == null)
            return null;
        return (C) taggedComponents.get(tag);
    }

    public <C extends MutableComponent> CompoundComponent edit(String tag, Consumer<C> consumer) {
        C comp = get(tag);
        if (consumer != null)
            consumer.accept(comp);
        return this;
    }

}
