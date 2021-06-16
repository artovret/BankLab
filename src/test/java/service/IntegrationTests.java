package service;

import database.MemDataBaseFile;
import httpServer.SimpleServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTests {
    HttpClient client;
    HttpRequest requets;

    @BeforeEach
    void startServer() throws IOException, SQLException {
        //new MemDataBase().initDb();
        new MemDataBaseFile().initDb();
        client = HttpClient.newHttpClient();
        SimpleServer.createConnection();
    }

    @AfterEach
    void toolZ(){
        SimpleServer.closeConnection();
    }


    @Test
    void getCards() throws IOException, InterruptedException {
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/user/Nikita")).GET().build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("4444 4444 4648 6865"));
    }

    @Test
    void getNameBalance() throws IOException, InterruptedException {
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/user/Nikita/Balance")).GET().build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("40670.00"));
    }
    @Test
    void getBankBalance() throws IOException, InterruptedException {
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/user/Artem/88888888899914439333")).GET().build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("30000.01"));
    }
    @Test
    void sendManeyGet() throws IOException, InterruptedException {
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/send/")).GET().build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }


    @Test
    void setBalance() throws IOException, InterruptedException {
        String requestBody = "[\n" +
                "    {\n" +
                "        \"balance\": \"50000\",\n" +
                "        \"cardName\": \"6666 6666 6666 6666\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"cardName\": \"1111 1111 1111 1111\",\n" +
                "        \"balance\": \"30000\"\n" +
                "    }\n" +
                "]";
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/user/")).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    void newCard() throws IOException, InterruptedException {
        String requestBody = "[\n" +
                "  {\n" +
                "    \"code\": \"88888888899914439333\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"code\": \"88888888899914439333\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"code\": \"88888888899914439333\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"code\": \"88888888899914439333\"\n" +
                "  }\n" +
                "]";
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/card/")).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void sendManey() throws IOException, InterruptedException {
        String requestBody = "[\n" +
                "  {\n" +
                "    \"sender\":\"40817810099910004777\",\n" +
                "    \"receiver\":\"40817810099910009999\",\n" +
                "    \"summ\":\"30000\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"sender\":\"88888888899914439333\",\n" +
                "    \"receiver\":\"40817810099910009999\",\n" +
                "    \"summ\":\"50000\"\n" +
                "  }\n" +
                "]";
        requets = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/send/")).POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        HttpResponse<String> response = client.send(requets, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

}
