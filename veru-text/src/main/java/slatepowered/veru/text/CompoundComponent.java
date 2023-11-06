package slatepowered.veru.text;

import java.util.List;

public class CompoundComponent extends MutableComponent {

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

}
