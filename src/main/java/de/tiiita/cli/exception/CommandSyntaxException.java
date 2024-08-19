package de.tiiita.cli.exception;

public class CommandSyntaxException extends RuntimeException {

  public CommandSyntaxException(String message) {
    super(message);
  }
}
