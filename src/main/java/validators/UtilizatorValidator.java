package validators;

import domain.Utilizator;

/**
 * Validates users: first and last name must not be blank.
 */
public class UtilizatorValidator  implements Validator<Utilizator> {
   /**
    * Checks that the user has a first and a last name.
    *
    * @param t the user to validate
    * @throws ValidationException if a name is null or blank
    */
    @Override
   public void validate(Utilizator t) {
        String erori = "";
        if (t.getNume() == null || t.getNume().isBlank()) {
            erori += "Numele nu poate fi vid\n";
        }
        if (t.getPrenume() == null || t.getPrenume().isBlank()) {
            erori += "Prenumele nu poate fi vid\n";
        }
        if (!erori.isEmpty()) {
            throw new ValidationException(erori.trim());
        }
    }
}

