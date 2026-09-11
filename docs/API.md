# TDNR API v1

This document defines the smallest shared contract for the web client, Java backend prototype, and future Flutter client.

## Conventions

- Base path: `/api/v1`
- JSON is encoded as UTF-8.
- Timestamps use ISO 8601 UTC, for example `2026-09-13T12:30:00Z`.
- Entity IDs and operation IDs are UUIDs.
- Every mutable entity has a monotonically increasing `version`.
- A successful mutation increments `version` and returns the complete current representation.
- A stale mutation returns `409 Conflict` with the current entity and a machine-readable error code.

## Task representation

```json
{
  "id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d1",
  "title": "Read the architecture notes",
  "description": null,
  "status": "TODO",
  "owner_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d2",
  "collection_id": null,
  "created_at": "2026-09-13T12:30:00Z",
  "updated_at": "2026-09-13T12:30:00Z",
  "created_by_device_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d3",
  "updated_by_device_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d3",
  "version": 1,
  "deleted_at": null
}
```

The initial status values are `TODO`, `ONGOING`, `DONE`, `BACKBURNER`, `FUTURE`, and `IDEA`.

A deleted task remains represented by a tombstone with `deleted_at` set until the server's retention policy allows it to be removed from synchronization history.

## Online task endpoints

### `GET /tasks`

Returns visible, non-deleted tasks. Initial query parameters:

- `status`: filter by one status.
- `collection_id`: filter by collection.
- `updated_after`: return tasks changed after a timestamp.

Response:

```json
{
  "items": [],
  "next_cursor": null
}
```

### `POST /tasks`

Creates a task. The request may include `operation_id` and `device_id` so an offline retry is idempotent.

```json
{
  "operation_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d4",
  "device_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d3",
  "title": "Read the architecture notes",
  "description": null,
  "status": "TODO",
  "collection_id": null
}
```

Returns `201 Created` and the created task. Repeating the same `operation_id` returns the original result without creating a duplicate.

### `GET /tasks/{task_id}`

Returns one task, including a tombstone when the caller is allowed to observe its deletion.

### `PATCH /tasks/{task_id}`

Updates selected mutable fields. The request must identify the version it was based on using `If-Match: "{version}"` or an equivalent `base_version` field for queued operations.

Returns `200 OK` with the updated task, or `409 Conflict` when the base version is stale.

### `POST /tasks/{task_id}/complete`

Completes a task using the same version and idempotency rules as `PATCH`.

### `DELETE /tasks/{task_id}`

Creates a tombstone using the same version and idempotency rules. Deletion is not immediately destructive because offline clients need to receive it during synchronization.

## Synchronization endpoints

These endpoints are reserved for the offline-first mobile client and may initially be implemented as a narrow prototype.

### `POST /sync/push`

Accepts an ordered batch of queued operations. Each operation includes:

```json
{
  "operation_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d4",
  "device_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d3",
  "entity_type": "task",
  "entity_id": "018f2d8e-3a7a-7b0a-9f32-8f5ad4c6b2d1",
  "operation": "UPDATE",
  "base_version": 1,
  "occurred_at": "2026-09-13T12:30:00Z",
  "payload": {}
}
```

The response reports one result per operation: `APPLIED`, `ALREADY_APPLIED`, `CONFLICT`, or `REJECTED`.

### `GET /sync/pull?cursor={cursor}`

Returns changes after the caller's cursor, including tombstones. The response includes a new cursor that the client stores only after successfully applying the batch locally.

```json
{
  "changes": [],
  "next_cursor": "opaque-server-cursor"
}
```

The cursor is opaque to clients. The server must retain enough change history for registered clients to recover from temporary disconnection, or return a resynchronization-required error when the cursor is too old.

## Errors

Errors use a stable code and human-readable message:

```json
{
  "error": {
    "code": "VERSION_CONFLICT",
    "message": "The task changed after the client read it.",
    "entity": {}
  }
}
```

Initial error codes include `VALIDATION_FAILED`, `NOT_FOUND`, `VERSION_CONFLICT`, `DUPLICATE_OPERATION`, and `RESYNC_REQUIRED`.

## Deliberately deferred

- Authentication and authorization details.
- Pagination beyond the initial cursor shape.
- Tags, collections, recurrence, reminders, and parent tasks in endpoint payloads.
- Conflict-merge rules beyond surfacing the current server entity.
- Message brokers and multi-node replication.
