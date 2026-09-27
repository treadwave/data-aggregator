package com.data_aggregator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.net.ssl.SSLSession;

import org.junit.jupiter.api.Test;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.ApiClient.marketstack.MarketstackHttpProcess;
import com.data_aggregator.ApiClient.simkl.SimklHttpProcess;
import com.data_aggregator.ApiClient.weatherstack.WeatherstackHttpProcess;
import com.data_aggregator.HtmlClient.MyHttpClient;
import com.data_aggregator.Models.MarketstackExchange;
import com.data_aggregator.Models.Post;
import com.data_aggregator.Models.Weather;

public class HttpProcessTest {
    @Test
    public void marketstackProcessReadsDataArray() throws Exception {
        String body = """
            {
              "data": [
                {
                  "name": "NASDAQ",
                  "acronym": "NAS",
                  "mic": "XNAS",
                  "country_code": "US",
                  "city": "New York",
                  "website": "site",
                  "timezone": {"timezone": "America/New_York", "abbr": "EST", "abbr_dst": "EDT"},
                  "currency": {"code": "USD", "symbol": "$", "name": "US Dollar"}
                }
              ]
            }
            """;

        List<MarketstackExchange> exchanges = new MarketstackHttpProcess().process(new FakeHttpClient(body));

        assertEquals(1, exchanges.size());
        assertEquals("NASDAQ", exchanges.get(0).name());
        assertEquals("USD", exchanges.get(0).currency().code());
    }

    @Test
    public void marketstackProcessRejectsInvalidResponses() throws Exception {
        assertIOException(() -> new MarketstackHttpProcess().process(new FakeHttpClient("{")));
        assertIOException(() -> new MarketstackHttpProcess().process(new FakeHttpClient("{\"error\":{\"message\":\"bad\"}}")));
        assertIOException(() -> new MarketstackHttpProcess().process(new FakeHttpClient("{\"data\":{}}")));
    }

    @Test
    public void simklProcessReadsArrayAndRejectsInvalidJson() throws Exception {
        String body = """
            [{
              "title": "Movie",
              "url": "/movie",
              "poster": "poster",
              "fanart": "fanart",
              "ids": {"simkl_id": 101, "slug": "movie"},
              "release_date": "2026-01-01",
              "rank": 7,
              "drop_rate": "1%",
              "watched": 12,
              "plan_to_watch": 34,
              "ratings": {
                "simkl": {"rating": 8.1, "votes": 100},
                "imdb": {"rating": 7.9, "votes": 200}
              },
              "country": "us",
              "runtime": "2h",
              "status": "released",
              "dvd_date": "2026-02-01",
              "overview": "Overview"
            }]
            """;

        List<Post> posts = new SimklHttpProcess().process(new FakeHttpClient(body));

        assertEquals(1, posts.size());
        assertEquals("Movie", posts.get(0).title());

        assertIOException(() -> new SimklHttpProcess().process(new FakeHttpClient("{")));
    }

    @Test
    public void weatherstackProcessReadsObjectAndRejectsApiError() throws Exception {
        String body = """
            {
              "request": {"type": "City", "query": "Moscow", "language": "en", "unit": "m"},
              "location": {
                "name": "Moscow",
                "country": "Russia",
                "region": "Moscow City",
                "lat": "55.752",
                "lon": "37.616",
                "localtime": "2026-06-11 10:00"
              },
              "current": {
                "observation_time": "07:00 AM",
                "temperature": 22,
                "weather_code": "116",
                "air_quality": {"co": 1.1, "no2": 2.2, "o3": 3.3, "so2": 4.4},
                "wind_speed": 5,
                "humidity": 60,
                "uv_index": 3
              }
            }
            """;

        List<Weather> weather = new WeatherstackHttpProcess().process(new FakeHttpClient(body));

        assertEquals(1, weather.size());
        assertEquals("Moscow", weather.get(0).location().name());

        assertIOException(() -> new WeatherstackHttpProcess().process(new FakeHttpClient("{\"success\":false}")));
        assertIOException(() -> new WeatherstackHttpProcess().process(new FakeHttpClient("{")));
    }

    private void assertIOException(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
        } catch (IOException e) {
            assertTrue(e.getMessage().length() > 0);
            return;
        }

        throw new AssertionError("IOException expected");
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static class FakeHttpClient extends MyHttpClient {
        private final String body;

        FakeHttpClient(String body) {
            super(new DummyApiClient());
            this.body = body;
        }

        @Override
        public HttpResponse<String> makeResponse() {
            return new FakeHttpResponse(body);
        }
    }

    private static class DummyApiClient implements ApiClient {
        public String getName() {
            return "dummy";
        }

        public String getKey() {
            return "";
        }

        public String getUrl() {
            return "https://example.test";
        }

        public List<?> getData() {
            return List.of();
        }

    }

    private static class FakeHttpResponse implements HttpResponse<String> {
        private final String body;

        FakeHttpResponse(String body) {
            this.body = body;
        }

        public int statusCode() {
            return 200;
        }

        public HttpRequest request() {
            return null;
        }

        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (name, value) -> true);
        }

        public String body() {
            return body;
        }

        public Optional<SSLSession> sslSession() {
            return Optional.empty();
        }

        public URI uri() {
            return URI.create("https://example.test");
        }

        public Version version() {
            return Version.HTTP_1_1;
        }
    }
}
