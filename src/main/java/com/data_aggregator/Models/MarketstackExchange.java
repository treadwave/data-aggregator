package com.data_aggregator.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketstackExchange(
    String name,
    String acronym,
    String mic,
    String country,
    @JsonProperty("country_code") String countryCode,
    String city,
    String website,
    Timezone timezone,
    Currency currency
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Timezone(
        String timezone,
        String abbr,
        @JsonProperty("abbr_dst") String abbrDst
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Currency(
        String code,
        String symbol,
        String name
    ) {}
}
