package repository;
import domain.Utilizator;
import validators.Validator;


/**
 * Text file repository for users; line format: id;firstName;lastName.
 */
public class UtilizatorFileRepository  extends AbstractFileRepository<Integer,Utilizator>{

    /**
     * Creates the repository and loads the users from the file.
     *
     * @param fileName the path of the text file
     * @param validator the validation strategy
     */
    public UtilizatorFileRepository(String fileName, Validator<Utilizator> validator){
        super(validator,fileName);
    }
    /**
     * Converts a line into a user.
     *
     * @param line a line with the format id;firstName;lastName
     * @return the user
     * @throws NumberFormatException if the id is not a number
     * @throws ArrayIndexOutOfBoundsException if a field is missing
     */
    @Override
    protected Utilizator extractEntity(String line)
    {
        String imparte[]= line.split(";",-1);
        int id =Integer.parseInt(imparte[0]);
        String nume=imparte[2];
        String prenume=imparte[1];
        return new Utilizator(id,nume,prenume);
    }
    /**
     * Converts a user into a line.
     *
     * @param entity the user
     * @return the line id;firstName;lastName
     */
    @Override
    protected String createEntityAsString(Utilizator entity)
    {
        String concatenare=entity.getId()+";"+entity.getPrenume()+";"+entity.getNume();
        return concatenare;
    }



}
