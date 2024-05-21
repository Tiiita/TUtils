package de.tiiita.util;

public class ThreadChecker {

    public static void asyncOnly() {
        if (Thread.currentThread().getName().equalsIgnoreCase("main")) {
            throw new IllegalStateException("Method is called sync but is marked as an async only method!");
        }
    }

    public static void syncOnly() {
        if (!Thread.currentThread().getName().equalsIgnoreCase("main")) {
            throw new IllegalStateException("Method is called async but is marked as a sync only method!");
        }
    }
}
