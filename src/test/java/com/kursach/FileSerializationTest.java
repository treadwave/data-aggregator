package com.kursach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursach.Files.CsvToFile;
import com.kursach.Files.FilesWriteService;
import com.kursach.Files.JsonToFile;
import com.kursach.Models.AggregatedCsvRow;
import com.kursach.Models.JsonSerializationModel;
import com.kursach.Serialization.CsvSerialization;
import com.kursach.Serialization.JsonSerialization;

public class FileSerializationTest {
    @TempDir
    Path temporaryFolder;

    @Test
    public void jsonSerializationWrapsDataInCommonFormat() throws Exception {
        JsonSerializationModel model = new JsonSerialization("source", 3).serialize(Map.of("value", "ok"));

        assertEquals(3, model.id);
        assertEquals("source", model.source);
        assertEquals(Map.of("value", "ok"), model.data);
    }

    @Test
    public void jsonToFileCreatesAndAppendsArray() throws Exception {
        Path file = temporaryFolder.resolve("data.json");

        new JsonToFile(file.toString(), "first", 1).jsonToFile(List.of(Map.of("a", 1)));
        new JsonToFile(file.toString(), "second", 2).jsonToFile(List.of(Map.of("b", 2)));

        JsonNode root = new ObjectMapper().readTree(file.toFile());

        assertEquals(2, root.size());
        assertEquals("first", root.get(0).get("source").asText());
        assertEquals("second", root.get(1).get("source").asText());
    }

    @Test
    public void jsonToFileRejectsBrokenExistingJson() throws Exception {
        Path file = temporaryFolder.resolve("broken.json");
        Files.writeString(file, "{");

        try {
            new JsonToFile(file.toString(), "source", 1).jsonToFile(List.of());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Cannot read existing JSON file"));
            return;
        }

        throw new AssertionError("Broken JSON must be rejected");
    }

    @Test
    public void csvWritersCreateHeaderOnceAndAppendRows() throws Exception {
        Path file = temporaryFolder.resolve("data.csv");

        AggregatedCsvRow first = row(1, "first");
        AggregatedCsvRow second = row(2, "second");

        new CsvToFile<AggregatedCsvRow>(file.toString()).csvToFile(List.of(first), AggregatedCsvRow.class);
        new FilesWriteService<AggregatedCsvRow>(file.toString(), 0).writeCsv(List.of(second), AggregatedCsvRow.class);

        String csv = Files.readString(file);

        assertTrue(csv.startsWith("id,source,timestamp"));
        assertEquals(1, countOccurrences(csv, "id,source,timestamp"));
        assertTrue(csv.contains("first"));
        assertTrue(csv.contains("second"));
    }

    @Test
    public void csvSerializationReturnsHeaderAndData() throws Exception {
        String csv = new CsvSerialization<AggregatedCsvRow>().serialize(List.of(row(4, "source")), AggregatedCsvRow.class);

        assertTrue(csv.contains("id,source,timestamp"));
        assertTrue(csv.contains("source"));
    }

    private AggregatedCsvRow row(int id, String source) {
        AggregatedCsvRow row = new AggregatedCsvRow();
        row.id = id;
        row.source = source;
        row.timestamp = "time";
        row.title = "title-" + source;
        return row;
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
}
