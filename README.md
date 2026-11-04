# Social Network (Java)

A Java console application that models a small social network: users, friendships and communities. The code is split into layers (domain, repository, service, UI). Storage can be switched between in-memory and file-based through a repository factory. Communities are found as connected components of the friendship graph.
## Features

- Add and remove users (ids are generated automatically)
- Add and remove friendships (friendships are symmetric: Ana-Dan is the same as Dan-Ana)
- Removing a user also removes all of their friendships
- Count the number of communities (connected components of the friendship graph)
- Find the most sociable community (the connected component containing the longest simple path)
- Search users by first-name prefix, by last name, by both, or with a long last name
- Data is kept in text files and survives restarts

## Architecture

The project follows a layered architecture:

| Layer | Package | Responsibility |
|---|---|---|
| UI | `UI` | Console menu (`Consola`); shows errors as messages |
| Service | `Service` | Business logic: users, friendships, communities, searches |
| Repository | `repository` | Storage (memory, text files, database skeleton) and the factory |
| Domain | `domain` | `Utilizator`, `Prietenie`, `Pereche`, search criteria |
| Validation | `validators` | `Validator<T>` and the concrete validators |

## Persistence and configuration

The storage type is chosen in `src/main/resources/config.properties`:

```properties
repository.type=FILE          # MEMORY, FILE or DATABASE
utilizatori.file=data/utilizatori.txt
prietenii.file=data/prietenii.txt
db.url=...
db.user=...
db.password=...
```

| `repository.type` | Behavior |
|---|---|
| `FILE` | Data is stored in text files and loaded at startup |
| `MEMORY` | Data lives only in memory and is lost when the application exits |
| `DATABASE` | Not implemented yet; the repositories are skeletons that throw `UnsupportedOperationException` |

A missing or unknown value (for example `FILEE`) stops the application with a clear message.
Switching the storage type never requires changes in `Service`, `UI` or `Main`.

### File formats

- `data/utilizatori.txt`: `id;firstName;lastName`
- `data/prietenii.txt`: `idUser1;idUser2;date` (date in ISO format, for example `2026-10-19T10:30`)

Files use UTF-8. If a file is missing, the application starts with an empty repository and creates the file on the first save.
An invalid line (missing field, bad number or date, failed validation, duplicate id) is skipped and reported with its line number; the rest of the file is still loaded.
`data/utilizatori_corupt.txt` is a sample file with corrupt lines.

## Design

- **Simple Factory**: `RepositoryFactory` is the only class that knows the concrete repositories; it picks one from a `Map<RepositoryType, Supplier<...>>`, so there is no `switch`.
- **Template Method**: `AbstractFileRepository` loads and saves the file; `UtilizatorFileRepository` and `PrietenieFileRepository` only convert a line to an entity and back.
- **Strategy**: `Validator<T>` is injected into the repositories. It is a functional interface with a default `andThen` method, so validation rules can be written as lambdas and composed.
- **Functional style (Java 8)**: `Repository.findAll(Predicate<E>)` with reusable criteria in static methods in `CriteriiUtilizator`, method references, `Optional`, and streams instead of loops in the service, the repositories and the UI.
- **Exceptions**: unchecked custom exceptions (`ValidationException`, `ServiceException`, `RepositoryException`) with exception chaining; they are handled in the UI.

## Requirements

- JDK 11 or newer
- The project is a plain IntelliJ IDEA project (no build tool needed)

## Running

1. Open the project in IntelliJ IDEA.
2. Run the `Main` class from the project root, so that the relative paths `data/...` and `src/main/resources/config.properties` resolve correctly.
3. Use the numbered menu.

## Project structure

```
src/
  Main.java
  UI/            Consola
  Service/       Service, ServiceException
  domain/        Entity, Utilizator, Prietenie, Pereche, CriteriiUtilizator
  repository/    Repository, InMemoryRepository, AbstractFileRepository,
                 UtilizatorFileRepository, PrietenieFileRepository,
                 UtilizatorDbRepository, PrietenieDbRepository,
                 RepositoryFactory, RepositoryType, RepositoryException
  validators/    Validator, UtilizatorValidator, PrietenieValidator, ValidationException
  main/resources/config.properties
data/            utilizatori.txt, prietenii.txt, utilizatori_corupt.txt
documentation/   generated Javadoc
readme.txt       notes for the lab (design decisions, before/after of the refactoring)
```
