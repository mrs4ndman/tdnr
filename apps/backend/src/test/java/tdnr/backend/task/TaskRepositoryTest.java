package tdnr.backend.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class TaskRepositoryTest {

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

    @Test
    void testSave() {
        Task task = newTask();
        TaskRepository repository = new InMemoryTaskRepository();

        Task savedTask = repository.save(task);

        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getStatus(), savedTask.getStatus());
        assertEquals(task.getCreatedAt(), savedTask.getCreatedAt());
        assertEquals(task.getUpdatedAt(), savedTask.getUpdatedAt());
        assertEquals(task.getDueAt(), savedTask.getDueAt());

        Task nullTask = null;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(nullTask),
                "Task cannot be null"
        );
        assertEquals("Task cannot be null", exception.getMessage());
    }

    @Test
    void testFindById() {
        Task task = newTask();
        TaskRepository repository = new InMemoryTaskRepository();

        repository.save(task);

        Task foundTask = repository.findById(task.getId()).orElse(null);

        assertEquals(task.getId(), foundTask.getId());
        assertEquals(task.getTitle(), foundTask.getTitle());
        assertEquals(task.getStatus(), foundTask.getStatus());
        assertEquals(task.getCreatedAt(), foundTask.getCreatedAt());
        assertEquals(task.getUpdatedAt(), foundTask.getUpdatedAt());
        assertEquals(task.getDueAt(), foundTask.getDueAt());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null),
                "Task id cannot be null"
        );
        assertEquals("Task id cannot be null", exception.getMessage());
    }

    @Test
    void testFindAll() {
        Task task1 = newTask();
        Task task2 = newTask();
        TaskRepository repository = new InMemoryTaskRepository();

        repository.save(task1);
        repository.save(task2);

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void testUpdateTask() {
        Task task = newTask();
        TaskRepository repository = new InMemoryTaskRepository();

        repository.save(task);

        // Update the task
        task.changeTitle("Updated Task Title");
        task.changeStatus(TaskStatus.DONE);
        repository.save(task);

        Task updatedTask = repository.findById(task.getId()).orElse(null);

        assertEquals("Updated Task Title", updatedTask.getTitle());
        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
    }

    @Test
    public void testEmptyRepository() {
        TaskRepository repository = new InMemoryTaskRepository();
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void testFindByIdNotFound() {
        TaskRepository repository = new InMemoryTaskRepository();
        assertEquals(false, repository.findById(UUID.randomUUID()).isPresent());
    }

    @Test
    public void testSaveDuplicateTask() {
        Task task = newTask();
        TaskRepository repository = new InMemoryTaskRepository();

        repository.save(task);
        task.changeTitle("Updated Task Title");
        repository.save(task); // Save the same task again
        Task retrievdTask = repository.findById(task.getId()).orElse(null);
        assertEquals("Updated Task Title", retrievdTask.getTitle());
        assertEquals(1, repository.findAll().size());
    }
}
