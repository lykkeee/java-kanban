package ru.yandex.potapov.schedule.manager;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KVTaskClient {
    private final String url;
    HttpClient client;
    private final String apiToken;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;
        client = HttpClient.newHttpClient();
        apiToken = register();
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        return String.valueOf(response.body());
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(json)).uri(uri).build();
        client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
    }

    private String register() throws IOException, InterruptedException {
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
        return String.valueOf(response.body());
    }

}
