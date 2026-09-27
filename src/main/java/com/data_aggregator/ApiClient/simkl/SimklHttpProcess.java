package com.data_aggregator.ApiClient.simkl;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.data_aggregator.HtmlClient.MyHttpClient;
import com.data_aggregator.Models.Post;

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
