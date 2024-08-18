package de.tiiita.cli;

public class MissingAnnotationException extends RuntimeException {

  public MissingAnnotationException(String message) {
    super(message);
  }
}
