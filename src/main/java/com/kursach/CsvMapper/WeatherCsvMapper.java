package com.kursach.CsvMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kursach.Models.AggregatedCsvRow;
import com.kursach.Models.Weather;

public class WeatherCsvMapper implements CsvRowMapper<Weather> {
    private int id;

    public WeatherCsvMapper() {
    }

    public WeatherCsvMapper(int id) {
        this.id = id;
    }

    public List<AggregatedCsvRow> getRows(List<Weather> weatherList) {
        return toRows(weatherList, id);
    }

    @Override
    public String getSourceName() {
        return "weatherstack";
    }

    @Override
    public Class<Weather> getDataType() {
        return Weather.class;
    }

    @Override
    public List<AggregatedCsvRow> toRows(List<Weather> weatherList, int id) {
        List<AggregatedCsvRow> rows = new ArrayList<>();

        for (Weather weather : weatherList) {
            AggregatedCsvRow row = new AggregatedCsvRow();

            row.id = id;
            row.source = "weatherstack";
            row.timestamp = OffsetDateTime.now().toString();

            row.requestType = weather.request().type();
            row.requestQuery = weather.request().query();
            row.requestLanguage = weather.request().language();

            row.locationName = weather.location().name();
            row.locationCountry = weather.location().country();
            row.locationRegion = weather.location().region();
            row.locationLat = weather.location().lat();
            row.locationLon = weather.location().lon();
            row.locationLocaltime = weather.location().localtime();

            row.observationTime = weather.current().observationTime();
            row.temperature = weather.current().temperature();
            row.weatherCode = weather.current().weatherCode();

            row.airQualityCo = weather.current().airQuality().co();
            row.airQualityNo2 = weather.current().airQuality().no2();
            row.airQualityO3 = weather.current().airQuality().o3();
            row.airQualitySo2 = weather.current().airQuality().so2();

            row.windSpeed = weather.current().windSpeed();
            row.humidity = weather.current().humidity();
            row.uvIndex = weather.current().uvIndex();

            rows.add(row);
        }

        return rows;
    }
}
