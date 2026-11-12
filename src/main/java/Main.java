import Service.Service;
import UI.Consola;
import domain.Pereche;
import domain.Prietenie;
import domain.Utilizator;
import repository.Repository;
import repository.RepositoryException;
import repository.RepositoryFactory;
import validators.PrietenieValidator;
import validators.UtilizatorValidator;
import validators.ValidationException;
import validators.Validator;


/**
 * Entry point of the application.
 */
public class Main {
    /**
     * Reads the configuration, builds the validators and the repositories
     * and starts the console.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            RepositoryFactory factory = new RepositoryFactory();
            Validator<Utilizator> validatorUtilizatori = new UtilizatorValidator();
            Validator<Utilizator> maxZeceCaractere = u -> {
                if ( u.getNume().length()>10 ) {
                    throw new ValidationException("Numele Utilizatorul are mai mult de 10 litere");
                }
            };
            validatorUtilizatori=validatorUtilizatori.andThen(maxZeceCaractere);
            Validator<Prietenie> validatorPrietenii = new PrietenieValidator();
            Repository<Integer, Utilizator> repoUtilizatori = factory.createUtilizatorRepository(validatorUtilizatori);
            Repository<Pereche<Integer, Integer>, Prietenie> repoPrietenii = factory.createPrietenieRepository(validatorPrietenii);
            Service service = new Service(repoUtilizatori, repoPrietenii);
            Consola ui = new Consola(service);
            ui.run();

        } catch (RepositoryException e) {
            System.err.println("Aplicatia nu a putut porni: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cauza: " + e.getCause().getMessage());
            }
        }
    }
}