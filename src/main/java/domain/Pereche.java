package domain;

import java.util.Objects;

/**
 * Immutable pair of two values.
 *
 * @param <E1> the type of the first value
 * @param <E2> the type of the second value
 */
public class Pereche<E1,E2> {
    private final E1 e1;
    private final E2 e2;
    /**
     * Creates a pair.
     * @param e1 the first value
     * @param e2 the second value
     */
    public Pereche(E1 e1,E2 e2)
    {
        this.e1=e1;
        this.e2=e2;
    }

    /**
     * Returns the first value.
     *
     * @return the first value
     */
    public E1 getE1() {
        return e1;
    }
    /**
     * Returns the second value.
     *
     * @return the second value
     */
    public E2 getE2()
    {
        return e2;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Pereche<?, ?> altul = (Pereche<?, ?>) o;          // un singur cast, pe obiect
        return Objects.equals(this.e1, altul.e1)
                && Objects.equals(this.e2, altul.e2);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.getE1(),this.getE2());
    }

    @Override
    public String toString() {
        return "(" + e1 + ", " + e2 + ")";
    }
}
