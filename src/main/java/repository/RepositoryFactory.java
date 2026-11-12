package repository;

import domain.Pereche;
import domain.Prietenie;
import domain.Utilizator;
import validators.Validator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Supplier;


/**
 * Simple Factory: the only class that knows the concrete repositories.
 * The persistence type is read from config.properties.
 */
public class RepositoryFactory {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private final Properties props = new Properties();
    private final RepositoryType tip;

    /**
     * Loads the configuration.
     *
     * @throws RepositoryException if the file is missing or repository.type is invalid
     */
    public RepositoryFactory() {
        try (InputStream in = new FileInputStream(CONFIG_FILE)) {  //citire din fisierul de configurare
            props.load(in);//incarcare in props din fisierul de configurare
        } catch (IOException e) {//problema la citirea fisierului de configurare
            throw new RepositoryException("Nu pot citi fisierul de configurare " + CONFIG_FILE, e);
        }

        String text = citeste("repository.type");//preia cheia din fisierul de configurare
        try {
            tip = RepositoryType.valueOf(text);//alegem tipul din ENUM
        } catch (IllegalArgumentException e) {//eroare
            throw new RepositoryException(
                    "repository.type necunoscut: '" + text + "' (valori: MEMORY, FILE, DATABASE)", e);
        }
    }

    /**
     * Reads a required property.
     *
     * @param cheie the property name
     * @return the value
     * @throws RepositoryException if the property is missing
     */
    private String citeste(String cheie) {
        String valoare = props.getProperty(cheie);//preia valoarea din fisierul de configurare
        if (valoare == null || valoare.isBlank())//daca este null sau gol->eroare
            throw new RepositoryException("Lipseste proprietatea '" + cheie + "' din " + CONFIG_FILE);
        return valoare;
    }

    /**
     * Creates the user repository.
     *
     * @param validator the validation strategy
     * @return the user repository of the configured type
     * @throws RepositoryException if a required property is missing
     *                             or the configured type has no creator
     */
    public Repository<Integer, Utilizator> createUtilizatorRepository(Validator<Utilizator> validator) {
        Map<RepositoryType, Supplier<Repository<Integer, Utilizator>>> creatori = new HashMap<>();
        creatori.put(RepositoryType.MEMORY, () -> new InMemoryRepository<>(validator));
        creatori.put(RepositoryType.FILE, () -> new UtilizatorFileRepository(citeste("utilizatori.file"), validator));
        creatori.put(RepositoryType.DATABASE, () -> new UtilizatorDbRepository(validator,
                citeste("db.url"), citeste("db.user"), citeste("db.password")));
        Supplier<Repository<Integer, Utilizator>> creator = creatori.get(tip);
        if (creator == null) {
            throw new RepositoryException("Tip de persistenta necunoscut: " + tip);
        }
        return creator.get();
    }
    /**
     * Creates the friendship repository.
     *
     * @param validator the validation strategy
     * @return the friendship repository of the configured type
     * @throws RepositoryException if a required property is missing
     *                             or the configured type has no creator
     */
    public Repository<Pereche<Integer, Integer>, Prietenie> createPrietenieRepository(Validator<Prietenie> validator) {
        Map<RepositoryType, Supplier<Repository<Pereche<Integer,Integer>,Prietenie>>> creatori = new HashMap<>();
        creatori.put(RepositoryType.MEMORY, () -> new InMemoryRepository<>(validator));
        creatori.put(RepositoryType.FILE, () -> new PrietenieFileRepository(citeste("prietenii.file"), validator));
        creatori.put(RepositoryType.DATABASE, () -> new PrietenieDbRepository(validator,
                citeste("db.url"), citeste("db.user"), citeste("db.password")));
        Supplier<Repository<Pereche<Integer,Integer>,Prietenie>> creator = creatori.get(tip);
        if (creator == null) {
            throw new RepositoryException("Tip de persistenta necunoscut: " + tip);
        }
        return creator.get();
    }
}