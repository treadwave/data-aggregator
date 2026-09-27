package com.kursach.ApiClient.simkl;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursach.HtmlClient.MyHttpClient;
import com.kursach.Models.Post;

public class SimklHttpProcess {
    private final ObjectMapper objectMapper;

    public SimklHttpProcess() {
        objectMapper = new ObjectMapper();
    }

    public List<Post> process(MyHttpClient httpClient) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.makeResponse();

        try {
            return objectMapper.readValue(response.body(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IOException("Cannot parse Simkl API response: " + e.getOriginalMessage(), e);
        }
    }
}
