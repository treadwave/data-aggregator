package com.data_aggregator.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({
    "id",
    "source",
    "timestamp",
    "title",
    "url",
    "poster",
    "fanart",
    "ids.simkl_id",
    "ids.slug",
    "release_date",
    "rank",
    "drop_rate",
    "watched",
    "plan_to_watch",
    "ratings.simkl.rating",
    "ratings.simkl.votes",
    "ratings.imdb.rating",
    "ratings.imdb.votes",
    "country",
    "runtime",
    "status",
    "dvd_date",
    "overview",
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
    "current.uv_index",
    "marketstack.name",
    "marketstack.acronym",
    "marketstack.mic",
    "marketstack.country",
    "marketstack.country_code",
    "marketstack.city",
    "marketstack.website",
    "marketstack.timezone.timezone",
    "marketstack.timezone.abbr",
    "marketstack.timezone.abbr_dst",
    "marketstack.currency.code",
    "marketstack.currency.symbol",
    "marketstack.currency.name"
})
public class AggregatedCsvRow {
    public int id;
    public String source;
    public String timestamp;

    // simkl
    public String title;
    public String url;
    public String poster;
    public String fanart;
    public @JsonProperty("ids.simkl_id") Integer simklId;
    public @JsonProperty("ids.slug") String slug;
    public @JsonProperty("release_date") String releaseDate;
    public Integer rank;
    public @JsonProperty("drop_rate") String dropRate;
    public Integer watched;
    public @JsonProperty("plan_to_watch") Integer planToWatch;
    public @JsonProperty("ratings.simkl.rating") Double simklRating;
    public @JsonProperty("ratings.simkl.votes") Integer simklVotes;
    public @JsonProperty("ratings.imdb.rating") Double imdbRating;
    public @JsonProperty("ratings.imdb.votes") Integer imdbVotes;
    public String country;
    public String runtime;
    public String status;
    public @JsonProperty("dvd_date") String dvdDate;
    public String overview;

    // weather
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
    public Integer temperature;

    @JsonProperty("current.weather_code")
    public String weatherCode;

    @JsonProperty("current.air_quality.co")
    public Double airQualityCo;

    @JsonProperty("current.air_quality.no2")
    public Double airQualityNo2;

    @JsonProperty("current.air_quality.o3")
    public Double airQualityO3;

    @JsonProperty("current.air_quality.so2")
    public Double airQualitySo2;

    @JsonProperty("current.wind_speed")
    public Integer windSpeed;

    @JsonProperty("current.humidity")
    public Integer humidity;

    @JsonProperty("current.uv_index")
    public Integer uvIndex;

    // marketstack
    @JsonProperty("marketstack.name")
    public String marketstackName;

    @JsonProperty("marketstack.acronym")
    public String marketstackAcronym;

    @JsonProperty("marketstack.mic")
    public String marketstackMic;

    @JsonProperty("marketstack.country")
    public String marketstackCountry;

    @JsonProperty("marketstack.country_code")
    public String marketstackCountryCode;

    @JsonProperty("marketstack.city")
    public String marketstackCity;

    @JsonProperty("marketstack.website")
    public String marketstackWebsite;

    @JsonProperty("marketstack.timezone.timezone")
    public String marketstackTimezone;

    @JsonProperty("marketstack.timezone.abbr")
    public String marketstackTimezoneAbbr;

    @JsonProperty("marketstack.timezone.abbr_dst")
    public String marketstackTimezoneAbbrDst;

    @JsonProperty("marketstack.currency.code")
    public String marketstackCurrencyCode;

    @JsonProperty("marketstack.currency.symbol")
    public String marketstackCurrencySymbol;

    @JsonProperty("marketstack.currency.name")
    public String marketstackCurrencyName;
}
