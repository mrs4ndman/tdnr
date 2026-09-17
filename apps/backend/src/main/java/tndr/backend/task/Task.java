package tndr.backend.task;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Task {

    private final UUID id;
    private String title;
    private TaskStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant completedAt;
    private Instant deletedAt;
    private Instant dueAt;
    private long version;

    // Task creation?
    public Task(UUID id, String title, TaskStatus status, Instant createdAt, Instant updatedAt, Instant dueAt) {
        this.id = Objects.requireNonNull(id, "Task id cannot be null");

        this.title = Objects.requireNonNull(title, "Task title cannot be null");
        this.title = normalizeTitle(this.title);
        if (this.title.isBlank() || this.title.isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be blank or empty");
        }

        this.status = Objects.requireNonNull(status, "Task status cannot be null");

        this.createdAt = Objects.requireNonNull(createdAt, "Task created-at value cannot be null");

        this.updatedAt = Objects.requireNonNull(updatedAt, "Task updated-at value cannot be null");

        this.completedAt = null;
        this.deletedAt = null;
        this.dueAt = dueAt;

        this.version = 1L;
    }

    // Task hydration from database
    public Task(UUID id, String title, TaskStatus status, Instant createdAt, Instant updatedAt, Instant completedAt, Instant deletedAt, Instant dueAt, long version) {
        this.id = Objects.requireNonNull(id, "Task id cannot be null");

        this.title = Objects.requireNonNull(title, "Task title cannot be null");
        this.title = normalizeTitle(this.title);
        if (this.title.isBlank() || this.title.isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be blank or empty");
        }

        this.status = Objects.requireNonNull(status, "Task status cannot be null");

        this.createdAt = Objects.requireNonNull(createdAt, "Task created-at value cannot be null");

        this.updatedAt = Objects.requireNonNull(updatedAt, "Task updated-at value cannot be null");

        this.completedAt = completedAt;

        // Both nullable as values are not needed in retrieval / initial construction of a task
        this.deletedAt = deletedAt;
        this.dueAt = dueAt;

        this.version = version;
        if (this.version <= 0) {
            throw new IllegalArgumentException("Task version must be greater than 0");
        }
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

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public long getVersion() {
        return version;
    }

    public static String normalizeTitle(String title) {
        return title.trim();
    }

    /**
     * Updates the task's updatedAt timestamp and increments the version number.
     * This method should be called whenever a task is modified to ensure that
     * the updatedAt timestamp reflects the latest modification time and the
     * version number is updated accordingly.
     */
    public void updateTask() {
        this.updatedAt = Instant.now();
        this.version++;
    }

    public void changeTitle(String newTitle) {
        Objects.requireNonNull(newTitle, "New title cannot be null");
        newTitle = normalizeTitle(newTitle);
        if (newTitle.isBlank() || newTitle.isEmpty()) {
            throw new IllegalArgumentException("New title cannot be blank or empty");
        }
        this.title = newTitle;
        this.updateTask();
    }

    public void changeDueTime(Instant newDueTime) {
        Objects.requireNonNull(newDueTime, "New due time cannot be null");
        this.dueAt = newDueTime;
        this.updateTask();
    }

    public void removeDueTime() {
        this.dueAt = null;
        this.updateTask();
    }

    public void changeStatus(TaskStatus newStatus) {
        Objects.requireNonNull(newStatus, "New status cannot be null");
        this.status = newStatus;
        this.updateTask();
    }

    public void completeTask() {
        this.status = TaskStatus.DONE;
        this.completedAt = Instant.now();
        this.updateTask();
    }

    public void reopenTask() {
        this.status = TaskStatus.ONGOING;
        this.completedAt = null;
        this.updateTask();
    }
}
