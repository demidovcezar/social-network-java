package validators;

/**
 * Unchecked exception thrown when an entity is not valid.
 */
public class ValidationException extends RuntimeException {
    /**
     * Creates the exception with a message.
     *
     * @param message the description of the errors
     */
    public ValidationException(String message) {
        super(message);
    }
}
