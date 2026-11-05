package tdnr.backend.task;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class TaskService {

    private final TaskRepository taskRepository;
    private final Clock clock;

    public TaskService(TaskRepository taskRepository, Clock clock) {
        // Implementation for TaskService constructor
        this.taskRepository = Objects.requireNonNull(taskRepository, "Task repository cannot be null");
        this.clock = Objects.requireNonNull(clock, "Clock cannot be null");
    }

    /*
     */
    public Task createTask(String title, TaskStatus status, Instant dueAt) {
        // Implementation for creating a new task
        UUID taskId = UUID.randomUUID();
        Instant now = clock.instant();
        Task generatedTask = new Task(taskId, title, status, now, now, dueAt);
        return taskRepository.save(generatedTask);
    }

    public Optional<Task> getTask(UUID id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
