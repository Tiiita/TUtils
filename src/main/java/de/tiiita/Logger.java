package de.tiiita;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * This is a custom logger which supports colored and uncolored output. It allows you to get the
 * full log as a file.
 */
public abstract class Logger {
  private static final String ANSI_YELLOW = "\u001B[38;2;166;138;13m";
  private static final String ANSI_RED = "\u001B[38;2;226;77;71m";
  private static final String ANSI_BLUE = "\u001B[38;2;42;125;211m";
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\033[38;5;10m";

  /**
   * Log an information to the console and the temp log file.
   *
   * @param message the message you want to log.
   *                <p>
   *                If the colors are enabled, which they are by default, this prints the message in
   *                white. The log file entry will have no color.
   * @see #getLogMessage(String, String, String)
   */
  public static void logInfo(String message) {
    System.out.println(getLogMessage(message, ANSI_GREEN, "info"));
  }

  /**
   * Log a warning to the console and the temp log file. Use this method for things that are not
   * going right but are not an error though.
   * <p>
   * If the colors are enabled, which they are by default, this prints the message in yellow. The
   * log file entry will have no color.
   *
   * @param message the message you want to log.
   * @see #getLogMessage(String, String, String)
   */
  public static void logWarning(String message) {
    System.out.println(getLogMessage(message, ANSI_YELLOW, "warning"));

  }

  /**
   * Use this to log an error to the console. It is better to use Exception in the most use cases
   * but sometimes, you want to log an error without exception, for example when no internet
   * connection is available.
   * <p>
   * If the colors are enabled, which they are by default, this prints the message in red. The log
   * file entry will have no color.
   *
   * @param message the message you want to log.
   * @see #getLogMessage(String, String, String)
   */
  public static void logError(String message) {
    System.out.println(getLogMessage(message, ANSI_RED, "error"));
  }

  /**
   * This should just be used temporally to debug information in the code for the developer.
   * <p>
   * If the colors are enabled, which they are by default, this prints the message in blue. The log
   * file entry will have no color.
   *
   * @param message the message you want to log.
   */
  public static void logDebug(String message) {
    System.out.println(getLogMessage(message, ANSI_BLUE, "debug"));
  }

  /**
   * This method just makes a free space in the console. The log file will not be affected by this.
   */
  public static void makeSpace() {
    System.out.println(" ");
  }

  public static String getLogMessage(String message, String ansiColor, String prefix) {
    return getPrefix(prefix, ansiColor) + " " + message;
  }


  public static File getLogAsFile(String path) {
    File file = new File(path);
    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(System.in.readAllBytes());
      outputStream.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return file;
  }

  private static String getPrefix(String logType, String color) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String formattedDate = now.format(formatter) + " ";

    return color == null ? formattedDate + logType.toUpperCase() + ":"
        : formattedDate + color + logType.toUpperCase() + getAnsiResetWhite() + ":";
  }


  public static String getAnsiYellow() {
    return ANSI_YELLOW;
  }

  public static String getAnsiBlue() {
    return ANSI_BLUE;
  }

  public static String getAnsiRed() {
    return ANSI_RED;
  }

  public static String getAnsiResetWhite() {
    return ANSI_RESET;
  }

  public static String getAnsiGreen() {
    return ANSI_GREEN;
  }

}
