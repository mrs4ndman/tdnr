package tndr.backend.task;

import java.time.Instant;
import java.util.UUID;

public class Task {

    private UUID id;
    private String title;
    private TaskStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant dueAt;
    private long version;

    // Task creation?
    public Task(UUID id, String title, TaskStatus status, Instant createdAt, Instant updatedAt, Instant dueAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dueAt = dueAt;
        this.version = 1;
    }

    // Task hydration from database
    public Task(UUID id, String title, TaskStatus status, Instant createdAt, Instant updatedAt, Instant dueAt, long version) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dueAt = dueAt;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public long getVersion() {
        return version;
    }

}
