package domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A friendship between two users;
 * the id is the pair of user ids, smaller id first.
 */
public class Prietenie extends Entity<Pereche<Integer, Integer>> {
    private final LocalDateTime date;
    /**
     * Creates a friendship dated now.
     * @param a the id of one user
     * @param b the id of the other user
     */

    public Prietenie(int a, int b) {

        this(a, b, LocalDateTime.now());

    }

    /**
     * Creates a friendship with the given date.
     * @param a the id of one user
     * @param b the id of the other user
     * @param date when the friendship started
     */
    public Prietenie(int a, int b, LocalDateTime date)
    {
        super(new Pereche<>(Math.min(a, b), Math.max(a, b)));   // mai micul primul
        this.date = date;

    }

    /**
     * Returns the smaller user id.
     *
     * @return the smaller user id
     */
    public int getIdUtilizator1() {
        return getId().getE1();
    }

    /**
     * Returns the larger user id.
     *
     * @return the larger user id
     */
    public int getIdUtilizator2() {
        return getId().getE2();
    }

    /**
     * Returns the date of the friendship.
     *
     * @return the date of the friendship
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return Objects.equals(getId(), ((Prietenie) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Prietenie: " + getIdUtilizator1() + " - " + getIdUtilizator2();
    }
}