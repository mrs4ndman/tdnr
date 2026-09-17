package tndr.backend.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class TaskTest {

    private static final String TITLE = "Test Task";
    private static final TaskStatus STATUS = TaskStatus.ONGOING;

    private Task newTask() {
        Instant now = Instant.now();
        return new Task(
                UUID.randomUUID(),
                TITLE,
                STATUS,
                now,
                now,
                now.plusSeconds(3600)
        );
    }

    private Task newHydratedTask() {
        Instant now = Instant.now();
        return new Task(
                UUID.randomUUID(),
                TITLE,
                TaskStatus.DONE,
                now,
                now,
                now.plusSeconds(1800),
                null,
                now.plusSeconds(3600),
                2
        );
    }

    @Test
    public void createValidTask() {
        Task task = newTask();

        assertEquals(TITLE, task.getTitle());
        assertEquals(STATUS, task.getStatus());
        assertEquals(1, task.getVersion());
        assertEquals(task.getCreatedAt(), task.getUpdatedAt());
        assertEquals(task.getCreatedAt().plusSeconds(3600), task.getDueAt());
        assertNull(task.getCompletedAt());
        assertNull(task.getDeletedAt());
    }

    @Test
    public void createTaskWithNullId() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Task(
                        null,
                        TITLE,
                        STATUS,
                        Instant.now(),
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                )
        );

        assertEquals("Task id cannot be null", exception.getMessage());
    }

    @Test
    public void createTaskWithNullTitle() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Task(
                        UUID.randomUUID(),
                        null,
                        STATUS,
                        Instant.now(),
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                )
        );

        assertEquals("Task title cannot be null", exception.getMessage());
    }

    @Test
    public void createTaskWithBlankTitle() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Task(
                        UUID.randomUUID(),
                        "   ",
                        STATUS,
                        Instant.now(),
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                )
        );

        assertEquals("Task title cannot be blank or empty", exception.getMessage());
    }

    @Test
    public void createTaskWithNullStatus() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Task(
                        UUID.randomUUID(),
                        TITLE,
                        null,
                        Instant.now(),
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                )
        );

        assertEquals("Task status cannot be null", exception.getMessage());
    }

    @Test
    public void createTaskWithNullCreatedAt() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Task(
                        UUID.randomUUID(),
                        TITLE,
                        STATUS,
                        null,
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                )
        );

        assertEquals("Task created-at value cannot be null", exception.getMessage());
    }

    @Test
    public void createTaskWithNullUpdatedAt() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Task(
                        UUID.randomUUID(),
                        TITLE,
                        STATUS,
                        Instant.now(),
                        null,
                        Instant.now().plusSeconds(3600)
                )
        );

        assertEquals("Task updated-at value cannot be null", exception.getMessage());
    }

    @Test
    public void createTaskWithNullDueAt() {
        Instant now = Instant.now();
        Task task = new Task(
                UUID.randomUUID(),
                TITLE,
                STATUS,
                now,
                now,
                null
        );

        assertNull(task.getDueAt());
    }

    @Test
    public void createTaskWithNullCompletedAt() {
        Task task = newTask();

        assertNull(task.getCompletedAt());
    }

    @Test
    public void createTaskTrimsTitle() {
        Instant now = Instant.now();
        Task task = new Task(
                UUID.randomUUID(),
                "   Test Task   ",
                STATUS,
                now,
                now,
                now.plusSeconds(3600)
        );

        assertEquals(TITLE, task.getTitle());
    }

    @Test
    public void hydrateTaskWithValidValues() {
        Task task = newHydratedTask();

        assertEquals(TITLE, task.getTitle());
        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals(2, task.getVersion());
        assertNull(task.getDeletedAt());
        assertEquals(task.getCreatedAt().plusSeconds(1800), task.getCompletedAt());
        assertEquals(task.getCreatedAt().plusSeconds(3600), task.getDueAt());
    }

    @Test
    public void hydrateTaskWithNullId() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Task(
                        null,
                        TITLE,
                        TaskStatus.DONE,
                        Instant.now(),
                        Instant.now(),
                        Instant.now().plusSeconds(1800),
                        null,
                        Instant.now().plusSeconds(3600),
                        1
                )
        );

        assertEquals("Task id cannot be null", exception.getMessage());
    }

    @Test
    public void hydrateTaskWithNullRequiredFields() {
        Instant now = Instant.now();
        Instant completedAt = now.plusSeconds(1800);
        Instant dueAt = now.plusSeconds(3600);

        assertEquals(
                "Task title cannot be null",
                assertThrows(
                        NullPointerException.class,
                        () -> hydratedTask(UUID.randomUUID(), null, TaskStatus.DONE, now, now, completedAt, dueAt, 2)
                ).getMessage()
        );

        assertEquals(
                "Task status cannot be null",
                assertThrows(
                        NullPointerException.class,
                        () -> hydratedTask(UUID.randomUUID(), TITLE, null, now, now, completedAt, dueAt, 2)
                ).getMessage()
        );

        assertEquals(
                "Task created-at value cannot be null",
                assertThrows(
                        NullPointerException.class,
                        () -> hydratedTask(UUID.randomUUID(), TITLE, TaskStatus.DONE, null, now, completedAt, dueAt, 2)
                ).getMessage()
        );

        assertEquals(
                "Task updated-at value cannot be null",
                assertThrows(
                        NullPointerException.class,
                        () -> hydratedTask(UUID.randomUUID(), TITLE, TaskStatus.DONE, now, null, completedAt, dueAt, 2)
                ).getMessage()
        );
    }

    private Task hydratedTask(
            UUID id,
            String title,
            TaskStatus status,
            Instant createdAt,
            Instant updatedAt,
            Instant completedAt,
            Instant dueAt,
            long version
    ) {
        return new Task(
                id,
                title,
                status,
                createdAt,
                updatedAt,
                completedAt,
                null,
                dueAt,
                version
        );
    }

    @Test
    public void hydrateTaskWithInvalidVersion() {
        Instant now = Instant.now();

        for (long version : new long[]{0, -1}) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> hydratedTask(
                            UUID.randomUUID(),
                            TITLE,
                            TaskStatus.DONE,
                            now,
                            now,
                            now.plusSeconds(1800),
                            now.plusSeconds(3600),
                            version
                    )
            );

            assertEquals("Task version must be greater than 0", exception.getMessage());
        }
    }

    @Test
    public void updateTask() {
        Task task = newTask();
        Instant oldUpdatedAt = task.getUpdatedAt();
        long oldVersion = task.getVersion();

        task.updateTask();

        assertEquals(oldVersion + 1, task.getVersion());
        assertEquals(TITLE, task.getTitle());
        assertEquals(STATUS, task.getStatus());
        assertTrue(task.getUpdatedAt().isAfter(oldUpdatedAt));
    }

    @Test
    public void changeTitle() {
        Task task = newTask();
        Instant oldUpdatedAt = task.getUpdatedAt();
        long oldVersion = task.getVersion();

        task.changeTitle("A very different title");

        assertEquals("A very different title", task.getTitle());
        assertEquals(oldVersion + 1, task.getVersion());
        assertNotNull(task.getUpdatedAt());
        assertTrue(task.getUpdatedAt().isAfter(oldUpdatedAt));

        assertThrows(NullPointerException.class, () -> task.changeTitle(null));
        assertThrows(IllegalArgumentException.class, () -> task.changeTitle("   "));
    }

    @Test
    public void changeDueTime() {
        Task task = newTask();
        Instant oldUpdatedAt = task.getUpdatedAt();
        long oldVersion = task.getVersion();
        Instant newDueAt = Instant.now().plusSeconds(3600);

        task.changeDueTime(newDueAt);

        assertEquals(newDueAt, task.getDueAt());
        assertEquals(oldVersion + 1, task.getVersion());
        assertNotNull(task.getUpdatedAt());
        assertTrue(task.getUpdatedAt().isAfter(oldUpdatedAt));

        task.removeDueTime();

        assertNull(task.getDueAt());
        assertEquals(oldVersion + 2, task.getVersion());
        assertNotNull(task.getUpdatedAt());
        assertTrue(task.getUpdatedAt().isAfter(oldUpdatedAt));

        assertThrows(NullPointerException.class, () -> task.changeDueTime(null));
    }

    @Test
    public void changeStatus() {
        Task task = newTask();

        task.changeStatus(TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals(2, task.getVersion());

        assertThrows(NullPointerException.class, () -> task.changeStatus(null));
    }

    @Test
    public void completeTask() {
        Task task = newTask();

        task.completeTask();

        assertEquals(TaskStatus.DONE, task.getStatus());
        assertEquals(2, task.getVersion());
        assertNotNull(task.getUpdatedAt());
    }

    @Test
    public void reopenTask() {
        Instant now = Instant.now();
        Task task = new Task(
                UUID.randomUUID(),
                TITLE,
                TaskStatus.DONE,
                now,
                now,
                now.minusSeconds(1800),
                null,
                now.plusSeconds(3600),
                1
        );

        task.reopenTask();

        assertEquals(TaskStatus.ONGOING, task.getStatus());
        assertNull(task.getCompletedAt());
        assertEquals(2, task.getVersion());
    }
}
