package slatepowered.veru.concurrent;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;

public class FutureUtil {

    /**
     * Completes with a list of values when all given futures have completed.
     *
     * @param futures The futures.
     * @param <T> The value type.
     * @return The compound future.
     */
    public static <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futures) {
        final int size = futures.size();
        CompletableFuture<List<T>> compoundFuture = new CompletableFuture<>();
        Vector<T> list = new Vector<>();

        for (CompletableFuture<T> future : futures) {
            future.whenComplete((t, throwable) -> {
                if (throwable != null) {
                    compoundFuture.completeExceptionally(throwable);
                    return;
                }

                list.add(t);
                if (list.size() == size) {
                    compoundFuture.complete(list);
                }
            });
        }

        return compoundFuture;
    }

}
