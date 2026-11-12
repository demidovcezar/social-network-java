package Service;

/**
 * Unchecked exception thrown when a business rule is broken.
 */
public class ServiceException extends RuntimeException {
    /**
     * Creates the exception with a message.
     *
     * @param message the description of the error
     */
    public ServiceException(String message) {
        super(message);
    }
}
