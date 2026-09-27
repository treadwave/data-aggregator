package com.kursach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursach.ApiClient.ApiClient;
import com.kursach.cli.AggregationService;

public class AggregationServiceTest {
    private OutputFilesBackup outputFilesBackup;

    @BeforeEach
    public void setUp() throws Exception {
        outputFilesBackup = new OutputFilesBackup();
    }

    @AfterEach
    public void tearDown() throws Exception {
        outputFilesBackup.restore();
    }

    @Test
    public void aggregateClientWritesJsonInCommonFormat() throws Exception {
        AggregationService service = new AggregationService();

        service.prepareOutput("json", false);
        service.aggregateClient(new FakeApiClient("fake"), "json", 10);

        JsonNode root = new ObjectMapper().readTree(Path.of("output.json").toFile());

        assertEquals(1, root.size());
        assertEquals(10, root.get(0).get("id").asInt());
        assertEquals("fake", root.get(0).get("source").asText());
        assertEquals("ok", root.get(0).get("data").get(0).get("value").asText());
    }

    @Test
    public void aggregateClientWritesCsvSafelyWithSingleHeader() throws Exception {
        AggregationService service = new AggregationService();

        service.prepareOutput("csv", false);
        service.aggregateClient(new FakeApiClient("simkl", List.of(TestData.post())), "csv", 1);
        service.aggregateClient(new FakeApiClient("weatherstack", List.of(TestData.weather())), "csv", 2);

        String csv = Files.readString(Path.of("output.csv"));

        assertTrue(csv.startsWith("id,source,timestamp"));
        assertEquals(1, countOccurrences(csv, "id,source,timestamp"));
        assertTrue(csv.contains("simkl"));
        assertTrue(csv.contains("weatherstack"));
        assertTrue(csv.contains("Movie"));
        assertTrue(csv.contains("Moscow"));
    }

    @Test
    public void prepareOutputCalculatesNextIdsForAppendMode() throws Exception {
        Files.writeString(Path.of("output.json"), "[{\"id\":4},{\"id\":7}]");
        assertEquals(8, new AggregationService().prepareOutput("json", true));

        Files.writeString(Path.of("output.csv"), "id,source,timestamp\n2,a,t\n5,b,t\n");
        assertEquals(6, new AggregationService().prepareOutput("csv", true));
    }

    @Test
    public void serviceRejectsInvalidFormatAndUnknownApi() throws Exception {
        try {
            new AggregationService().prepareOutput("xml", false);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unknown format"));
        }

        try {
            new AggregationService().run(List.of("missing"), "json", false);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Unknown API"));
            return;
        }

        throw new AssertionError("Unknown API must be rejected");
    }

    private int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(pattern, index)) >= 0) {
            count++;
            index += pattern.length();
        }

        return count;
    }

    private static class FakeApiClient implements ApiClient {
        private final String name;
        private final List<?> data;

        FakeApiClient(String name) {
            this(name, List.of(Map.of("value", "ok")));
        }

        FakeApiClient(String name, List<?> data) {
            this.name = name;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public String getKey() {
            return "";
        }

        public String getUrl() {
            return "https://example.test";
        }

        public List<?> getData() {
            return data;
        }

    }
}
