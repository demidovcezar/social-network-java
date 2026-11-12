Toy Social Network - Laborator 5 (persistenta in baza de date PostgreSQL)

1. Ce s-a schimbat
Functionalitatile sunt aceleasi ca la laboratorul 4, dar datele se pot pastra
acum si intr-o baza de date PostgreSQL. Am implementat UtilizatorDbRepository
si PrietenieDbRepository cu JDBC. Id-ul unui utilizator nou il genereaza
stocarea, nu Service, iar la adaugare aplicatia afiseaza id-ul primit.
Am trecut proiectul pe Gradle, ca driverul PostgreSQL sa se descarce automat.

2. Configurare
- Baza de date socialnetwork, cu tabelele users si friendships (id generat cu
  GENERATED ALWAYS AS IDENTITY, cheie primara (id1, id2), CHECK (id1 < id2),
  chei straine catre users(id) cu ON DELETE CASCADE).
- Copiez config.example.properties in config.properties si pun parola reala.
  config.properties este in .gitignore, ca parola sa nu ajunga pe GitHub.
- Driverul org.postgresql:postgresql:42.7.7 este in build.gradle.kts.
- Tipul de stocare se alege doar din repository.type (DATABASE, FILE, MEMORY);
  Main si factory-ul nu se modifica.

3. ON DELETE CASCADE si stergerea explicita
Le folosesc pe amandoua. Service sterge intai prieteniile utilizatorului, apoi
utilizatorul. Asta trebuie sa ramana, pentru ca FILE si MEMORY nu au CASCADE.
In baza de date, ON DELETE CASCADE este plasa de siguranta: datele raman
corecte si daca cineva sterge un utilizator direct din pgAdmin.

4. Repository-urile JDBC
- Implementeaza Repository<ID, E>, deci Service si UI nu stiu ca lucreaza cu o
  baza de date (DIP).
- Conexiunea se obtine cu DriverManager.getConnection(url, user, password), cu
  parametrii primiti de la factory. Connection, PreparedStatement si ResultSet
  se deschid in try-with-resources, deci se inchid automat.
- executeQuery() pentru SELECT, executeUpdate() pentru INSERT, UPDATE, DELETE.
- Respecta contractul Repository: null ca argument da IllegalArgumentException,
  validarea se face inainte de save si update, update intoarce entitatea daca
  id-ul nu exista (0 randuri modificate), delete intoarce entitatea stearsa.
- La prietenii, id-ul este perechea (id1, id2), iar data se converteste cu
  Timestamp.valueOf si toLocalDateTime.

5. Id-ul generat de stocare
- DATABASE: INSERT fara id, cu RETURN_GENERATED_KEYS; citesc id-ul cu
  getGeneratedKeys() si il pun in obiect cu setId.
- FILE si MEMORY: InMemoryRepository are metoda genereazaId(), apelata in save
  cand entitatea nu are id (Template Method). Repository-urile de utilizatori
  o suprascriu cu cel mai mare id + 1.
- Service creeaza utilizatorul fara id, il salveaza si intoarce getId().
Am mutat generarea din Service pentru ca baza de date nu refoloseste id-urile
sterse, deci maximul + 1 calculat in Service putea fi diferit de id-ul real.

6. SQL injection
Toate interogarile folosesc PreparedStatement cu ?. Valorile nu se
concateneaza in SQL, deci un nume ca '); DROP TABLE users; -- se salveaza ca
text obisnuit.

7. Tratarea exceptiilor
SQLException se impacheteaza in RepositoryException, cu cauza atasata. Metoda
RepositoryException.din alege mesajul dupa e.getSQLState(): 08 = server oprit,
28P01 = parola gresita, 3D000 = baza inexistenta, 23xxx = constrangere
incalcata. Consola afiseaza doar mesajul, fara stack trace. Conexiunea se
deschide la fiecare operatie, deci aplicatia porneste si cu serverul oprit.

8. Demonstratie
1. CRUD cu DATABASE, verificat in pgAdmin, si id-ul generat.
2. Repornesc aplicatia: datele raman.
3. Schimb repository.type intre DATABASE, FILE si MEMORY.
4. Opresc serverul (services.msc): apare mesajul de eroare, fara stack trace.
5. Adaug un utilizator cu numele '); DROP TABLE users; -- : se salveaza ca text.
