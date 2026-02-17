# Book Library DDD

![](images/2.png)

A library manager built with Domain-Driven Design (DDD) and hexagonal architecture principles. It now supports both the original Swing UI and a Spring Boot REST API, with Spring Data JPA + H2 configured for the Spring runtime profile while preserving the same application/domain core.

## Architecture Overview

### Hexagonal boundaries

- **Inbound ports (driving adapters):** Swing views call application services through `application.ports.input.services` interfaces. This keeps UI logic interchangeable while preserving domain rules.
- **Outbound ports (driven adapters):** Persistence, security, and clock abstractions live under `application.ports.output`. Infrastructure implements them with JDBC repositories, password encryption, and system time utilities, enabling replacement without touching domain code.
- **Manual composition:** `com.finalproject.presentation.swing.SwingDesktopApplication` bootstraps a Swing-specific `DependencyInjector` that wires adapters into services explicitly, avoiding hidden magic and making dependencies visible for tests.

### Layer responsibilities

- **Presentation (Swing UI)** – Screens (Login, FavoriteBooks, FavoriteAuthors, UnreadBooks, WishlistNotification, etc.) gather user intent and delegate to application services without embedding business logic.
- **Application layer** – Coordinates use cases, permission checks, and transactions. Book use cases are split with CQRS (`BookCommandApplicationService` + `BookQueryApplicationService`) and work alongside `UserApplicationService` and other services to orchestrate repositories, mappers, and security ports while enforcing high-level workflows.
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
├── presentation
│   ├── spring             # Spring API starter + REST controllers (inbound adapters)
│   └── swing              # Swing starter, views, and swing bootstrap wiring
├── application            # Use cases, ports, mappers, and application DTOs
├── domain                 # Entities, value objects, domain exceptions (core model)
└── infrastructure
    ├── configuration      # Spring security/openapi/bean configuration
    ├── persistence
    │   ├── jdbc           # JDBC adapters for Swing runtime
    │   └── jpa            # JPA adapters/entities/repositories for Spring runtime
    └── security           # Shared security helpers/context
```

## Database & Infrastructure

- **H2 + JPA setup** – Spring runtime uses H2 (`jdbc:h2:mem:mylibrary`) and Spring Data JPA. Schema and seed data are loaded automatically from `src/main/resources/db/h2/schema.sql` and `src/main/resources/db/h2/data.sql`.
- **Connection management** – `DatabaseConfig` is a lightweight singleton that loads the H2 driver, provides connections, and offers a shutdown helper.
- **Transaction boundary** – `JdbcUnitOfWork` wraps JDBC operations, handling commit/rollback around supplied actions to keep application service workflows consistent.

## Running the Application

1. Install Java 17 and Maven.
2. Build the project: `mvn clean package`.
3. Start the Spring Boot API:

```bash
mvn spring-boot:run
```

4. Optional: run the original Swing client from your IDE using `com.finalproject.presentation.swing.SwingDesktopApplication`.

Swing JDBC runtime is decoupled from Spring JPA runtime. For Swing, you can point JDBC to MySQL using environment variables:
- `SWING_DB_DRIVER` (default `com.mysql.cj.jdbc.Driver`)
- `SWING_DB_URL` (default `jdbc:mysql://localhost:3306/booklibrary`)
- `SWING_DB_USER` (default `root`)
- `SWING_DB_PASSWORD` (default `password`)
If the MySQL driver is not present, Swing falls back to embedded H2 scripts for local execution.

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Use the **Authorize** button in Swagger with `Bearer <your_token>` (token from `/api/auth/login`).
- Only `/api/auth/login` is open; all other `/api/**` endpoints require JWT authentication.

### REST API quick start

- `POST /api/auth/login`
  - Body: `{ "username": "admin", "password": "123" }`
  - Returns authenticated user metadata (`userId`, `username`, `userType`) plus a JWT `token`.
- `GET /api/books/{id}`
  - Protected endpoint (JWT required) backed by a dedicated query handler (CQRS).
- `POST /api/books`, `PUT /api/books/{id}`, `DELETE /api/books/{id}`
  - Implemented via CQRS command handlers in the application layer.
  - Require `Authorization: Bearer <token>` (JWT from `/api/auth/login`).

Example create request:

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "authorName": "Martin",
    "authorSurname": "Fowler",
    "title": "Refactoring",
    "year": 2018,
    "numberOfPages": 448,
    "about": "Improving existing code",
    "coverPath": "src/main/resources/covers/Book1.jpg"
  }'
```

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
