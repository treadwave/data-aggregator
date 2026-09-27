package com.data_aggregator;

import com.data_aggregator.Models.MarketstackExchange;
import com.data_aggregator.Models.Post;
import com.data_aggregator.Models.Weather;

public final class TestData {
    private TestData() {
    }

    public static Post post() {
        return new Post(
            "Movie",
            "/movie",
            "poster",
            "fanart",
            new Post.Ids(101, "movie"),
            "2026-01-01",
            7,
            "1%",
            12,
            34,
            new Post.Ratings(
                new Post.Simkl(8.1, 100),
                new Post.Imdb(7.9, 200)
            ),
            "us",
            "2h",
            "released",
            "2026-02-01",
            "Overview"
        );
    }

    public static Weather weather() {
        return new Weather(
            new Weather.Request("City", "Moscow, Russia", "en", "m"),
            new Weather.Location("Moscow", "Russia", "Moscow City", "55.752", "37.616", "2026-06-11 10:00"),
            new Weather.Current(
                "07:00 AM",
                22,
                "116",
                new Weather.AirQuality(1.1, 2.2, 3.3, 4.4),
                5,
                60,
                3
            ),
            new Weather.Forecast(10, 25, 18)
        );
    }

    public static MarketstackExchange exchange() {
        return new MarketstackExchange(
            "NASDAQ - ALL MARKETS",
            "NASDAQ",
            "XNAS",
            null,
            "US",
            "NEW YORK",
            "www.nasdaq.com",
            new MarketstackExchange.Timezone("America/New_York", "EST", "EDT"),
            new MarketstackExchange.Currency("USD", "$", "US Dollar")
        );
    }
}
