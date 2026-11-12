package repository;

import domain.Entity;
import validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Repository that keeps the entities in memory, in a map indexed by id.
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Validator<E> validator;
    private final Map<ID, E> repo = new HashMap<>();

    /**
     * Creates an empty repository.
     *
     * @param v the validation strategy
     * @throws NullPointerException if v is null
     */
    public InMemoryRepository(Validator<E> v) {
        this.validator = Objects.requireNonNull(v, "Validator obligatoriu!");
    }

    @Override
    public E findOne(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id nu poate fi null!");
        }
        return repo.get(id);
    }

    @Override
    public Iterable<E> findAll() { // live view of the stored entities; do not modify the repository while iterating
        return repo.values();
    }

    /**
     * Saves the entity; if it has no id, the id is generated with {@link #genereazaId()}.
     *
     * @param entity the entity to save
     * @return null if the entity was saved, or the existing entity with the same id
     * @throws IllegalArgumentException if entity is null or no id can be generated
     * @throws validators.ValidationException if the entity is not valid
     */
    @Override
    public E save(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity nu poate fi null!");
        }
        validator.validate(entity);
        if (entity.getId() == null) {
           entity.setId(genereazaId());
        }
        if (entity.getId() == null) {
            throw new IllegalArgumentException("Entitatea nu are id si repository-ul nu poate genera unul");
        }
        return repo.putIfAbsent(entity.getId(), entity);   // null daca s-a salvat, altfel entitatea existenta
    }

    @Override
    public E delete(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nu poate fi null!");
        }
        return repo.remove(id);
    }

    @Override
    public E update(E entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity nu poate fi null!");
        }
        validator.validate(entity);
        if (entity.getId() == null) {
            throw new IllegalArgumentException("ID-ul entitatii nu poate fi null!");
        }
        if (repo.containsKey(entity.getId())) {
            repo.put(entity.getId(), entity);
            return null;                                    // actualizat cu succes
        }
        return entity;                                      // id inexistent
    }
    /**
     * Generates the id of a new entity (hook for subclasses, Template Method).
     * By default the repository cannot generate ids, so it returns null.
     *
     * @return the new id, or null if this repository cannot generate one
     */
    protected ID genereazaId() {
        return null;
    }

}