package tdnr.backend.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import tdnr.backend.task.InMemoryTaskRepository;
import tdnr.backend.task.TaskRepository;
import tdnr.backend.task.TaskService;

public class TaskHandlerTest {

    @Test
    public void testTaskHandlerMethod() throws IOException {
        ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(taskRepository, java.time.Clock.systemUTC());
        TaskHandler taskHandler = new TaskHandler(taskService, objectMapper);
        HealthHandler healthHandler = new HealthHandler();

        TdnrServer server = new TdnrServer(healthHandler, taskHandler);
        server.start(0); // Start on a random available port
        int port = server.getPort();

        try {
            // Make an HTTP GET request to the /api/v1/tasks endpoint
            java.net.URI url = new java.net.URI("http://localhost:" + port + "/api/v1/tasks");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.toURL().openConnection();
            connection.setRequestMethod("GET");

            // Check the response code
            int responseCode = connection.getResponseCode();
            assertEquals(405, responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop(0); // Stop the server
        }

    }

}
