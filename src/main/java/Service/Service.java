package Service;

import domain.Pereche;
import domain.Utilizator;
import repository.Repository;
import domain.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Business logic: users, friendships and communities.
 */
public class Service {
    private final Repository<Integer, Utilizator> repoUtilizatori;
    private final Repository<Pereche<Integer, Integer>, Prietenie> repoPrietenii;
    /**
     * Creates the service over the two repositories.
     *
     * @param repoUtilizatori the repository of users
     * @param repoPrietenii the repository of friendships
     */
    public Service(Repository<Integer, Utilizator> repoUtilizatori,Repository<Pereche<Integer, Integer>, Prietenie> repoPrietenii) {
        this.repoUtilizatori = repoUtilizatori;
        this.repoPrietenii = repoPrietenii;
    }
    /**
     * Generates the id of a new user.
     *
     * @return the next free user id (maximum id + 1)
     */
    private int genereazaId() {
        return StreamSupport.stream(repoUtilizatori.findAll().spliterator(), false)
                .map(Utilizator::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
    /**
     * Finds a user by id.
     *
     * @param id the user id
     * @return the user with the given id
     * @throws ServiceException if the user does not exist
     */
    private Utilizator gasesteUtilizator(int id) {
        return Optional.ofNullable(repoUtilizatori.findOne(id))
                .orElseThrow(() -> new ServiceException("Utilizatorul " + id + " nu exista"));
    }

    /**
     * Adds a user with a generated id.
     *
     * @param Nume the last name
     * @param Prenume the first name
     * @throws validators.ValidationException if a name is blank
     * @throws ServiceException if the user could not be saved
     * @throws repository.RepositoryException if the data cannot be persisted
     */
    public void adaugaUtilizator(String Nume,String Prenume)
    {
        Utilizator nou= new Utilizator(genereazaId(),Nume,Prenume);
        if(repoUtilizatori.save(nou)!=null)
        {
            throw new ServiceException("Eroare in Service la salvare utilizator");
        }


    }
    /**
     * Adds a friendship between two existing users.
     *
     * @param a the id of one user
     * @param b the id of the other user
     * @throws ServiceException if a user does not exist or the friendship already exists
     * @throws validators.ValidationException if a equals b
     * @throws repository.RepositoryException if the data cannot be persisted
     */
    public void adaugaPrietenie(int a,int b)
    {
        Prietenie noua =new Prietenie(a,b);
        gasesteUtilizator(a);
        gasesteUtilizator(b);
        if(repoPrietenii.save(noua)!=null)
        {
            throw  new ServiceException("Prietenia exista deja");
        }
    }
    /**
     * Removes a friendship.
     *
     * @param a the id of one user
     * @param b the id of the other user
     * @throws ServiceException if the friendship does not exist
     * @throws repository.RepositoryException if the data cannot be persisted
     */
    public void stergePrietenie(int a,int b)
    {
        Prietenie noua= new Prietenie(a,b);
        if(repoPrietenii.delete(noua.getId())==null) {
            throw new ServiceException("Prietenia nu exista");
        }

    }
    /**
     * Removes a user together with all of his friendships.
     *
     * @param id the user id
     * @throws ServiceException if the user does not exist
     * @throws repository.RepositoryException if the data cannot be persisted
     */
    public void stergeUtilizator(int id) {
        gasesteUtilizator(id);
        List<Prietenie> deSters = StreamSupport.stream(repoPrietenii.findAll().spliterator(), false)
                .filter(p -> p.getIdUtilizator1() == id || p.getIdUtilizator2() == id)
                .collect(Collectors.toList());
        deSters.forEach(p -> repoPrietenii.delete(p.getId()));
        repoUtilizatori.delete(id);

    }
    /**
     * Returns all users.
     *
     * @return all users
     */
    public Iterable<Utilizator> getUtilizatori() {
        return repoUtilizatori.findAll();
    }

    /**
     * Returns all friendships.
     *
     * @return all friendships
     */
    public Iterable<Prietenie> getPrietenii() {
        return repoPrietenii.findAll();
    }

    /**
     * Finds the users whose first name starts with a prefix.
     *
     * @param prefix the prefix of the first name
     * @return the users whose first name starts with the prefix
     */
    public List<Utilizator> cautaDupaPrenume(String prefix) {
        return repoUtilizatori.findAll(CriteriiUtilizator.prenumeleIncepeCu(prefix));
    }

    /**
     * Finds the users with a given last name.
     *
     * @param nume the last name
     * @return the users with the given last name
     */
    public List<Utilizator> cautaDupaNume(String nume) {
        return repoUtilizatori.findAll(CriteriiUtilizator.areNumele(nume));
    }

    /**
     * Finds the users whose first name starts with a prefix and who have a given last name.
     *
     * @param prefix the prefix of the first name
     * @param nume the last name
     * @return the users that satisfy both criteria
     */
    public List<Utilizator> cautaDupaPrenumeSiNume(String prefix, String nume) {
        return repoUtilizatori.findAll(
                CriteriiUtilizator.prenumeleIncepeCu(prefix).and(CriteriiUtilizator.areNumele(nume)));
    }

    /**
     * Finds the users whose last name is not short.
     *
     * @return the users with a long last name
     */
    public List<Utilizator> cautaCuNumeLung() {
        return repoUtilizatori.findAll(Predicate.not(CriteriiUtilizator::areNumeScurt));
    }

    /**
     * Returns the ids of the friends of a user.
     *
     * @param id the user id
     * @return the ids of all the users that are friends with the given user
     */
    private List<Integer> prieteniiLui(int id) {
        return StreamSupport.stream(repoPrietenii.findAll().spliterator(), false)
                .filter(p -> p.getIdUtilizator1() == id || p.getIdUtilizator2() == id)
                .map(p -> p.getIdUtilizator1() == id ? p.getIdUtilizator2() : p.getIdUtilizator1())
                .collect(Collectors.toList());
    }

    /**
     * Builds the friendship graph as an adjacency list.
     *
     * @return for each user id, the list of his friends' ids
     */
    private Map<Integer, List<Integer>> construiesteAdiacenta() {
        return StreamSupport.stream(repoUtilizatori.findAll().spliterator(), false)
                .collect(Collectors.toMap(Utilizator::getId, u -> prieteniiLui(u.getId())));
    }

    /**
     * Marks as visited all users reachable from the given user (DFS).
     *
     * @param id the starting user id
     * @param adiacenta the adjacency list of the friendship graph
     * @param vizitati the ids already visited; it is updated by the search
     */
    private void dfs(int id, Map<Integer, List<Integer>> adiacenta, Set<Integer> vizitati) {
        vizitati.add(id);
        adiacenta.get(id).stream()
                .filter(vecin -> !vizitati.contains(vecin))
                .forEach(vecin -> dfs(vecin, adiacenta, vizitati));
    }

    /**
     * Finds the community of a user.
     *
     * @param id the user id
     * @param adiacenta the adjacency list of the friendship graph
     * @return the ids of all the users reachable from the given user, in the order they were found
     */
    private Set<Integer> componentaDe(int id, Map<Integer, List<Integer>> adiacenta) {
        Set<Integer> vizitati = new LinkedHashSet<>();
        dfs(id, adiacenta, vizitati);
        return vizitati;
    }

    /**
     * Finds all the communities.
     *
     * @param adiacenta the adjacency list of the friendship graph
     * @return the communities, each one as the set of its users' ids
     */
    private List<Set<Integer>> comunitati(Map<Integer, List<Integer>> adiacenta) {
        return adiacenta.keySet().stream()
                .map(id -> componentaDe(id, adiacenta))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Counts the communities.
     *
     * @return the number of communities (connected components of the friendship graph)
     */
    public int numarComunitati() {
        return comunitati(construiesteAdiacenta()).size();
    }

    /**
     * Finds the longest simple path starting at a user (DFS with backtracking).
     * The loop was replaced by a stream; the shared set peDrum is restored
     * before each call returns, so the recursion stays correct.
     *
     * @param id the starting user id
     * @param adiacenta the adjacency list of the friendship graph
     * @param peDrum the ids on the current path
     * @return the length of the longest simple path starting at the given user
     */
    private int drumMaxim(int id, Map<Integer, List<Integer>> adiacenta, Set<Integer> peDrum) {
        peDrum.add(id);
        int max = adiacenta.get(id).stream()
                .filter(vecin -> !peDrum.contains(vecin))
                .mapToInt(vecin -> 1 + drumMaxim(vecin, adiacenta, peDrum))
                .max()
                .orElse(0);
        peDrum.remove(id);
        return max;
    }

    /**
     * Finds the longest simple path inside a community.
     *
     * @param comunitate the ids of the users of the community
     * @param adiacenta the adjacency list of the friendship graph
     * @return the length of the longest simple path that starts at one of the users
     */
    private int lungimeDrumMaxim(Set<Integer> comunitate, Map<Integer, List<Integer>> adiacenta) {
        return comunitate.stream()
                .mapToInt(nod -> drumMaxim(nod, adiacenta, new HashSet<>()))
                .max()
                .orElse(0);
    }

    /**
     * Finds the most sociable community.
     *
     * @return the users of the community that contains the longest simple path
     */
    public List<Utilizator> comunitateSociabila() {
        Map<Integer, List<Integer>> adiacenta = construiesteAdiacenta();
        return comunitati(adiacenta).stream()
                .max(Comparator.comparingInt(c -> lungimeDrumMaxim(c, adiacenta)))
                .orElse(Collections.emptySet())
                .stream()
                .map(repoUtilizatori::findOne)
                .collect(Collectors.toList());
    }
}
