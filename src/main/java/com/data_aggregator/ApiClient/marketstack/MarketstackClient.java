package com.data_aggregator.ApiClient.marketstack;

import java.io.IOException;
import java.util.List;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.HtmlClient.MyHttpClient;
import com.data_aggregator.Models.MarketstackExchange;

public class MarketstackClient implements ApiClient {
    private static final String API_KEY = System.getenv("MARKETSTACK_API_KEY");
    private static final String API_URL = "https://api.marketstack.com/v1/exchanges?access_key=" + API_KEY;

    private final MyHttpClient httpService;
    private final MarketstackHttpProcess process;

    public MarketstackClient() {
        process = new MarketstackHttpProcess();
        httpService = new MyHttpClient(this);
    }

    public String getName() {
        return "marketstack";
    }

    public List<MarketstackExchange> getData() throws IOException, InterruptedException {
        return process.process(httpService);
    }

    public String getKey() {
        return API_KEY;
    }

    public String getUrl() {
        return API_URL;
    }
}
