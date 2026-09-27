package com.data_aggregator.CsvMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.data_aggregator.Models.AggregatedCsvRow;
import com.data_aggregator.Models.MarketstackExchange;

public class MarketstackCsvMapper implements CsvRowMapper<MarketstackExchange> {
    private int id;

    public MarketstackCsvMapper() {
    }

    public MarketstackCsvMapper(int id) {
        this.id = id;
    }

    public List<AggregatedCsvRow> getRows(List<MarketstackExchange> exchanges) {
        return toRows(exchanges, id);
    }

    @Override
    public String getSourceName() {
        return "marketstack";
    }

    @Override
    public Class<MarketstackExchange> getDataType() {
        return MarketstackExchange.class;
    }

    @Override
    public List<AggregatedCsvRow> toRows(List<MarketstackExchange> exchanges, int id) {
        List<AggregatedCsvRow> rows = new ArrayList<>();

        for (MarketstackExchange exchange : exchanges) {
            AggregatedCsvRow row = new AggregatedCsvRow();

            row.id = id;
            row.source = "marketstack";
            row.timestamp = OffsetDateTime.now().toString();

            row.marketstackName = exchange.name();
            row.marketstackAcronym = exchange.acronym();
            row.marketstackMic = exchange.mic();
            row.marketstackCountry = exchange.country();
            row.marketstackCountryCode = exchange.countryCode();
            row.marketstackCity = exchange.city();
            row.marketstackWebsite = exchange.website();

            if (exchange.timezone() != null) {
                row.marketstackTimezone = exchange.timezone().timezone();
                row.marketstackTimezoneAbbr = exchange.timezone().abbr();
                row.marketstackTimezoneAbbrDst = exchange.timezone().abbrDst();
            }

            if (exchange.currency() != null) {
                row.marketstackCurrencyCode = exchange.currency().code();
                row.marketstackCurrencySymbol = exchange.currency().symbol();
                row.marketstackCurrencyName = exchange.currency().name();
            }

            rows.add(row);
        }

        return rows;
    }
}
