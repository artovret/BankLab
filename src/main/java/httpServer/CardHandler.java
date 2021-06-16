package httpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import json.BankIdJson;
import json.JsonReader;
import lombok.SneakyThrows;
import service.CardCervice;

import java.io.IOException;
import java.io.PrintStream;

/**
 *
 */
public class CardHandler implements HttpHandler {
    /**
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws NullPointerException if exchange is <code>null</code>
     */
    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);
            PrintStream response = new PrintStream(exchange.getResponseBody());
            String code = SimpleServer.getStr(exchange);
            for (BankIdJson bankIdJson : new JsonReader().getBankId(code)) {
                new CardCervice().add(bankIdJson.getCode());
            }
            response.close();
        } else {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            PrintStream response = new PrintStream(exchange.getResponseBody());
            response.println(new CardCervice().getAll());
            response.close();
        }
    }
}
