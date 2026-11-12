package repository;
import domain.Pereche;
import domain.Prietenie;
import validators.Validator;

import java.time.LocalDateTime;

/**
 * Text file repository for friendships; line format: id1;id2;date (ISO).
 */
public class PrietenieFileRepository  extends AbstractFileRepository<Pereche<Integer,Integer>, Prietenie>{
    /**
     * Creates the repository and loads the friendships from the file.
     *
     * @param fileName the path of the text file
     * @param validator the validation strategy
     */
    public PrietenieFileRepository(String fileName, Validator<Prietenie> validator)
    {
        super(validator,fileName);
    }
    /**
     * Converts a line into a friendship.
     *
     * @param line a line with the format id1;id2;date
     * @return the friendship
     * @throws NumberFormatException if an id is not a number
     * @throws java.time.DateTimeException if the date is not valid
     */
    @Override
    protected Prietenie extractEntity(String line)
    {
        String imparte[]= line.split(";",-1);
        int id_1=Integer.parseInt(imparte[0]);
        int id_2 = Integer.parseInt(imparte[1]);
        LocalDateTime data=LocalDateTime.parse(imparte[2]);
        return new Prietenie(id_1,id_2,data);
    }
    /**
     * Converts a friendship into a line.
     *
     * @param entity the friendship
     * @return the line id1;id2;date
     */
    @Override
    protected String createEntityAsString(Prietenie entity)
    {
        String concatenare=entity.getIdUtilizator1()+";"+entity.getIdUtilizator2()+";"+entity.getDate();
        return concatenare;
    }



}
