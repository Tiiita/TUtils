package de.tiiita.cli.exception;

public class NoCommandFoundException extends RuntimeException {

  public NoCommandFoundException(String message) {
    super(message);
  }
}
