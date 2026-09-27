package com.data_aggregator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_aggregator.ApiClient.ApiClient;
import com.data_aggregator.Concurrency.PollingService;

public class PollingServiceTest {
    private OutputFilesBackup outputFilesBackup;

    @BeforeEach
    public void setUp() throws Exception {
        outputFilesBackup = new OutputFilesBackup();
    }

    @AfterEach
    public void tearDown() throws Exception {
        outputFilesBackup.restore();
    }

    @Test
    public void pollingServiceRunsApiWithoutNetworkAndStopsExecutor() throws Exception {
        FakePollingApiClient client = new FakePollingApiClient(1);
        PollingService service = new PollingService(List.of(client), "json", false, 1, 1);

        service.start();
        assertTrue(client.awaitCalls());
        service.stop();

        assertFalse(service.isRunning());
        assertTrue(client.callCount() >= 1);
        assertTrue(Files.readString(Path.of("output.json")).contains("polling"));
    }

    @Test
    public void pollingServiceValidatesConstructorArguments() throws Exception {
        FakePollingApiClient client = new FakePollingApiClient(1);

        assertIllegalArgument(() -> new PollingService(List.of(), "json", false, 1, 1));
        assertIllegalArgument(() -> new PollingService(List.of(client), "json", false, 0, 1));
        assertIllegalArgument(() -> new PollingService(List.of(client), "json", false, 1, 0));
    }

    private void assertIllegalArgument(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
        } catch (IllegalArgumentException e) {
            return;
        }

        throw new AssertionError("IllegalArgumentException expected");
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static class FakePollingApiClient implements ApiClient {
        private final CountDownLatch latch;
        private final AtomicInteger calls = new AtomicInteger();

        FakePollingApiClient(int expectedCalls) {
            latch = new CountDownLatch(expectedCalls);
        }

        boolean awaitCalls() throws InterruptedException {
            return latch.await(3, TimeUnit.SECONDS);
        }

        int callCount() {
            return calls.get();
        }

        public String getName() {
            return "polling";
        }

        public String getKey() {
            return "";
        }

        public String getUrl() {
            return "https://example.test";
        }

        public List<?> getData() {
            calls.incrementAndGet();
            latch.countDown();
            return List.of(Map.of("value", "polling"));
        }

    }
}
