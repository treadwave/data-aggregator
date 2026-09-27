package com.data_aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.data_aggregator.CsvMapper.CsvMapperRegistry;
import com.data_aggregator.CsvMapper.CsvRowMapper;
import com.data_aggregator.CsvMapper.MarketstackCsvMapper;
import com.data_aggregator.CsvMapper.SimklCsvMapper;
import com.data_aggregator.CsvMapper.WeatherCsvMapper;
import com.data_aggregator.Models.AggregatedCsvRow;
import com.data_aggregator.Models.MarketstackExchange;
import com.data_aggregator.Models.PostForCsv;
import com.data_aggregator.Models.WeatherForCsv;

public class CsvMapperTest {
    @Test
    public void registryFindsCsvMappersBySourceName() {
        CsvMapperRegistry registry = new CsvMapperRegistry();

        Set<String> sources = registry.getAvailableMappers().stream()
            .map(CsvRowMapper::getSourceName)
            .collect(Collectors.toSet());

        assertEquals(Set.of("simkl", "weatherstack", "marketstack"), sources);
        assertEquals("simkl", registry.getMapper("simkl").getSourceName());
    }

    @Test
    public void simklMapperCopiesPostFieldsToCommonRow() {
        AggregatedCsvRow row = new SimklCsvMapper(5).getRows(List.of(TestData.post())).get(0);

        assertEquals(5, row.id);
        assertEquals("simkl", row.source);
        assertEquals("Movie", row.title);
        assertEquals(Integer.valueOf(101), row.simklId);
        assertEquals(Double.valueOf(8.1), row.simklRating);
        assertEquals("Overview", row.overview);
    }

    @Test
    public void weatherMapperCopiesWeatherFieldsToCommonRow() {
        AggregatedCsvRow row = new WeatherCsvMapper(6).getRows(List.of(TestData.weather())).get(0);

        assertEquals(6, row.id);
        assertEquals("weatherstack", row.source);
        assertEquals("City", row.requestType);
        assertEquals("Moscow", row.locationName);
        assertEquals(Integer.valueOf(22), row.temperature);
        assertEquals(Double.valueOf(4.4), row.airQualitySo2);
    }

    @Test
    public void marketstackMapperCopiesNestedFieldsToCommonRow() {
        AggregatedCsvRow row = new MarketstackCsvMapper(7).getRows(List.of(TestData.exchange())).get(0);

        assertEquals(7, row.id);
        assertEquals("marketstack", row.source);
        assertEquals("NASDAQ - ALL MARKETS", row.marketstackName);
        assertEquals("America/New_York", row.marketstackTimezone);
        assertEquals("EDT", row.marketstackTimezoneAbbrDst);
        assertEquals("USD", row.marketstackCurrencyCode);
    }

    @Test
    public void marketstackMapperAllowsMissingNestedObjects() {
        MarketstackExchange exchange = new MarketstackExchange(
            "Exchange",
            "EX",
            "XEX",
            "Country",
            "CC",
            "City",
            "site",
            null,
            null
        );

        AggregatedCsvRow row = new MarketstackCsvMapper(8).getRows(List.of(exchange)).get(0);

        assertEquals("Exchange", row.marketstackName);
        assertNull(row.marketstackTimezone);
        assertNull(row.marketstackCurrencyCode);
    }

    @Test
    public void oldCsvModelsStillFillFields() {
        PostForCsv postRow = new PostForCsv("simkl");
        postRow.fillPost(1, TestData.post());

        WeatherForCsv weatherRow = new WeatherForCsv("weatherstack");
        weatherRow.fillWeather(2, TestData.weather());

        assertEquals("simkl", postRow.source);
        assertEquals(101, postRow.simklId);
        assertEquals("weatherstack", weatherRow.source);
        assertEquals("Moscow", weatherRow.locationName);
    }
}
