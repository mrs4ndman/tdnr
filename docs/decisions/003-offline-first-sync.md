# ADR-003: Offline-first mobile synchronization

## Status

Accepted as a product requirement for the mobile client.

## Context

The Android client should remain useful when connectivity is unavailable. The current node notes describe local changes and a queue that synchronizes with the server when the connection returns.

Offline operation introduces local persistence, retries, duplicate delivery, ordering, deletions, and concurrent edits. The first implementation should establish clear synchronization semantics without introducing multi-node replication or a general-purpose distributed data system.

## Decision

The mobile client will use a local database as its working store and an outbound change queue for server synchronization.

The server remains authoritative for shared state. Client changes must carry stable entity IDs, an operation ID for idempotency, the client device ID, a base version where applicable, and timestamps. The server must provide a way to acknowledge accepted operations and report conflicts or rejected operations.

The first conflict strategy will use optimistic concurrency with entity versions. Automatic merging and CRDTs are deferred. Operations that conflict will be retained for user-visible resolution rather than silently discarded.

The web client may remain online-first initially, but it must use the same API contract and entity version rules.

## Consequences

- The API contract must define versions, idempotent operation handling, deletion representation, and synchronization checkpoints before mobile sync is implemented.
- Local mobile storage is a client concern and is not the server's source of truth.
- The backend needs durable change metadata sufficient to answer what changed after a client checkpoint.
- Conflict resolution becomes a product workflow, not merely a transport error.
- The first task workflow can be implemented online before the mobile sync engine, as long as identifiers and versions are designed from the start.

## Alternatives considered

### Online-only mobile client

Rejected because offline use is an explicit v1 product requirement.

### Last-write-wins

Deferred because it can silently overwrite user work and does not expose enough information for deliberate conflict handling.

### CRDT-based synchronization

Deferred because the current domain and requirements do not justify the additional modeling and operational complexity.

## Next decision

Define the task entity, operation envelope, version rules, and minimal REST/JSON endpoints for create, list, update, complete, and delete.
