package com.data_aggregator.Models;

import java.time.*;

public class JsonSerializationModel {
    public int id;
    public String source;
    public String timestamp = OffsetDateTime.now().toString();
    public Object data;

    public JsonSerializationModel() {
    }

    public JsonSerializationModel(int id, String source, Object data) {
        this.id = id;
        this.source = source;
        this.data = data;
    }

}
