package repository;

import domain.Pereche;
import domain.Prietenie;
import validators.Validator;

/**
 * Skeleton database repository for friendships; implemented in lab 4.
 */
public class PrietenieDbRepository implements Repository<Pereche<Integer, Integer>, Prietenie> {
    private final Validator<Prietenie> validator;
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
    public PrietenieDbRepository(Validator<Prietenie> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Prietenie findOne(Pereche<Integer, Integer> id) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Iterable<Prietenie> findAll() {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Prietenie save(Prietenie entity) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Prietenie delete(Pereche<Integer, Integer> id) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }

    @Override
    public Prietenie update(Prietenie entity) {
        throw new UnsupportedOperationException("Implemented in lab 4");
    }
}