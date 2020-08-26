package com.decagon;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class WebServerTest {
    static WebServer webServer;

    @BeforeAll
    static void setup () throws IOException {
       new WebServer(8080).start();
    }

    @Test
    void testingIndexPage() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/"))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String files = Files.readString(Paths.get("src/main/resources/index.html"))
                    .replaceAll("\\s", "");

            assertEquals(files, response.body().replaceAll("\\s", ""));
            assertEquals(200, response.statusCode());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testingJsonResponse() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/json"))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String files = Files.readString(Paths.get("src/main/resources/response.json"))
                    .replaceAll("\\s", "");

            assertEquals(files, response.body().replaceAll("\\s", ""));
            assertEquals(200, response.statusCode());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testingNotFoundResource() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/j"))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String files = Files.readString(Paths.get("src/main/resources/notfound.html"))
                    .replaceAll("\\s", "");

            assertEquals(files, response.body().replaceAll("\\s", ""));
            assertEquals(404, response.statusCode());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}