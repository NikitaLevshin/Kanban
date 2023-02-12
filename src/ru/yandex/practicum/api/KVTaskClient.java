package ru.yandex.practicum.api;

import ru.yandex.practicum.exceptions.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String URL;
    private final String API_TOKEN;
    public KVTaskClient(String url) {
        URL = url;
        API_TOKEN = apiToken();
    }

    public String apiToken() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "register"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Запрос не получен, код статуса " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Запрос не получен");
        }
    }

    public void put (String key, String json) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "save/" + key + "?API_TOKEN=" + API_TOKEN))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Запрос не получен, код статуса " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Запрос не получен");
        }
    }

    public String load (String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(URL + "load/" + key + "?API_TOKEN=" + API_TOKEN))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Запрос не получен, код статуса " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Запрос не получен");
        }
    }
}
