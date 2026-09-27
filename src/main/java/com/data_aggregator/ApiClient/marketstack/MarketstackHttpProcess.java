package com.data_aggregator.ApiClient.marketstack;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.data_aggregator.HtmlClient.MyHttpClient;
import com.data_aggregator.Models.MarketstackExchange;

public class MarketstackHttpProcess {
    private final ObjectMapper objectMapper;

    public MarketstackHttpProcess() {
        objectMapper = new ObjectMapper();
    }

    public List<MarketstackExchange> process(MyHttpClient httpClient) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.makeResponse();
        MarketstackResponse marketstackResponse;

        try {
            marketstackResponse = objectMapper.readValue(response.body(), MarketstackResponse.class);
        } catch (JsonProcessingException e) {
            throw new IOException("Cannot parse Marketstack API response: " + e.getOriginalMessage(), e);
        }

        if (marketstackResponse.error != null && !marketstackResponse.error.isNull()) {
            throw new IOException("Marketstack API error: " + marketstackResponse.error);
        }

        if (marketstackResponse.data == null) {
            throw new IOException("Marketstack API response does not contain data array");
        }

        return marketstackResponse.data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MarketstackResponse {
        public List<MarketstackExchange> data;
        public JsonNode error;
    }
}
