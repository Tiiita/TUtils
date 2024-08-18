package de.tiiita;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  private static final String ANSI_YELLOW = "\u001B[38;2;166;138;13m";
  private static final String ANSI_RED = "\u001B[38;2;226;77;71m";
  private static final String ANSI_BLUE = "\u001B[38;2;42;125;211m";
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


  private static String getPrefix(String logType, String color) {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    String formattedDate = now.format(formatter) + " ";

    String uncoloredLog = formattedDate + logType.toUpperCase() + ":";

    return color == null ? uncoloredLog
        : formattedDate + color + logType.toUpperCase() + "\u001B[0m" + ":";
  }
}
