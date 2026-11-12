package repository;

import java.sql.SQLException;

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

    /**
     * Creates a RepositoryException with a clear message from a JDBC error,
     * based on its SQL state (connection, authentication or constraint errors).
     *
     * @param operatie the operation that failed, for example "Eroare la salvarea utilizatorului"
     * @param e the original SQLException, kept as the cause
     * @return the exception to throw
     */
    public static RepositoryException din(String operatie, SQLException e) {
        String cod = e.getSQLState();
        String mesaj;
        if (cod != null && cod.startsWith("08")) {
            mesaj = "Baza de date nu este disponibila. Verificati ca serverul PostgreSQL este pornit.";
        } else if ("28P01".equals(cod)) {
            mesaj = "Autentificare esuata. Verificati db.user si db.password din config.properties.";
        } else if ("3D000".equals(cod)) {
            mesaj = "Baza de date din db.url nu exista.";
        } else if ("23505".equals(cod)) {
            mesaj = operatie + ": inregistrarea exista deja.";
        } else if ("23503".equals(cod)) {
            mesaj = operatie + ": utilizatorul referit nu exista.";
        } else if ("23514".equals(cod)) {
            mesaj = operatie + ": date invalide (constrangere CHECK incalcata).";
        } else if ("23502".equals(cod)) {
            mesaj = operatie + ": lipseste o valoare obligatorie.";
        } else {
            mesaj = operatie + ": " + e.getMessage();
        }
        return new RepositoryException(mesaj, e);
    }
}
