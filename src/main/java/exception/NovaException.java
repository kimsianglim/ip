package exception;

/**
 * Represents an exception specific to the Nova application.
 * <p>
 * A {@code NovaException} is thrown when a user command is invalid,
 * malformed, or cannot be processed correctly.
 */
public class NovaException extends Exception {
    public NovaException(String message) {
        super(message);
    }
}
