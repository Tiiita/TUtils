package de.tiiita.util;

/**
 * Simple class that automatically reports when a method is called in the wrong thread.
 */
public class ThreadChecker {

    /**
     * Ad this to you methods body, and it will automatically throw an IllegalStateException if the method is called in the main thread.
     */
    public static void asyncOnly() {
        asyncOnly("main");
    }

    /**
     * Ad this to you methods body, and it will automatically throw an IllegalStateException if the method is called in
     * a different thread than the main thread.
     */
    public static void syncOnly() {
        syncOnly("main");
    }

    /**
     * Ad this to you methods body, and it will automatically throw an IllegalStateException if the method is called in the main thread.
     *
     * @param mainThread the name of the main thread. If the main thread is called "main" use {@link #asyncOnly()}
     */
    public static void asyncOnly(String mainThread) {
        if (Thread.currentThread().getName().equalsIgnoreCase(mainThread)) {
            throw new IllegalStateException("Method is called sync but is marked as an async only method!");
        }
    }

    /**
     * Ad this to you methods body, and it will automatically throw an IllegalStateException if the method is called in
     * a different thread than the main thread.
     *
     * @param mainThread the name of the main thread. If the main thread is called "main" use {@link #syncOnly()}
     */
    public static void syncOnly(String mainThread) {
        if (!Thread.currentThread().getName().equalsIgnoreCase(mainThread)) {
            throw new IllegalStateException("Method is called async but is marked as a sync only method!");
        }
    }
}
