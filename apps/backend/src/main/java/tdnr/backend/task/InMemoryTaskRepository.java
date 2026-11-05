package tdnr.backend.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryTaskRepository implements TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public Task save(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (tasks.stream().anyMatch(t -> t.getId().equals(task.getId()))) {
            tasks.removeIf(t -> t.getId().equals(task.getId()));
        }
        tasks.add(task);
        return task;
    }

    @Override
    public Optional<Task> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Task id cannot be null");
        }

        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

}
