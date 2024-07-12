package de.tiiita;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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

    public static void logInfo(String message) {
        log(message, ANSI_RESET, "inf");
    }

    public static void logWarning(String message) {
        log(message, ANSI_YELLOW, "war");

    }

    public static void logError(String message) {
        log(message, ANSI_RED, "err");
    }

    public static void logDebug(String message) {
        log(message, ANSI_BLUE, "deb");
    }

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
