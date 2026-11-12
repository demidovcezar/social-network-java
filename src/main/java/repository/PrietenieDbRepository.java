package repository;

import domain.Pereche;
import domain.Prietenie;
import validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Database repository for friendships (PostgreSQL, table friendships), implemented with JDBC.
 * The id of a friendship is the pair (id1, id2), with id1 &lt; id2.
 */
public class PrietenieDbRepository implements Repository<Pereche<Integer, Integer>, Prietenie> {
    private final Validator<Prietenie> validator;
    private final String url;
    private final String user;
    private final String password;

    /**
     * Creates the repository with the connection parameters.
     *
     * @param validator the validation strategy
     * @param url the database url
     * @param user the database user
     * @param password the database password
     */
    public PrietenieDbRepository(Validator<Prietenie> validator, String url, String user, String password) {
        this.validator = validator;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Finds a friendship by its pair of user ids.
     *
     * @param id the pair (id1, id2)
     * @return the friendship, or null if it does not exist
     * @throws IllegalArgumentException if id is null
     * @throws RepositoryException if the database cannot be accessed
     */
    @Override
    public Prietenie findOne(Pereche<Integer, Integer> id) {
        if(id==null)throw new IllegalArgumentException("Id-ul nu poate fi null");
        try(
                Connection c= DriverManager.getConnection(url,user,password);
                PreparedStatement ps=c.prepareStatement("SELECT * FROM  friendships WHERE  id1=? AND id2=?"))
        {
            ps.setInt(1,id.getE1());
            ps.setInt(2,id.getE2());
            try(ResultSet rs=ps.executeQuery())
            {
                if(rs.next())
                {
                    LocalDateTime data = rs.getTimestamp("friends_from").toLocalDateTime();
                    Prietenie nou=new Prietenie(rs.getInt("id1"),rs.getInt("id2"),data);
                    return nou;
                }
                return null;
            }

        }
        catch (SQLException e)
        {
            throw RepositoryException.din("Eroare la cautarea prieteniei", e);
        }

    }

    /**
     * Returns all friendships.
     *
     * @return all friendships from the database
     * @throws RepositoryException if the database cannot be accessed
     */
    @Override
    public Iterable<Prietenie> findAll() {
        List<Prietenie> prietenii=new ArrayList<>();
        try(
                Connection c = DriverManager.getConnection(url,user,password);
                PreparedStatement ps=c.prepareStatement("SELECT * FROM friendships");
                ResultSet rs=ps.executeQuery())
        {
            while(rs.next())
            {
                LocalDateTime data = rs.getTimestamp("friends_from").toLocalDateTime();
                Prietenie nou=new Prietenie(rs.getInt("id1"),rs.getInt("id2"),data);
                prietenii.add(nou);

            }
        }
        catch(SQLException e)
        {
            throw RepositoryException.din("Nu s-au putut citi prieteniile", e);
        }
        return prietenii;
    }

    /**
     * Saves a friendship.
     *
     * @param entity the friendship to save
     * @return null if the friendship was saved, or the entity if it already exists
     * @throws IllegalArgumentException if entity is null
     * @throws validators.ValidationException if the friendship is not valid
     * @throws RepositoryException if the database cannot be accessed or a user does not exist
     */
    @Override
    public Prietenie save(Prietenie entity)
    {
        if(entity==null)throw new IllegalArgumentException("Prietenia nu poate fi null");
        validator.validate(entity);
        if (findOne(entity.getId()) != null) return entity;
        String sql = "INSERT INTO friendships(id1,id2,friends_from) VALUES(?,?,?)";
        try (
                Connection c=DriverManager.getConnection(url,user,password);
                PreparedStatement ps= c.prepareStatement(sql);
        )
        {
            ps.setInt(1,entity.getIdUtilizator1());
            ps.setInt(2,entity.getIdUtilizator2());
            LocalDateTime data =entity.getDate();
            ps.setTimestamp(3,Timestamp.valueOf(data));
            ps.executeUpdate();
        }
        catch(SQLException e)
        {
            throw RepositoryException.din("Eroare la salvarea prieteniei", e);
        }
        return null;
    }

    /**
     * Deletes a friendship.
     *
     * @param id the pair (id1, id2)
     * @return the deleted friendship, or null if it does not exist
     * @throws IllegalArgumentException if id is null
     * @throws RepositoryException if the database cannot be accessed
     */
    @Override
    public Prietenie delete(Pereche<Integer, Integer> id) {
        Prietenie gasit=findOne(id);
        if(gasit==null)return null;
        String sql="DELETE FROM friendships WHERE id1 = ? and id2=?";
        try(
                Connection c=DriverManager.getConnection(url,user,password);
                PreparedStatement ps= c.prepareStatement(sql);
        )
        {
            ps.setInt(1,id.getE1());
            ps.setInt(2,id.getE2());
            ps.executeUpdate();
            return  gasit;

        }
        catch (SQLException e)
        {
            throw RepositoryException.din("Eroare la stergerea prieteniei", e);
        }
    }

    /**
     * Updates the date of a friendship.
     *
     * @param entity the friendship with the new date
     * @return null if the friendship was updated, or the entity if it does not exist
     * @throws IllegalArgumentException if entity is null
     * @throws validators.ValidationException if the friendship is not valid
     * @throws RepositoryException if the database cannot be accessed
     */
    @Override
    public Prietenie update(Prietenie entity) {
        if(entity==null)
        {
            throw new IllegalArgumentException("Prietenia nu poate fi null");
        }
        validator.validate(entity);
        String sql="UPDATE friendships SET friends_from=? WHERE id1 =? and id2=?";
        try(
                Connection c=DriverManager.getConnection(url,user,password);
                PreparedStatement ps= c.prepareStatement(sql);
        )
        {
            ps.setTimestamp(1,Timestamp.valueOf(entity.getDate()));
            ps.setInt(2,entity.getIdUtilizator1());
            ps.setInt(3,entity.getIdUtilizator2());
            int randuri =ps.executeUpdate();
            if(randuri ==0)return entity;
            return null;
        }
        catch (SQLException e)
        {
            throw RepositoryException.din("Eroare la actualizarea prieteniei", e);
        }
    }
}