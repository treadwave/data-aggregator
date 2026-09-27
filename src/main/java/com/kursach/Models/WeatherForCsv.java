package com.kursach.Models;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "id",
    "source",
    "timestamp",
    "request.type",
    "request.query",
    "request.language",
    "location.name",
    "location.country",
    "location.region",
    "location.lat",
    "location.lon",
    "location.localtime",
    "current.observation_time",
    "current.temperature",
    "current.weather_code",
    "current.air_quality.co",
    "current.air_quality.no2",
    "current.air_quality.o3",
    "current.air_quality.so2",
    "current.wind_speed",
    "current.humidity",
    "current.uv_index"
})

public class WeatherForCsv {
    public WeatherForCsv(String source) {
        this.source = source;
    }

    public void fillWeather(int id, Weather weather) {
        this.id = id;

        this.requestType = weather.request().type();
        this.requestQuery = weather.request().query();
        this.requestLanguage = weather.request().language();

        this.locationName = weather.location().name();
        this.locationCountry = weather.location().country();
        this.locationRegion = weather.location().region();
        this.locationLat = weather.location().lat();
        this.locationLon = weather.location().lon();
        this.locationLocaltime = weather.location().localtime();

        this.observationTime = weather.current().observationTime();
        this.temperature = weather.current().temperature();
        this.weatherCode = weather.current().weatherCode();

        this.airQualityCo = weather.current().airQuality().co();
        this.airQualityNo2 = weather.current().airQuality().no2();
        this.airQualityO3 = weather.current().airQuality().o3();
        this.airQualitySo2 = weather.current().airQuality().so2();

        this.windSpeed = weather.current().windSpeed();
        this.humidity = weather.current().humidity();
        this.uvIndex = weather.current().uvIndex();
    }

    public int id;
    public String source;
    public String timestamp = OffsetDateTime.now().toString();

    @JsonProperty("request.type")
    public String requestType;

    @JsonProperty("request.query")
    public String requestQuery;

    @JsonProperty("request.language")
    public String requestLanguage;

    @JsonProperty("location.name")
    public String locationName;

    @JsonProperty("location.country")
    public String locationCountry;

    @JsonProperty("location.region")
    public String locationRegion;

    @JsonProperty("location.lat")
    public String locationLat;

    @JsonProperty("location.lon")
    public String locationLon;

    @JsonProperty("location.localtime")
    public String locationLocaltime;

    @JsonProperty("current.observation_time")
    public String observationTime;

    @JsonProperty("current.temperature")
    public int temperature;

    @JsonProperty("current.weather_code")
    public String weatherCode;

    @JsonProperty("current.air_quality.co")
    public double airQualityCo;

    @JsonProperty("current.air_quality.no2")
    public double airQualityNo2;

    @JsonProperty("current.air_quality.o3")
    public double airQualityO3;

    @JsonProperty("current.air_quality.so2")
    public double airQualitySo2;

    @JsonProperty("current.wind_speed")
    public int windSpeed;

    @JsonProperty("current.humidity")
    public int humidity;

    @JsonProperty("current.uv_index")
    public int uvIndex;
}