package com.data_aggregator.ApiClient.simkl;

import java.io.IOException;
import java.util.List;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.HtmlClient.MyHttpClient;
import com.data_aggregator.Models.*;

public class SimklClient implements ApiClient {
    private final MyHttpClient httpService;
    private SimklHttpProcess process;

    public SimklClient() {
        process = new SimklHttpProcess();
        httpService = new MyHttpClient(this);
    }

    public String getName() {
        return "simkl";
    }

    public List<Post> getData() throws IOException, InterruptedException {
        return process.process(httpService);
    }

    public String getKey() {
        return System.getenv("SIMKL_CLIENT_ID");
    }

    public String getUrl() {
        return "https://api.simkl.com/movies/trending?client_id=" + getKey();
    }

}
