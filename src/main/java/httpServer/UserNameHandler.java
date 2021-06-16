package httpServer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import json.CardJson;
import json.JsonReader;
import lombok.SneakyThrows;
import service.BankIdService;
import service.CardCervice;
import service.UserService;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/**
 *
 */
class UserNameHandler implements HttpHandler {
    /**
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     * @throws NullPointerException if exchange is <code>null</code>
     */
    @SneakyThrows
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            //String requestMethod = exchange.getRequestMethod();
            String[] lexemes = exchange.getRequestURI().getPath().split("/");
            if (lexemes.length == 3) {
                String name = lexemes[2];
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, 0);
                PrintStream response = new PrintStream(exchange.getResponseBody());
                //response.println("context: USER; method: " + requestMethod);
                response.println(new CardCervice().getByUser(name));
                response.close();
            } else if (lexemes.length == 4) {
                String name = lexemes[2];
                String param = lexemes[3];
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, 0);
                PrintStream response = new PrintStream(exchange.getResponseBody());
                response.println("{\"Balance\": \"" + new UserService().getBalance(name, param) + "\"}");
                response.close();
            } else {
                Headers responseHeaders = exchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(400, 0);
                PrintStream response = new PrintStream(exchange.getResponseBody());
                response.println("I cannot parse the request".toUpperCase(Locale.ROOT));
                response.close();
            }
        } else {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);
            PrintStream response = new PrintStream(exchange.getResponseBody());
            String code = SimpleServer.getStr(exchange);
            for (CardJson card : new JsonReader().getCards(code)) {
                new BankIdService().addManey(new BankIdService().getById(new CardCervice().getByNameCode(card.getCardName()).getBankId()), card.getBalance());
            }
            response.close();
        }
    }
}
