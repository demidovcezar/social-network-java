package validators;

/**
 * Validation strategy for entities.
 *
 * @param <T> the type of the validated entities
 */
@FunctionalInterface
public interface Validator<T> {
    /**
     * Combines this validator with another one.
     *
     * @param other the validator applied after this one
     * @return a validator that applies this one first, then {@code other}
     * @throws IllegalArgumentException if other is null
     */
    default  Validator<T> andThen(Validator<T> other)
    {
        if(other == null)
        {
            throw new IllegalArgumentException("other validator cannot be null");
        }
        return t ->
        {
            this.validate(t);
            other.validate(t);
        };

    }
    /**
     * Checks the entity.
     *
     * @param t the entity to validate
     * @throws ValidationException if the entity is not valid
     */
    void validate(T t) throws ValidationException;
}
