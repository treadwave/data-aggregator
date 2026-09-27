package com.data_aggregator.HtmlClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.data_aggregator.ApiClient.ApiClient;

public class MyHttpClient {
    private final HttpClient httpClient;
    private final String API_KEY;
    private final String API_URL;

    public MyHttpClient(ApiClient client) {
        httpClient = HttpClient.newHttpClient();
        this.API_KEY = client.getKey();
        this.API_URL = client.getUrl();
    }

    public HttpResponse<String> makeResponse() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).GET().build();
        System.out.println("REQUEST URL = " + API_URL);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP request failed with status " + response.statusCode() + ": " + response.body());
        }

        if (response.body() == null || response.body().isBlank()) {
            throw new IOException("Empty response from API: " + API_URL);
        }

        return response;
    }
}
