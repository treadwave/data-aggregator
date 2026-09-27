package com.data_aggregator.Serialization;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.Models.JsonSerializationModel;

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