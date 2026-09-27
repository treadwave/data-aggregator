package com.data_aggregator.cli;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.ApiClient.ApiRegistry;
import com.data_aggregator.CsvMapper.CsvMapperRegistry;
import com.data_aggregator.CsvMapper.CsvRowMapper;
import com.data_aggregator.Files.FilesWriteService;
import com.data_aggregator.Models.AggregatedCsvRow;

public class AggregationService {
    private static final Object FILE_WRITE_LOCK = new Object();
    private final CsvMapperRegistry csvMapperRegistry = new CsvMapperRegistry();

    public void run(List<String> apis, String format, boolean append) throws Exception {
        int id = prepareOutput(format, append);

        List<ApiClient> clients = new ApiRegistry().getClientsByNames(apis);

        for (ApiClient client : clients) {
            aggregateClient(client, format, id);
            id++;
        }
    }

    public int prepareOutput(String format, boolean append) throws IOException {
        validateFormat(format);

        String filename = getFilename(format);
        int id = getStartId(filename, format, append);

        if (!append) {
            synchronized (FILE_WRITE_LOCK) {
                Files.deleteIfExists(Path.of(filename));
            }
        }

        return id;
    }

    public void aggregateClient(ApiClient client, String format, int id) throws Exception {
        validateFormat(format);

        List<?> data = client.getData();
        writeData(client, data, format, id);
    }

    public String getFilename(String format) {
        return format.equals("csv") ? "output.csv" : "output.json";
    }

    private void writeData(ApiClient client, List<?> data, String format, int id) throws Exception {
        String filename = getFilename(format);

        synchronized (FILE_WRITE_LOCK) {
            if (format.equals("json")) {
                FilesWriteService<Object> service = new FilesWriteService<>(filename, id);
                service.writeJson(client.getName(), data);
            } else {
                FilesWriteService<AggregatedCsvRow> service = new FilesWriteService<>(filename, id);
                CsvRowMapper<?> mapper = csvMapperRegistry.getMapper(client.getName());
                service.writeCsv(toCsvRows(mapper, data, id), AggregatedCsvRow.class);
            }
        }
    }

    private List<AggregatedCsvRow> toCsvRows(CsvRowMapper<?> mapper, List<?> data, int id) {
        return toCsvRowsTyped(mapper, data, id);
    }

    private <T> List<AggregatedCsvRow> toCsvRowsTyped(CsvRowMapper<T> mapper, List<?> data, int id) {
        return mapper.toRows(castList(data, mapper.getDataType()), id);
    }

    private <T> List<T> castList(List<?> data, Class<T> type) {
        return data.stream().map(type::cast).toList();
    }

    private void validateFormat(String format) {
        if (!format.equals("json") && !format.equals("csv")) {
            throw new IllegalArgumentException("Unknown format: " + format + ". Available formats: json, csv");
        }
    }

    private int getStartId(String filename, String format, boolean append) throws IOException {
        if (!append) {
            return 1;
        }

        Path path = Path.of(filename);
        if (!Files.exists(path) || Files.size(path) == 0) {
            return 1;
        }

        if (format.equals("json")) {
            return getNextJsonId(path);
        }

        return getNextCsvId(path);
    }

    private int getNextJsonId(Path path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(path.toFile());

        if (!root.isArray()) {
            throw new IOException("Existing JSON file must contain an array: " + path);
        }

        int maxId = 0;
        for (JsonNode item : root) {
            maxId = Math.max(maxId, item.path("id").asInt(0));
        }

        return maxId + 1;
    }

    private int getNextCsvId(Path path) throws IOException {
        int maxId = 0;
        List<String> lines = Files.readAllLines(path);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }

            String idColumn = line.split(",", 2)[0].replace("\"", "").trim();
            try {
                maxId = Math.max(maxId, Integer.parseInt(idColumn));
            } catch (NumberFormatException e) {
                throw new IOException("Cannot read id from CSV file " + path + " at line " + (i + 1), e);
            }
        }

        return maxId + 1;
    }
}
