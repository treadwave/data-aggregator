package com.kursach.ApiClient.weatherstack;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursach.HtmlClient.MyHttpClient;
import com.kursach.Models.Weather;

public class WeatherstackHttpProcess {
    private final ObjectMapper objectMapper;

    public WeatherstackHttpProcess() {
        objectMapper = new ObjectMapper();
    }

    public List<Weather> process(MyHttpClient httpClient) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.makeResponse();

        if (response.body().contains("\"success\":false")) {
            throw new IOException("Weatherstack API error: " + response.body());
        }

        try {
            return List.of(objectMapper.readValue(response.body(), Weather.class));
        } catch (JsonProcessingException e) {
            throw new IOException("Cannot parse Weatherstack API response: " + e.getOriginalMessage(), e);
        }
    }
}
