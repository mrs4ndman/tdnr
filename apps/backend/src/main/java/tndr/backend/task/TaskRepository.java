package tndr.backend.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: TESTS
public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();
}
