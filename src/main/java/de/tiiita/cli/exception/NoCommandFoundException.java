package de.tiiita.cli.exception;

public class NoCommandFoundException extends CommandSyntaxException {

  public NoCommandFoundException(String message) {
    super(message);
  }
}
