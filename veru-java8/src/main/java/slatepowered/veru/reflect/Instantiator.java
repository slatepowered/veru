package slatepowered.veru.reflect;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Creates an instance of T.
 *
 * @param <T> The instance type.
 */
@FunctionalInterface
public interface Instantiator<T> {

    T create() throws InstantiationException;

    /**
     * Find an instantiator which will use the most appropriate
     * constructor to create an instance of the given of the given
     * class with the given injectable values.
     *
     * @param tClass The class to instantiate.
     * @param injectableValues The values.
     * @param <T> The instance type.
     * @return The instantiator.
     */
    @SuppressWarnings("unchecked")
    static <T> Instantiator<T> fitInstantiator(Class<T> tClass, List<Object> injectableValues) {
        int bestScore = 0;
        Constructor<?> bestFit = null;
        Object[] bestArgumentArrangement = null;

        outer: for (Constructor<?> constructor : tClass.getConstructors()) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            final int paramCount = paramTypes.length;

            int score = 0;
            Object[] argArrangement = new Object[paramCount];

            // arrange arguments and calculate score
            for (int i = 0; i < paramCount; i++) {
                Class<?> paramType = paramTypes[i];

                // find best argument value
                Object argValue = null;
                boolean hadNullValue = false;
                for (Object v : injectableValues) {
                    if (v == null) {
                        hadNullValue = true;
                        continue;
                    }

                    if (paramType.isAssignableFrom(v.getClass())) {
                        argValue = v;
                        break;
                    }
                }

                if (argValue == null && !hadNullValue)
                    continue outer; // skip this constructor because there
                                    // is no way to invoke it with the given values

                argArrangement[i] = argValue;
                score += argValue != null ? 1 : 0; // todo: score based on distance to parameter class
            }

            if (score > bestScore) {
                bestFit = constructor;
                bestArgumentArrangement = argArrangement;
            }
        }

        Constructor<?> finalBestFit = bestFit;
        Object[] finalBestArgumentArrangement = bestArgumentArrangement;
        return bestFit == null ? null : () -> {
            try {
                return (T) finalBestFit.newInstance(finalBestArgumentArrangement);
            } catch (InstantiationException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Unhandled exception in instantiation of " + tClass, e);
            }
        };
    }

}
