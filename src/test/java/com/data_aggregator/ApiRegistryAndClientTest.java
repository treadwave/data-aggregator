package com.data_aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.ApiClient.ApiRegistry;
import com.data_aggregator.ApiClient.marketstack.MarketstackClient;
import com.data_aggregator.ApiClient.simkl.SimklClient;
import com.data_aggregator.ApiClient.weatherstack.WeatherstackClient;

public class ApiRegistryAndClientTest {
    @Test
    public void registryReturnsAvailableApiClients() {
        List<String> names = new ApiRegistry().getAvailableApis().stream().map(ApiClient::getName).collect(Collectors.toList());

        assertTrue(names.contains("simkl"));
        assertTrue(names.contains("weatherstack"));
        assertTrue(names.contains("marketstack"));
    }

    @Test
    public void registryReturnsClientsByNamesAndRejectsUnknownApi() {
        List<ApiClient> clients = new ApiRegistry().getClientsByNames(List.of(" MARKETSTACK ", "simkl"));

        assertEquals("marketstack", clients.get(0).getName());
        assertEquals("simkl", clients.get(1).getName());

        try {
            new ApiRegistry().getClientsByNames(List.of("missing"));
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unknown API"));
            return;
        }

        throw new AssertionError("Unknown API must be rejected");
    }

    @Test
    public void clientsExposeNamesAndUrlsWithoutNetworkCalls() {
        MarketstackClient marketstackClient = new MarketstackClient();
        SimklClient simklClient = new SimklClient();
        WeatherstackClient weatherstackClient = new WeatherstackClient();

        assertEquals("marketstack", marketstackClient.getName());
        assertTrue(marketstackClient.getUrl().contains("api.marketstack.com"));

        assertEquals("simkl", simklClient.getName());
        assertTrue(simklClient.getUrl().contains("api.simkl.com"));

        assertEquals("weatherstack", weatherstackClient.getName());
        assertTrue(weatherstackClient.getUrl().contains("api.weatherstack.com"));
    }
}
