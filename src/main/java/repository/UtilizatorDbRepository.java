package repository;

import domain.Utilizator;
import validators.Validator;

/**
 * Skeleton database repository for users; implemented in lab 4.
 */
public class UtilizatorDbRepository implements Repository<Integer, Utilizator> {
    private final Validator<Utilizator> validator;
    private final String url;
    private final String user;
    private final String password;

    /**
     * Creates the skeleton repository with the connection parameters.
     *
     * @param validator the validation strategy
     * @param url the database url
     * @param user the database user
     * @param password the database password
     */
    public UtilizatorDbRepository(Validator<Utilizator> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Utilizator findOne(Integer id) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Iterable<Utilizator> findAll() {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Utilizator save(Utilizator entity) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Utilizator delete(Integer id) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Utilizator update(Utilizator entity) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }
}