package de.tiiita.minecraft.util;

/**
 * Just a class to difference exceptions for better error handling.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
