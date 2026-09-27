package com.kursach.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kursach.ApiClient.ApiClient;
import com.kursach.ApiClient.ApiRegistry;
import com.kursach.Concurrency.PollingService;

public class Automatic {

    public void run(String[] args) throws Exception {
        List<String> apis = new ArrayList<>();
        String format = "json";
        boolean append = false;
        boolean polling = false;
        int maxConcurrentTasks = 1;
        int intervalSeconds = 60;

        for (String arg : args) {
            if (arg.startsWith("--apis=")) {
                String value = arg.substring("--apis=".length());
                apis = Arrays.stream(value.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .filter(api -> !api.isBlank())
                    .toList();
            } else if (arg.startsWith("--format=")) {
                format = arg.substring("--format=".length()).trim().toLowerCase();
            } else if (arg.startsWith("--append=")) {
                append = Boolean.parseBoolean(arg.substring("--append=".length()));
            } else if (arg.startsWith("--polling=")) {
                polling = Boolean.parseBoolean(arg.substring("--polling=".length()));
            } else if (arg.startsWith("--threads=")) {
                maxConcurrentTasks = parsePositiveInt(arg.substring("--threads=".length()), "threads");
            } else if (arg.startsWith("--n=")) {
                maxConcurrentTasks = parsePositiveInt(arg.substring("--n=".length()), "n");
            } else if (arg.startsWith("--interval=")) {
                intervalSeconds = parsePositiveInt(arg.substring("--interval=".length()), "interval");
            } else if (arg.startsWith("--t=")) {
                intervalSeconds = parsePositiveInt(arg.substring("--t=".length()), "t");
            }
        }

        if (apis.isEmpty()) {
            throw new IllegalArgumentException("No api mentioned");
        }

        if (!format.equals("json") && !format.equals("csv")) {
            throw new IllegalArgumentException("Unknown format: " + format + ". Available formats: json, csv");
        }

        if (polling) {
            runPolling(apis, format, append, maxConcurrentTasks, intervalSeconds);
        } else {
            new AggregationService().run(apis, format, append);
        }
    }

    private void runPolling(
        List<String> apis,
        String format,
        boolean append,
        int maxConcurrentTasks,
        int intervalSeconds
    ) throws Exception {
        List<ApiClient> clients = new ApiRegistry().getClientsByNames(apis);
        PollingService pollingService = new PollingService(clients, format, append, maxConcurrentTasks, intervalSeconds);
        Thread shutdownHook = new Thread(pollingService::stop);

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try {
            pollingService.start();
            System.out.println("Polling started. Press Ctrl+C to stop.");
            pollingService.awaitUntilStopped();
        } finally {
            pollingService.stop();
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    private int parsePositiveInt(String value, String parameterName) {
        try {
            int result = Integer.parseInt(value.trim());
            if (result < 1) {
                throw new IllegalArgumentException(parameterName + " must be greater than 0");
            }

            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(parameterName + " must be a number", e);
        }
    }
}
