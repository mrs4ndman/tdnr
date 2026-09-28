package tndr.backend.task;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TaskService {

    public TaskService(TaskRepository taskRepository, Clock clock) {
        // Implementation for TaskService constructor
    }

    /*
     * Creates a new task with the given title, status, and due date. 
     * Should: 
     * 1. Validate or require its repository dependency. 
     * 2. Geenrate a UUID 
     * 3. Obtain the current time 
     * 4. Create a Task object 
     * 5. Save it through TaskRepository
     * 6. Return the saved Task object
     */
    public Task createTask(String title, TaskStatus status, Instant dueAt) {
        // Implementation for creating a new task
        return null;
    }

    Task getTask(UUID id);

    List<Task> getAllTasks();
}
