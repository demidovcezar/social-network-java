package domain;

import java.util.Objects;

/**
 * A user of the social network.
 */
public class Utilizator extends Entity<Integer> implements Comparable<Utilizator>{
    private String nume;
    private String prenume;
    /**
     * Creates a user.
     * @param id the user id
     * @param nume the last name
     * @param prenume the first name
     */
    public Utilizator(int id,String nume,String prenume)
    {
        super(id);
        this.nume=nume;
        this.prenume=prenume;

    }
    /**
     * Sets the last name.
     *
     * @param Nume the new last name
     */
    public void setNume(String Nume)
    {
        this.nume=Nume;
    }
    /**
     * Sets the first name.
     *
     * @param Prenume the new first name
     */
    public void setPrenume(String Prenume)
    {
        this.prenume=Prenume;
    }
    /**
     * Returns the last name.
     *
     * @return the last name
     */
    public String getNume()
    {
        return nume;

    }
    /**
     * Returns the first name.
     *
     * @return the first name
     */
    public String getPrenume()
    {
        return prenume;
    }
    @Override
    public String toString()
    {
        return nume+" "+prenume;
    }

    public int compareTo(Utilizator s)
    {
        int r= nume.compareTo(s.nume);
        if(r!=0)
        {
            return r;
        }
        return prenume.compareTo(s.prenume);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return Objects.equals(super.getId(),((Utilizator) o).getId());

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getId());
    }


}
