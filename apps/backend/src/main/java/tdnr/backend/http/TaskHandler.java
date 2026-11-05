package tdnr.backend.http;

import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import tdnr.backend.task.TaskService;

public class TaskHandler implements HttpHandler {

    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    public TaskHandler(TaskService taskService, ObjectMapper objectMapper) {
        this.taskService = Objects.requireNonNull(taskService);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    // POST /api/v1/tasks
    @Override
    public void handle(HttpExchange exchange) throws java.io.IOException {
        String method = exchange.getRequestMethod();
        if (!"POST".equals(method)) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        String response = "Task handler response";
        exchange.sendResponseHeaders(200, response.length());
        try (java.io.OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

}
