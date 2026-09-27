package com.data_aggregator.Files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.data_aggregator.Serialization.CsvSerialization;

public class CsvToFile<T> {
    private String filename;
    private final CsvMapper mapper = new CsvMapper();

    private CsvSerialization<T> csvSerializator;

    public CsvToFile(String filename) throws Exception {
        this.filename = filename;
    }

    public void csvToFile(List<T> list, Class<T> type) throws Exception {
        Path path = Path.of(filename);
        CsvMapper mapper = new CsvMapper();
        boolean writeHeader = !Files.exists(path) || Files.size(path) == 0;

        CsvSchema schema;

        if (writeHeader) {
            schema = mapper.schemaFor(type).withHeader();
        } else {
            schema = mapper.schemaFor(type).withoutHeader();
        }

        String result = mapper.writer(schema).writeValueAsString(list);

        Files.writeString(path, result, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

    }

    public void append(String filename, List<T> data, Class<T> type) throws Exception {
        Path path = Path.of(filename);
        CsvSchema schema;
        if (!Files.exists(path) || Files.size(path) == 0) {

            schema = mapper.schemaFor(type).withHeader();

        } else {
            schema = mapper.schemaFor(type);

        }

        String csv = mapper.writer(schema).writeValueAsString(data);
        Files.writeString(path, csv, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
