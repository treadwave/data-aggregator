package com.data_aggregator.ApiClient.weatherstack;

import java.io.IOException;
import java.util.List;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.HtmlClient.MyHttpClient;
import com.data_aggregator.Models.*;

public class WeatherstackClient implements ApiClient {
    private final MyHttpClient httpService;
    private WeatherstackHttpProcess process;

    public WeatherstackClient() {
        process = new WeatherstackHttpProcess();
        httpService = new MyHttpClient(this);
    }

    public String getName() {
        return "weatherstack";
    }

    public List<Weather> getData() throws IOException, InterruptedException {
        return process.process(httpService);
    }

    public String getKey() {
        return System.getenv("WEATHERSTACK_API_KEY");
    }

    public String getUrl() {
        return "https://api.weatherstack.com/current?access_key=" + getKey() + "&query=Moscow";
    }

}
