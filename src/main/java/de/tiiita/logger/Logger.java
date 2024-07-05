package de.tiiita.logger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public abstract class Logger {
    private static final String ANSI_YELLOW = "\u001B[38;2;166;138;13m";
    private static final String ANSI_RED = "\u001B[38;2;226;77;71m";
    private static final String ANSI_BLUE = "\u001B[38;2;42;125;211m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void logInfo(String message) {
        System.out.println(ANSI_RESET + getPrefix("inf") + " " + message + ANSI_RESET);
    }

    public static void logWarning(String message) {
        System.out.println(ANSI_YELLOW + getPrefix("war") + " " + message + ANSI_RESET);

    }
    public static void logError(String message) {
        System.out.println(ANSI_RED + getPrefix("err") + " " + message + ANSI_RESET);
    }

    public static void logDebug(String message) {
        System.out.println(ANSI_BLUE + getPrefix("deb") + " " + message + ANSI_RESET);
    }

    //[22:01:59] [INFO]:
    private static String getPrefix(String logType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(Date.from(Instant.now()));

        return "[" + time + "] [" + logType.toUpperCase() + "]" ;
    }
}
