package com.kursach.Concurrency;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.kursach.ApiClient.ApiClient;
import com.kursach.cli.AggregationService;

public class PollingService implements AutoCloseable {
    private final List<ApiClient> clients;
    private final String format;
    private final int intervalSeconds;
    private final ScheduledThreadPoolExecutor executor;
    private final AggregationService aggregationService;
    private final AtomicInteger nextId;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public PollingService(List<ApiClient> clients, String format, boolean append, int maxConcurrentTasks, int intervalSeconds) throws IOException {
        if (clients == null || clients.isEmpty()) {
            throw new IllegalArgumentException("At least one API must be selected");
        }

        if (maxConcurrentTasks < 1) {
            throw new IllegalArgumentException("Max concurrent tasks must be greater than 0");
        }

        if (intervalSeconds < 1) {
            throw new IllegalArgumentException("Polling interval must be greater than 0");
        }

        this.clients = List.copyOf(clients);
        this.format = format;
        this.intervalSeconds = intervalSeconds;
        this.executor = new ScheduledThreadPoolExecutor(maxConcurrentTasks);
        this.executor.setRemoveOnCancelPolicy(true);
        this.aggregationService = new AggregationService();
        this.nextId = new AtomicInteger(aggregationService.prepareOutput(format, append));
    }

    public void start() {
        if (!running.compareAndSet(false, true)) {
            return;
        }

        for (ApiClient client : clients) {
            scheduleNext(client, 0);
        }
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) {
            return;
        }

        for (ScheduledFuture<?> task : scheduledTasks.values()) {
            task.cancel(true);
        }

        scheduledTasks.clear();
        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void awaitUntilStopped() throws InterruptedException {
        while (running.get()) {
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void close() {
        stop();
    }

    private void scheduleNext(ApiClient client, long delaySeconds) {
        if (!running.get()) {
            return;
        }

        ScheduledFuture<?> future = executor.schedule(() -> {
            try {
                int id = nextId.getAndIncrement();
                aggregationService.aggregateClient(client, format, id);
                System.out.println("API " + client.getName() + " polled with id " + id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println("Polling error for API " + client.getName() + ": " + e.getMessage());
            } finally {
                if (running.get()) {
                    scheduleNext(client, intervalSeconds);
                }
            }
        }, delaySeconds, TimeUnit.SECONDS);

        scheduledTasks.put(client.getName(), future);
    }
}
