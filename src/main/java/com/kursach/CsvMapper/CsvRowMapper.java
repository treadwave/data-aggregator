package com.kursach.CsvMapper;

import java.util.List;

import com.kursach.Models.AggregatedCsvRow;

public interface CsvRowMapper<T> {
    String getSourceName();

    Class<T> getDataType();

    List<AggregatedCsvRow> toRows(List<T> data, int id);
}
