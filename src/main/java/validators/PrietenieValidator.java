package validators;

import domain.Prietenie;

/**
 * Validates friendships: the two user ids must be different.
 */
public class PrietenieValidator implements Validator<Prietenie> {
    /**
     * Checks that the two user ids are different.
     *
     * @param t the friendship to validate
     * @throws ValidationException if the two ids are equal
     */
    @Override
    public void validate(Prietenie t) {
        String erori = "";
        if(t.getIdUtilizator1() == t.getIdUtilizator2())
            erori+="Eroare id-uri egale\n";
        if (!erori.isEmpty()) {
            throw new ValidationException(erori.trim());
        }
    }
}
