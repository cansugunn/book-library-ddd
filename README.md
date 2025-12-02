

README.md
Nuevo
+90
-0

# Book Library DDD

A desktop library manager built with Domain-Driven Design (DDD) and hexagonal architecture principles. The Swing UI authenticates users, manages favorites and wishlists, and lets administrators curate the catalog while persisting state to MySQL via JDBC.

## Architecture Overview

### Hexagonal boundaries

- **Inbound ports (driving adapters):** Swing views call application services through `application.ports.input.services` interfaces. This keeps UI logic interchangeable while preserving domain rules.
- **Outbound ports (driven adapters):** Persistence, security, and clock abstractions live under `application.ports.output`. Infrastructure implements them with JDBC repositories, password encryption, and system time utilities, enabling replacement without touching domain code.
- **Manual composition:** `com.finalproject.presentation.Main` bootstraps a `DependencyInjector` that wires adapters into services explicitly, avoiding hidden magic and making dependencies visible for tests.

### Layer responsibilities

- **Presentation (Swing UI)** – Screens (Login, FavoriteBooks, FavoriteAuthors, UnreadBooks, WishlistNotification, etc.) gather user intent and delegate to application services without embedding business logic.
- **Application layer** – Coordinates use cases, permission checks, and transactions. Services such as `BookApplicationService` and `UserApplicationService` orchestrate repositories, mappers, and security ports while enforcing high-level workflows.
- **Domain layer** – Aggregates like `Book`, `Author`, `User`, and `UserBookState` encapsulate business rules. Validation methods (`validate()`) protect invariants such as rating ranges, sensible publication years, release date ordering, and mandatory author names. Violations raise domain exceptions to keep rule enforcement close to the model.
- **Infrastructure layer** – JDBC repositories implement persistence ports; `JdbcUnitOfWork` scopes transactional work; `CypherPasswordEncryptor` fulfills the password hashing/verification port; `CurrentUserHolder` (thread-local) implements the security context port; `DatabaseConfig` centralizes connection lifecycle.

## Security & Thread-local session handling

- **Password hashing** – The `PasswordEncryptor` output port abstracts credential hashing/verification. `CypherPasswordEncryptor` encrypts and matches passwords before persistence or authentication, isolating crypto details from the UI and application layers.
- **Thread-local user context** – Authentication populates a thread-local `CurrentUser` holder. Application services read this context to authorize admin-only actions (create/update/delete books, orphan author cleanup) without passing session state through every method. UI classes only trigger login; they never cache credentials or user objects directly.
- **Permission checks** – Services guard mutations with role checks (reader vs. admin) and emit domain errors when callers lack rights, keeping enforcement centralized.

## Domain business rules (DDD focus)

- **Aggregate invariants** – `Book.validate()` constrains publication year ranges and required fields; `UserBookState.validate()` bounds ratings, restricts release dates to logical windows, and ensures status transitions remain consistent.
- **Consistency between aggregates** – Book workflows ensure authors are created on demand and removed when no books reference them. User-book state updates keep comments, ratings, and statuses aligned with the owning `User` aggregate and the target `Book`.
- **Explicit ubiquitous language** – DTOs, entities, and services share domain vocabulary (wishlist, favorites, unread list, read status) to keep intent clear across layers.
- **Error surfacing** – Domain exceptions bubble up to the UI, allowing screens to show descriptive validation or permission messages without duplicating rules.

## Key Features

- **User authentication** with thread-local session storage for downstream authorization.
- **Role-based catalog administration** for creating, updating, and deleting books with transactional author maintenance.
- **Reading state tracking** (read/unread/wishlist), ratings, comments, and optional release dates enforced by domain validation.
- **Favorites and shortcuts** surfaced through dedicated UI screens for quick navigation.
- **Secure credential handling** via the password encryptor port and infrastructure implementation.
- **Transactional persistence** coordinating repositories inside `JdbcUnitOfWork` to keep cross-aggregate updates atomic.

## Project Structure

```
src/main/java/com/finalproject
├── presentation            # Swing entry point and views (inbound adapters)
├── application             # DTOs, mappers, service interfaces/implementations (use cases)
├── domain                  # Entities, value objects, domain exceptions (core model)
└── infrastructure          # JDBC repositories, security, database setup (driven adapters)
```

## Database & Infrastructure

- **MySQL setup** – `docker-compose.yaml` starts a local MySQL instance (`mylibrary` DB, `root/root` credentials). Initialization scripts create schemas, tables, and seed data (authors, books, users, and sample user-book states).
- **Connection management** – `DatabaseConfig` is a lightweight singleton that loads the MySQL driver, provides connections, and offers a shutdown helper.
- **Transaction boundary** – `JdbcUnitOfWork` wraps JDBC operations, handling commit/rollback around supplied actions to keep application service workflows consistent.

To start MySQL with seed data:

```bash
cd src/main/java/com/finalproject/infrastructure/database
docker-compose up -d
```

## Running the Application

1. Install Java 17 and Maven.
2. Build the project: `mvn clean package`.
3. Run the Swing client (ensure MySQL is up):

```bash
java -cp target/se2232-1.0-SNAPSHOT.jar com.finalproject.presentation.Main
```

Alternatively, open the project in your IDE and run `com.finalproject.presentation.Main`.

## Development Standards & Practices

- **DDD building blocks** – Entities and value objects enforce invariants through `validate()` methods and domain-specific exception types.
- **Ports & adapters** – Interfaces in `application.ports.input` and `application.ports.output` decouple use cases from transport, storage, and security implementations.
- **Explicit mapping** – Mapper classes translate between DTOs and domain objects to keep UI and persistence concerns away from domain models.
- **Manual dependency injection** – `DependencyInjector` wires concrete infrastructure implementations into application services without a container, keeping wiring explicit and testable.
- **Security separation** – Authentication state is held in thread-local context objects, and password handling goes through the `PasswordEncryptor` abstraction to avoid leaking crypto details.
- **Error handling** – User-facing flows catch `DomainException` instances to surface validation or permission errors in the UI.
- **Java standards** – Uses Java 17, conventional package naming, builder patterns for domain objects, and immutable IDs/value objects to minimize shared mutable state.

## Resources

- **Report** – See `Report.pdf` for the original project write-up.
- **Initial data** – Cover images referenced in seed data live under `src/main/resources/covers/`.
