package httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

//http://localhost:8080/
public class SimpleServer {
    static HttpServer server;

    public static void createConnection() throws IOException {
        InetSocketAddress addr = new InetSocketAddress(8080);
        server = HttpServer.create(addr, 0);
        server.createContext("/exit/", new exitHandler());
        server.createContext("/card/", new CardHandler());
        server.createContext("/user/", new UserNameHandler());
        server.createContext("/send/", new SendHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    public static void closeConnection() {
        server.stop(0);
    }

    public static String getStr(HttpExchange exchange) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();
    }
}

