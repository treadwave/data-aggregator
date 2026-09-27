package com.data_aggregator.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Post(
    String title,
    String url,
    String poster,
    String fanart,
    Ids ids,
    @JsonProperty("release_date") String releaseDate,
    int rank,
    @JsonProperty("drop_rate") String dropRate,
    int watched,
    @JsonProperty("plan_to_watch") int planToWatch,
    Ratings ratings,
    String country,
    String runtime,
    String status,
    @JsonProperty("dvd_date") String dvdDate,
    String overview
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Ids(
        @JsonProperty("simkl_id") int simklId,
        String slug
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Simkl(
        double rating,
        int votes
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Imdb(
        double rating,
        int votes
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Ratings(
        Simkl simkl,
        Imdb imdb
    ) {}
}
