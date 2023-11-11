package slatepowered.veru.reflect;

import slatepowered.veru.misc.Throwables;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

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

}
