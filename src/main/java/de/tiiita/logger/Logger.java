package de.tiiita.logger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public abstract class Logger {
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void logInfo(String message) {
        System.out.println(ANSI_RESET + getPrefix("inf") + " " + message);
    }

    public static void logWarning(String message) {
        System.out.println(ANSI_YELLOW + getPrefix("war") + " " + message);

    }
    public static void logError(String message) {
        System.out.println(ANSI_RED + getPrefix("err") + " " + message);
    }

    public static void logDebug(String message) {
        System.out.println(ANSI_BLUE + getPrefix("deb") + " " + message);
    }

    //[22:01:59] [INFO]:
    private static String getPrefix(String logType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(Date.from(Instant.now()));

        return "[" + time + "] [" + logType.toUpperCase() + "]" ;
    }
}
