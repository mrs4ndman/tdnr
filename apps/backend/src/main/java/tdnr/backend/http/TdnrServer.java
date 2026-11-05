package tdnr.backend.http;

import java.io.IOException;
import java.util.Objects;

import com.sun.net.httpserver.HttpServer;

public class TdnrServer {

    private HttpServer httpServer;
    private int port;
    private HealthHandler healthHandler;
    private TaskHandler taskHandler;

    public TdnrServer(HealthHandler healthHandler, TaskHandler taskHandler) {
        this.healthHandler = Objects.requireNonNull(healthHandler);
        this.taskHandler = Objects.requireNonNull(taskHandler);
    }

    public int getPort() {
        return this.port;
    }

    public void start(int port) throws IOException {
        httpServer = HttpServer.create();
        httpServer.createContext("/health", healthHandler);
        httpServer.createContext("/api/v1/tasks", taskHandler);
        httpServer.setExecutor(null); // creates a default executor
        httpServer.bind(new java.net.InetSocketAddress(port), 0);
        this.port = httpServer.getAddress().getPort();
        httpServer.start();
    }

    public void stop(int seconds) {
        if (Objects.nonNull(httpServer)) {
            httpServer.stop(seconds);
        }
    }
}
