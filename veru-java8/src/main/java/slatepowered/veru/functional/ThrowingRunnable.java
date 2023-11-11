package slatepowered.veru.functional;

import slatepowered.veru.misc.Throwables;

@FunctionalInterface
public interface ThrowingRunnable extends Runnable {

    /**
     * Execute this potentially throwing runnable.
     *
     * @throws Throwable Any error which may occur.
     */
    void runThrowing() throws Throwable;

    /**
     * Executes this potentially throwing runnable sneakily
     * rethrowing any errors which may be thrown.
     */
    @Override
    default void run() {
        try {
            runThrowing();
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

}
