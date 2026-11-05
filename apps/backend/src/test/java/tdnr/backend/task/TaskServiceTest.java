package tdnr.backend.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskServiceTest {

    // Shared fixtures are recreated before every test.
    final Instant fixedInstant = Instant.parse("2024-01-01T00:00:00Z");
    TaskRepository taskRepository;
    Clock clock;
    TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = new InMemoryTaskRepository();
        clock = Clock.fixed(fixedInstant, java.time.ZoneOffset.UTC);
        taskService = new TaskService(taskRepository, clock);
    }

    // Dependency validation
    @Test
    void testConstructorWithNullRepository() {

        Exception exception = assertThrows(NullPointerException.class, () -> new TaskService(null, clock), "Task repository cannot be null");
        assertEquals("Task repository cannot be null", exception.getMessage());
    }

    @Test
    void testConstructorWithNullClock() {
        Exception exception = assertThrows(NullPointerException.class, () -> new TaskService(taskRepository, null), "Clock cannot be null");
        assertEquals("Clock cannot be null", exception.getMessage());
    }

    // Task creation
    @Test
    void testCreateTaskWithValidInputs() {
        String title = "Test Task";
        TaskStatus status = TaskStatus.ONGOING;
        Instant dueAt = fixedInstant.plusSeconds(3600);

        Task createdTask = taskService.createTask(title, status, dueAt);

        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());
        assertEquals(title, createdTask.getTitle());
        assertEquals(status, createdTask.getStatus());
        assertEquals(fixedInstant, createdTask.getCreatedAt());
        assertEquals(fixedInstant, createdTask.getUpdatedAt());
        assertEquals(dueAt, createdTask.getDueAt());
        assertEquals(1, createdTask.getVersion());
    }

    // Task retrieval
    @Test
    void testGetTaskById() {
        String title = "Test Task";
        TaskStatus status = TaskStatus.ONGOING;
        Instant dueAt = fixedInstant.plusSeconds(3600);

        Task createdTask = taskService.createTask(title, status, dueAt);
        Task retrievedTask = taskService.getTask(createdTask.getId()).orElse(null);

        assertNotNull(retrievedTask);
        assertEquals(createdTask.getId(), retrievedTask.getId());
    }

    @Test
    void testGetTaskByNonExistentId() {
        Task retrievedTask = taskService.getTask(java.util.UUID.randomUUID()).orElse(null);
        assertNull(retrievedTask);
    }

    @Test
    void testGetAllTasks() {
        String title1 = "Task 1";
        String title2 = "Task 2";
        TaskStatus status = TaskStatus.ONGOING;
        Instant dueAt = fixedInstant.plusSeconds(3600);

        taskService.createTask(title1, status, dueAt);
        taskService.createTask(title2, status, dueAt);

        assertEquals(2, taskService.getAllTasks().size());
    }

    @Test
    void testCreateTaskWithoutDueDate() {
        String title = "Task without due date";
        TaskStatus status = TaskStatus.ONGOING;
        Instant dueAt = null;

        Task createdTask = taskService.createTask(title, status, dueAt);

        assertNotNull(createdTask);
        assertEquals(title, createdTask.getTitle());
        assertEquals(status, createdTask.getStatus());
        assertEquals(fixedInstant, createdTask.getCreatedAt());
        assertEquals(fixedInstant, createdTask.getUpdatedAt());
        assertNull(createdTask.getDueAt());
    }
}
