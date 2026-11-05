package tdnr.backend.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public final class HealthHandler implements HttpHandler {

    public String handleHealthCheck() {
        return "OK";
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        he.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        byte[] responseBytes = handleHealthCheck().getBytes(StandardCharsets.UTF_8);
        he.sendResponseHeaders(200, responseBytes.length);
        he.getResponseBody().write(responseBytes);
        he.getResponseBody().close();
    }

}
