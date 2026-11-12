package repository;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import domain.Entity;
import validators.ValidationException;
import validators.Validator;

/**
 * Repository that also keeps its entities in a text file, one per line.
 * The subclasses only convert a line to an entity and back (Template Method).
 */
public abstract class  AbstractFileRepository<ID,E extends Entity<ID>> extends InMemoryRepository<ID,E>{
    private  final String fileName;
    /**
     * Creates the repository and loads the file, if it exists.
     *
     * @param validator the validation strategy
     * @param fileName the path of the text file
     */
    public AbstractFileRepository(Validator<E> validator,String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }
    /**
     * Loads the file; invalid lines are reported and skipped.
     *
     * @throws RepositoryException if the file cannot be read
     */
    private void loadData() {
        try (Stream<String> flux = Files.lines(Path.of(fileName), StandardCharsets.UTF_8)) { //citim fisieru
            List<String> linii = flux.collect(Collectors.toList());//transformam in lista de stringuri
            IntStream.range(0, linii.size())//luam pe fiecare
                    .forEach(i -> proceseazaLinia(i + 1, linii.get(i)));//procesam
        } catch (NoSuchFileException e) {
            System.out.println("Fisierul " + fileName + " nu exista; repository gol.");
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citire: " + fileName, e);
        } catch (UncheckedIOException e) {
            throw new RepositoryException("Eroare la citire: " + fileName, e.getCause());
        }
    }

    /**
     * Converts one line to an entity and saves it; an invalid line is reported and skipped.
     *
     * @param numarLinie the number of the line in the file, starting from 1
     * @param linie the text of the line
     */
    private void proceseazaLinia(int numarLinie, String linie) {
        if (linie.isBlank()) {
            return;
        }
        try {
            E entitate = extractEntity(linie);
            if (super.save(entitate) != null) {
                System.err.println("Linia " + numarLinie + " ignorata: id duplicat (" + linie + ")");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException
                 | DateTimeException | ValidationException e) {
            System.err.println("Linia " + numarLinie + " ignorata: " + e.getMessage() + " (" + linie + ")");
        }
    }

    /**
     * Rewrites the whole file.
     *
     * @throws RepositoryException if the file cannot be written
     */
    private void writeToFile()
    {
        File folder = new File(fileName).getParentFile();//locatie fisier
        if (folder != null) {//daca fisierul e dat fara folder
            folder.mkdirs();
        }
        try{
            List<String> linii = StreamSupport.stream(findAll().spliterator(), false)//transformam intr-un stream de entitati
                    .map(this::createEntityAsString)//aplicam functia createEntityAsString pe fiecare entitate
                    .collect(Collectors.toList());//transformam intr-o lista de stringuri
            Files.write(Path.of(fileName), linii, StandardCharsets.UTF_8);//scriere fisier

        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere: " + fileName, e);
        }
    }

    /**
     * Converts a line of the file into an entity.
     *
     * @param line a line of the file
     * @return the entity described by the line
     */
    protected abstract E extractEntity(String line);
    /**
     * Converts an entity into a line of the file.
     *
     * @param entity the entity to convert
     * @return the line that describes the entity
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Saves the entity and, if it was saved, rewrites the file.
     *
     * @throws RepositoryException if the file cannot be written
     */
    @Override
    public E save(E entity) {
        E rezultat =super.save(entity);
        if(rezultat==null)
        {
            writeToFile();
        }
        return rezultat;
    }
    /**
     * Deletes the entity and, if it existed, rewrites the file.
     *
     * @throws RepositoryException if the file cannot be written
     */
    @Override
    public E delete(ID id) {
        E rezultat =super.delete(id);
        if(rezultat!=null)
        {
            writeToFile();
        }
        return rezultat;
    }
    /**
     * Updates the entity and, if it was updated, rewrites the file.
     *
     * @throws RepositoryException if the file cannot be written
     */
    @Override
    public E update(E entity) {
        E rezultat =super.update(entity);
        if(rezultat==null)
        {
            writeToFile();
        }
        return rezultat;
    }
}
