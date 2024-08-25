package de.tiiita;

import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * Simple class that can block thread with a condition or warn if a method is called in a thread it
 * should not be.
 */
public class Threads {

  private static String defaultMainThread = "main";
  private final static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("thread-safety");
  /**
   * Add this to you methods body, and it will automatically print a warning if the method is called
   * in the main thread.
   */
  public static void asyncOnly() {
    asyncOnly(defaultMainThread);
  }

  /**
   * Add this to you methods body, and it will automatically print a warning if the method is called
   * in a different thread than the main thread.
   */
  public static void syncOnly() {
    syncOnly(defaultMainThread);
  }

  /**
   * Add this to you methods body, and it will automatically print a warning if the method is called
   * in the main thread.
   *
   * @param mainThread the name of the main thread. If the main thread is called "main" use
   *                   {@link #asyncOnly()}
   */
  public static void asyncOnly(String mainThread) {
    if (Thread.currentThread().getName().equalsIgnoreCase(mainThread)) {
          logger.log(Level.WARNING, "Method is called sync but is marked as an async only method!");
    }
  }

  /**
   * Add this to you methods body, and it will automatically print a warning if the method is called
   * in a different thread than the main thread.
   *
   * @param mainThread the name of the main thread. If the main thread is called "main" use
   *                   {@link #syncOnly()}
   */
  public static void syncOnly(String mainThread) {
    if (!Thread.currentThread().getName().equalsIgnoreCase(mainThread)) {
      logger.log(Level.WARNING, "Method is called async but is marked as a sync only method!");
    }
  }

  /**
   * Override the main thread if the program renames the main thread to a custom name.
   *
   * @param mainThread the new main thread name, where the application runs in.
   */
  public static void setMainThread(String mainThread) {
    defaultMainThread = mainThread;
  }


  /**
   * This method is an overwritten method for precise sleeping with condition. The main logic is in:
   * {@link #sleepUntil(Supplier, int, int)}
   */
  public static void sleepUntil(Supplier<Boolean> condition, int maxMillis) {
    sleepUntil(condition, maxMillis, 1);
  }

  /**
   * This is a method which blocks the current thread until the condition is true. There is a time
   * limit, so it does not block infinitely if the condition is never true.
   * <p>
   * It uses a 1-millisecond sleep intervals, so it can sleep very precisely.
   *
   * @param condition      the condition for stopping the thread sleep.
   * @param maxMillis      the maximum time the thread can sleep. If this duration exceeds, this
   *                       method returns if the condition was not true until.
   * @param intervalMillis this is the interval the thread sleeps. So if this is set to 500 and the
   *                       condition is met after 200ms it would still sleep 300 ms.
   * @see #sleepUntil(Supplier, int)
   */
  public static void sleepUntil(Supplier<Boolean> condition, int maxMillis, int intervalMillis) {
    for (int i = 0; i < maxMillis; i++) {
      if (condition.get()) {
        return;
      }

      sleep(intervalMillis);
    }
  }

  /**
   * This is just a method that uses {@link Thread#sleep(long)} for you, so you do not have to do
   * that annoying try-catch any time!
   *
   * @param millis the milliseconds you want the current thread to sleep.
   */
  public static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
