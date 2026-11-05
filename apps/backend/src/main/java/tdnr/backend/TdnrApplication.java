package tdnr.backend;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import tdnr.backend.http.HealthHandler;
import tdnr.backend.http.TaskHandler;
import tdnr.backend.http.TdnrServer;
import tdnr.backend.task.InMemoryTaskRepository;
import tdnr.backend.task.TaskRepository;
import tdnr.backend.task.TaskService;

// Bootstrap class for the Tdnr application.
public class TdnrApplication {

    public static void main(String[] args) throws IOException {
        // Create the necessary components
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(taskRepository, java.time.Clock.systemUTC());
        TdnrServer server = new TdnrServer(
                new HealthHandler(),
                new TaskHandler(
                        taskService,
                        objectMapper
                )
        );

        server.start(8080);
    }

}
