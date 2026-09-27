package com.data_aggregator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OutputFilesBackup {
    private final Path jsonPath = Path.of("output.json");
    private final Path csvPath = Path.of("output.csv");
    private final String json;
    private final String csv;
    private final boolean jsonExists;
    private final boolean csvExists;

    public OutputFilesBackup() throws IOException {
        jsonExists = Files.exists(jsonPath);
        csvExists = Files.exists(csvPath);
        json = jsonExists ? Files.readString(jsonPath) : null;
        csv = csvExists ? Files.readString(csvPath) : null;
        Files.deleteIfExists(jsonPath);
        Files.deleteIfExists(csvPath);
    }

    public void restore() throws IOException {
        restoreFile(jsonPath, jsonExists, json);
        restoreFile(csvPath, csvExists, csv);
    }

    private void restoreFile(Path path, boolean existed, String content) throws IOException {
        if (existed) {
            Files.writeString(path, content);
        } else {
            Files.deleteIfExists(path);
        }
    }
}
