package com.kursach.CsvMapper;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kursach.util.ClasspathScanner;

public class CsvMapperRegistry {
    private static final String BASE_PACKAGE = "com.kursach.CsvMapper";
    private final ClasspathScanner classpathScanner = new ClasspathScanner();

    public CsvRowMapper<?> getMapper(String sourceName) {
        Map<String, CsvRowMapper<?>> mappersBySource = new LinkedHashMap<>();

        for (CsvRowMapper<?> mapper : getAvailableMappers()) {
            mappersBySource.put(mapper.getSourceName().toLowerCase(), mapper);
        }

        CsvRowMapper<?> mapper = mappersBySource.get(sourceName.trim().toLowerCase());
        if (mapper == null) {
            throw new IllegalArgumentException(
                "CSV format is not supported for API: " + sourceName + ". Available CSV APIs: " + String.join(", ", mappersBySource.keySet())
            );
        }

        return mapper;
    }

    public List<CsvRowMapper<?>> getAvailableMappers() {
        List<CsvRowMapper<?>> mappers = new ArrayList<>();

        for (CsvRowMapper<?> mapper : findRawMappers()) {
            mappers.add(mapper);
        }

        return mappers;
    }

    private List<CsvRowMapper> findRawMappers() {
        return classpathScanner.findImplementations(BASE_PACKAGE, CsvRowMapper.class);
    }
}
