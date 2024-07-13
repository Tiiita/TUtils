package de.tiiita.config;

public class WrongConfigException extends RuntimeException {
    /**
     * An exception that is thrown when the config id does not match.
     * @see YamlConfig#checkRightConfig(int)
     * @param message the exception message that will be shown in the console.
     */
    public WrongConfigException(String message) {
        super(message);
    }
}
