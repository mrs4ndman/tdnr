# ADR-001: Central authoritative backend for v1

## Status

Accepted for the first implementation slice.

## Context

TDNR is intended to support a self-hosted web client and a future Android client. The current design notes also consider storing JSON files on multiple nodes and replicating changes through message queues.

That distributed design would require decisions about conflict resolution, message delivery, ordering, retries, deleted records, schema migrations, authentication between nodes, and recovery. None of those requirements is currently established for the first usable version.

The domain already has relational candidates such as tasks, tags, collections, users, devices, and changes. Clients may eventually need offline work and synchronization, but the exact offline contract is not yet defined.

## Decision

For v1, TDNR will use:

- One backend service as the system boundary.
- One authoritative relational database on the server.
- A versioned REST/JSON API shared by web and mobile clients.
- Stable UUIDs and server-managed creation/update metadata on persisted entities.
- A change-log abstraction that can support synchronization later, without making the log event sourcing.
- Client-side local persistence and queued synchronization only when offline mobile behavior is explicitly implemented.

The backend language and framework remain open. They will be chosen after the first domain model and API contract are sufficiently clear.

The first domain implementation now uses Java 21 without Spring Boot. Plain
JUnit 5 tests exercise the domain and in-memory repository directly; a Spring
test context is not required for these units.

## Consequences

- The first implementation can focus on domain behavior, API correctness, migrations, authentication, and tests.
- PostgreSQL is the leading database candidate because the current domain is relational and may need transactions, constraints, indexes, and optimistic concurrency.
- Multi-node replication, message brokers, and server-side JSON files are deferred rather than designed into v1.
- Future synchronization should be able to use entity versions, stable IDs, timestamps, device identity, and the change log.
- Offline Android support may require a local database and an explicit conflict policy; it is not assumed to exist merely because the API is shared.
- A future distributed deployment may require a new ADR covering conflict resolution, delivery guarantees, and data ownership.

## Alternatives considered

### Distributed JSON files with message queues

Deferred because it adds substantial operational and consistency complexity before a concrete requirement exists.

### Client-owned JSON as the primary persistence model

Rejected for v1 because relational constraints, transactions, querying, migrations, and recovery are clearer with a server database. JSON export can be added later without making JSON the source of truth.

### Event sourcing from the start

Rejected because a change log is useful for synchronization and auditability without requiring every read model and correction to be reconstructed from events.

## Next decision

Complete the repository contract and its in-memory behavior, especially
replacement of an existing task by UUID. Then define the application service
that coordinates task creation and repository persistence before introducing
HTTP or PostgreSQL.
