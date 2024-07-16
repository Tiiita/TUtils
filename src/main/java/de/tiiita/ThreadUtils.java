package de.tiiita;

import java.util.function.Supplier;

/**
 * Simple class that can block thread with a condition or warn if a method is called in a thread it should not be.
 */
public class ThreadUtils {
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

    /**
     * This is a method which blocks the current thread until the condition is true. There is a time limit,
     * so it does not block infinitely if the condition is never true.
     * <p>
     * It uses 500-millisecond sleep intervals, so it can be a little delay between the met condition and the thread wakeup.
     * @param condition the condition for stopping the thread sleep.
     * @param maxSeconds the maximum time the thread can sleep. If this duration exceeds, this method returns if
     *                   the condition was not true until.
     */
    public static void sleepUntil(Supplier<Boolean> condition, int maxSeconds) {
        try {
            for (int i = 0; i < maxSeconds; i++) {
                if (condition.get()) {
                    Thread.sleep(500);
                    return;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
