package de.tiiita;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * This is a custom logger which supports colored and uncolored output.
 * It allows you to get the full log as a file.
 */
public abstract class Logger {
    private static boolean colors = true;
    private static final String ANSI_YELLOW = "\u001B[38;2;166;138;13m";
    private static final String ANSI_RED = "\u001B[38;2;226;77;71m";
    private static final String ANSI_BLUE = "\u001B[38;2;42;125;211m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final File logFile;

    static {
        try {
            logFile = Files.createTempFile("logs", ".log").toFile();
            logFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Log an information to the console and the temp log file.
     *
     * @param message the message you want to log.
     *                <p>
     *                If the colors are enabled, which they are by default, this prints the message in white.
     *                The log file entry will have no color.
     * @see #log(String, String, String)
     */
    public static void logInfo(String message) {
        log(message, ANSI_RESET, "inf");
    }

    /**
     * Log a warning to the console and the temp log file.
     * Use this method for things that are not going right but are not an error though.
     * <p>
     * If the colors are enabled, which they are by default, this prints the message in yellow.
     * The log file entry will have no color.
     *
     * @param message the message you want to log.
     * @see #log(String, String, String)
     */
    public static void logWarning(String message) {
        log(message, ANSI_YELLOW, "war");

    }

    /**
     * Use this to log an error to the console.
     * It is better to use Exception in the most use cases but sometimes, you want
     * to log an error without exception, for example when no internet connection is available.
     * <p>
     * If the colors are enabled, which they are by default, this prints the message in red.
     * The log file entry will have no color.
     *
     * @param message the message you want to log.
     * @see #log(String, String, String)
     */
    public static void logError(String message) {
        log(message, ANSI_RED, "err");
    }

    /**
     * This should just be used temporally to debug information in the code for the developer.
     * <p>
     * If the colors are enabled, which they are by default, this prints the message in blue.
     * The log file entry will have no color.
     *
     * @param message the message you want to log.
     */
    public static void logDebug(String message) {
        log(message, ANSI_BLUE, "deb");
    }

    /**
     * This method just makes a free space in the console.
     * The log file will not be affected by this.
     */
    public static void makeSpace() {
        System.out.println(" ");
    }

    private static void log(String message, String ansiColor, String prefix) {
        String nonColorLog = getPrefix(prefix) + " " + message;
        if (colors) {
            System.out.println(ansiColor + getPrefix(prefix) + " " + message + ANSI_RESET);
        } else System.out.println(nonColorLog);

        writeToLog(nonColorLog + "\n");
    }


    private static void writeToLog(String string) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(logFile)) {
            fileOutputStream.write(string.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static File getLogFile() {
        return logFile;
    }

    //Example: [22:01:59] [INFO]:
    private static String getPrefix(String logType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(Date.from(Instant.now()));

        return "[" + time + "] [" + logType.toUpperCase() + "]";
    }

    public static void disableColors() {
        colors = false;
    }

    public static void enableColors() {
        colors = true;
    }
}
