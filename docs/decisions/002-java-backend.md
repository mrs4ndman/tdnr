# ADR-002: Java 21 backend without Spring Boot for the first prototype

## Status

Accepted for the first backend prototype.

## Context

The project owner prefers Java as a possible backend language but finds Spring Boot potentially too heavy for a learning-focused, self-hosted application. TDNR needs an HTTP API, domain logic, persistence, authentication, and eventually synchronization support, but it does not yet need a large framework ecosystem.

Java 21 is available in the development environment. Maven is not installed,
and the available system Gradle version is old, so the backend uses a pinned
Gradle 8.10.2 wrapper. JUnit 5 is used directly for domain and repository tests
without a Spring test context.

## Decision

Prototype the backend in Java 21 without Spring Boot. Start with a small explicit HTTP boundary using JDK facilities, keeping transport, domain, and persistence code separated behind narrow interfaces.

The first prototype may use the JDK HTTP server and standard-library types. External libraries can be introduced when they solve a demonstrated problem, such as PostgreSQL access, migrations, JSON serialization, or authentication.

Gradle 8.10.2 is the build tool for the prototype. The wrapper provides
reproducible dependency resolution and test execution independently of the
system Gradle installation.

## Consequences

- The project stays close to the language and makes the architecture visible.
- There is less automatic wiring, validation, and convention than Spring Boot provides.
- HTTP routing, JSON handling, error responses, configuration, and lifecycle management must be designed explicitly.
- A framework can be adopted later if the application outgrows the prototype, provided domain code is not coupled to framework APIs.
- The first validation can use `javac` directly while build-tool support is settled.
- Plain JUnit tests keep domain learning fast and do not require Spring Boot's
	application context.

## Alternatives considered

### Spring Boot

Deferred because its conventions and dependency surface are disproportionate before the domain and API are stable.

### Go or Rust

Not selected for this prototype because Java is the current learning and implementation preference. They remain viable alternatives if Java's explicit infrastructure becomes the dominant cost.

## Review trigger

Reconsider this decision when authentication, persistence, migrations, test execution, or API surface require more infrastructure than the small explicit runtime can reasonably provide.
