package de.tiiita.cli.exception;

public class NoArgumentFoundException extends CommandSyntaxException {

  public NoArgumentFoundException(String message) {
    super(message);
  }
}
