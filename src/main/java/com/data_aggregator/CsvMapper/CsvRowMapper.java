package com.data_aggregator.CsvMapper;

import java.util.List;

import com.data_aggregator.Models.AggregatedCsvRow;

public interface CsvRowMapper<T> {
    String getSourceName();

    Class<T> getDataType();

    List<AggregatedCsvRow> toRows(List<T> data, int id);
}
