package domain;
import java.io.Serializable;
/**
 * Base class of all entities; holds the identifier.
 * @param <ID> the type of the identifier
 */
public class Entity<ID> implements  Serializable{
private ID id;
/**
 * Creates an entity without an id.
 */
public Entity(){};
/**
 * Creates an entity with the given id.
 * @param id the identifier
 */

public Entity(ID id){
    this.id=id;}
/**
 * Returns the identifier of the entity.
 *
 * @return the identifier, or null if it was not set
 */
public ID getId()
{
    return id;
}
/**
 * Sets the identifier.
 * @param id the new identifier
 */
public void setId(ID id){
    this.id = id;
}
}
