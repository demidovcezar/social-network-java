package domain;

import java.util.function.Predicate;

/**
 * Search criteria for users, to be used with {@code Repository.findAll(Predicate)}.
 * The predicates can be combined with {@code and}, {@code or} and {@code negate}.
 */
public class CriteriiUtilizator {

    private CriteriiUtilizator() {
    }

    /**
     * Creates a criterion on the first name prefix.
     *
     * @param prefix the prefix the first name must start with
     * @return a predicate that is true for users whose first name starts with the prefix
     */
    public static Predicate<Utilizator> prenumeleIncepeCu(String prefix) {
        return u -> u.getPrenume().startsWith(prefix);
    }

    /**
     * Creates a criterion on the last name.
     *
     * @param nume the required last name
     * @return a predicate that is true for users with the given last name
     */
    public static Predicate<Utilizator> areNumele(String nume) {
        return u -> u.getNume().equals(nume);
    }

    /**
     * Checks if the last name of a user is short (fewer than 5 characters).
     *
     * @param u the user to check
     * @return true if the last name has fewer than 5 characters
     */
    public static boolean areNumeScurt(Utilizator u) {
        return u.getNume().length() < 5;
    }
}
