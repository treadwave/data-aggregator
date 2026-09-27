package com.kursach.Serialization;

import com.kursach.ApiClient.ApiClient;
import com.kursach.Models.JsonSerializationModel;

public class JsonSerialization {
    private String source;
    private int id;

    public JsonSerialization(String source, int id) {
        this.source = source;
        this.id = id;
    }

    public JsonSerializationModel serialize(Object data) throws Exception {
        return new JsonSerializationModel(id, source, data);
    }
}