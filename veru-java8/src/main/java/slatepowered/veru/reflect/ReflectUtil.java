package slatepowered.veru.reflect;

import slatepowered.veru.misc.Throwables;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utilities for working with reflection.
 */
public class ReflectUtil {

    /**
     * Invokes the default (non-proxy) method, equivalent of
     * {@link java.lang.reflect.InvocationHandler#invokeDefault(Object, Method, Object...)}
     * in Java 16.
     *
     * @param on The object to invoke it on.
     * @param method The method to invoke.
     * @param args The arguments.
     * @return The return value.
     */
    public static Object invokeDefault(Object on, Method method, Object[] args) {
        try {
            MethodHandle handle = UnsafeUtil.findInvokeSpecial(method);
            return handle.invoke(on, args);
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
            throw new AssertionError();
        }
    }

    /**
     * Find all fields, including private ones, for the given class.
     *
     * @param klass The class.
     * @return The list of fields.
     */
    public static List<Field> findAllFields(Class<?> klass) {
        List<Field> fields = new ArrayList<>();
        for (; klass != null; klass = klass.getSuperclass()) {
            fields.addAll(Arrays.asList(klass.getDeclaredFields()));
        }

        return fields;
    }

}
