package httpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import errors.BalanceErrorException;
import json.CounterpartiesJson;
import json.JsonReader;
import lombok.SneakyThrows;
import service.BankIdService;
import service.CounterpartiesService;

import java.io.IOException;
import java.io.PrintStream;

public class SendHandler implements HttpHandler {

    /**
     * Handle the given request and generate an appropriate response.
     * See {@link HttpExchange} for a description of the steps
     * involved in handling an exchange.
     *
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
            for (CounterpartiesJson counterparty : new JsonReader().getCounterparties(code)) {
                try {
                    new BankIdService().sendManey(new BankIdService().getByCode(counterparty.getSender()), new BankIdService().getByCode(counterparty.getReceiver()), counterparty.getSumm());
                } catch (BalanceErrorException e) {
                    response.println(e);
                }
            }
            response.close();
        } else {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            PrintStream response = new PrintStream(exchange.getResponseBody());
            response.println(new CounterpartiesService().getAll());
            response.close();
        }
    }
}
