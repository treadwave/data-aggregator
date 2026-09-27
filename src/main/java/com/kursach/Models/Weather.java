package com.kursach.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)

public record Weather(
    Request request,
    Location location,
    Current current,
    Forecast forecast
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String type,
        String query,
        String language,
        String unit
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)

    public record Location(
        String name,
        String country,
        String region,
        String lat,
        String lon,
        String localtime
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AirQuality(
        double co,
        double no2,
        double o3,
        double so2
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Current(
        @JsonProperty("observation_time") String observationTime,
        Integer temperature,
        @JsonProperty("weather_code") String weatherCode,
        @JsonProperty("air_quality") AirQuality airQuality,
        @JsonProperty("wind_speed") Integer windSpeed,
        Integer humidity,
        @JsonProperty("uv_index") Integer uvIndex
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Forecast(
        @JsonProperty("mintemp") Integer minTemp,
        @JsonProperty("maxtemp") Integer maxTemp,
        @JsonProperty("avgtemp") Integer avgTemp
    ) {}
}