package repository;

/**
 * Unchecked exception thrown for persistence and configuration errors.
 */
public class RepositoryException extends RuntimeException{
     /**
      * Creates the exception with a message.
      *
      * @param message the description of the error
      */
     public RepositoryException(String message) {
         super(message);
     }

    /**
     * Creates the exception with a message and the original cause.
     *
     * @param message the description of the error
     * @param cause the original exception
     */
    public RepositoryException(String message, Throwable cause){
        super(message, cause);
    }
}
