package com.data_aggregator.Serialization;

import java.util.List;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvSerialization<T> {
    public String serialize(List<T> list, Class<T> type) throws Exception {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = csvMapper.schemaFor(type).withHeader();
        return csvMapper.writer(schema).writeValueAsString(list);
    }
}

