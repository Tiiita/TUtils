package de.tiiita;

/**
 * Simple class that automatically reports when a method is called in the wrong thread.
 */
public class ThreadChecker {
    private static String DEFAULT_MAIN_THREAD = "main";

    /**
     * Add this to you methods body, and it will automatically print a warning if the method is called in the main thread.
     */
    public static void asyncOnly() {
        asyncOnly(DEFAULT_MAIN_THREAD);
    }

    /**
     * Add this to you methods body, and it will automatically print a warning if the method is called in
     * a different thread than the main thread.
     */
    public static void syncOnly() {
        syncOnly(DEFAULT_MAIN_THREAD);
    }

    /**
     * Add this to you methods body, and it will automatically print a warning if the method is called in the main thread.
     *
     * @param mainThread the name of the main thread. If the main thread is called "main" use {@link #asyncOnly()}
     */
    public static void asyncOnly(String mainThread) {
        if (Thread.currentThread().getName().equalsIgnoreCase(mainThread)) {
            Logger.logWarning("Method is called sync but is marked as an async only method!");
        }
    }

    /**
     * Add this to you methods body, and it will automatically print a warning if the method is called in
     * a different thread than the main thread.
     *
     * @param mainThread the name of the main thread. If the main thread is called "main" use {@link #syncOnly()}
     */
    public static void syncOnly(String mainThread) {
        if (!Thread.currentThread().getName().equalsIgnoreCase(mainThread)) {
            Logger.logWarning("Method is called async but is marked as a sync only method!");
        }
    }

    /**
     * Override the main thread if the program renames the main thread to a custom name.
     * @param mainThread the new main thread name, where the application runs in.
     */
    public static void setMainThread(String mainThread) {
        DEFAULT_MAIN_THREAD = mainThread;
    }
}
