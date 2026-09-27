package com.kursach.Files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kursach.Models.JsonSerializationModel;
import com.kursach.Serialization.JsonSerialization;

public class JsonToFile {
    private String filename;
    private String source;
    private int id;
    private ObjectMapper mapper = new ObjectMapper();

    public JsonToFile(String filename, String source, int id) throws Exception {
        this.filename = filename;
        this.source = source;
        this.id = id;
    }

    public void jsonToFile(Object data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Path path = Path.of(filename);
        List<JsonSerializationModel> list;

        if (Files.exists(path) && Files.size(path) > 0) {
            try {
                list = mapper.readValue(Files.readString(path), new TypeReference<List<JsonSerializationModel>>() {});
            } catch (IOException e) {
                throw new IOException("Cannot read existing JSON file: " + filename, e);
            }
        } else {
            list = new ArrayList<>();
        }

        JsonSerialization result = new JsonSerialization(source, id);
        JsonSerializationModel model = result.serialize(data);

        list.add(model);

        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), list);
    }

    public void append(String filename, JsonSerializationModel model) throws Exception {
        Path path = Path.of(filename);
        List<JsonSerializationModel> allData;

        if (Files.exists(path) && Files.size(path) > 0) {
            try {
                allData = mapper.readValue(Files.readString(path), new TypeReference<List<JsonSerializationModel>>() {});
            } catch (IOException e) {
                throw new IOException("Cannot read existing JSON file: " + filename, e);
            }

        } else {
            allData = new ArrayList<>();
        }

        allData.add(model);

        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), allData);

    }
}
