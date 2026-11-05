package tdnr.backend.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.Test;

import tdnr.backend.task.InMemoryTaskRepository;
import tdnr.backend.task.TaskRepository;
import tdnr.backend.task.TaskService;

public class HealthEndpointTest {

    @Test
    public void testHealthEndpoint() throws Exception {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        TaskService taskService = new TaskService(taskRepository, java.time.Clock.systemUTC());
        TaskHandler taskHandler = new TaskHandler(taskService, new com.fasterxml.jackson.databind.ObjectMapper());
        HealthHandler healthHandler = new HealthHandler();
        TdnrServer server = new TdnrServer(healthHandler, taskHandler);
        server.start(0); // Start on a random available port
        int port = server.getPort();

        try {
            // Make an HTTP GET request to the /health endpoint
            URI url = new URI("http://localhost:" + port + "/health");
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.toURL().openConnection();
            connection.setRequestMethod("GET");

            // Check the response code and body
            int responseCode = connection.getResponseCode();
            assertEquals(200, responseCode);

            String contentType = connection.getHeaderField("Content-Type");
            assertEquals("text/plain; charset=UTF-8", contentType);

            try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()))) {
                String responseBody = in.readLine();
                assertEquals("OK", responseBody);
            } finally {
                connection.disconnect();
            }
        } finally {
            server.stop(0); // Stop the server
        }
    }

}
