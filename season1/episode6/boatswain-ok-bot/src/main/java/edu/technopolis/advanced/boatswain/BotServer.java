package edu.technopolis.advanced.boatswain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import edu.technopolis.advanced.boatswain.incoming.request.MessageNotification;
import edu.technopolis.advanced.boatswain.request.SendMessageRequest;

class BotServer {
    private static final Logger log = LoggerFactory.getLogger(BotServer.class);

    private final HttpServer httpServer;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    BotServer(String endpoint, Function<MessageNotification, Boolean> sender) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 8080);
        httpServer = HttpServer.create(addr, 0);
        httpServer.createContext(endpoint, exchange -> {
            MessageNotification notif;
            try (InputStream is = exchange.getRequestBody()) {
                notif = MAPPER.readValue(is, MessageNotification.class);
                log.info("Notification is received <{}>", notif);
            } catch (IOException e) {
                log.error("Failed to read notification", e);
                writeResponse(exchange, 500, "failure");
                return;
            }
            if (sender.apply(notif)) {
                writeResponse(exchange, 200, "ok");
            } else {
                writeResponse(exchange, 500, "failure");
            }
        });
    }

    private void writeResponse(HttpExchange exchange, int code, String content) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
        byte[] response = content.getBytes();
        exchange.sendResponseHeaders(code, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }


    BotServer start() {
        httpServer.start();
        return this;
    }

    void stop() {
        httpServer.stop(1);
    }
}
