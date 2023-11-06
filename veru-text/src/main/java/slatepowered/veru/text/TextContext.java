package slatepowered.veru.text;

import java.util.HashMap;
import java.util.Map;

/**
 * The context in which text exists.
 */
public class TextContext {

    /**
     * The services on this context.
     */
    private final Map<Class<?>, Object> services = new HashMap<>();

    public TextContext addService(Object service) {
        for (Class<?> klass = service.getClass(); klass != null; klass = klass.getSuperclass()) {
            services.put(klass, service);
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    public <C> C getService(Class<C> cClass) {
        return (C) services.get(cClass);
    }

}
