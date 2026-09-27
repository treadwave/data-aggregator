package com.kursach.cli;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.kursach.ApiClient.ApiClient;
import com.kursach.ApiClient.ApiRegistry;
import com.kursach.Concurrency.PollingService;

public class Interactive {
    private final Scanner scanner = new Scanner(System.in);
    private final ApiRegistry apiRegistry = new ApiRegistry();

    public void run() throws Exception {
        List<ApiClient> availableApis = apiRegistry.getAvailableApis();

        if (availableApis.isEmpty()) {
            System.out.println("Нет доступных API");
            return;
        }

        System.out.println("Выберите API:");
        for (int i = 0; i < availableApis.size(); i++) {
            System.out.println((i + 1) + " - " + availableApis.get(i).getName());
        }

        int allApisChoice = availableApis.size() + 1;
        System.out.println(allApisChoice + " - все");

        int apiChoice = readInt();
        if (apiChoice < 1 || apiChoice > allApisChoice) {
            System.out.println("Неверный выбор API");
            return;
        }

        List<String> apis = new ArrayList<>();
        if (apiChoice == allApisChoice) {
            for (ApiClient client : availableApis) {
                apis.add(client.getName());
            }
        } else {
            apis.add(availableApis.get(apiChoice - 1).getName());
        }

        System.out.println("Формат файла:");
        System.out.println("1 - json");
        System.out.println("2 - csv");

        int formatChoice = readInt();

        String format = switch (formatChoice) {
            case 1 -> "json";
            case 2 -> "csv";
            default -> null;
        };

        if (format == null) {
            System.out.println("Неверный формат");
            return;
        }

        System.out.println("Режим файла:");
        System.out.println("1 - создать новый");
        System.out.println("2 - дозаписать");

        int fileModeChoice = readInt();
        if (fileModeChoice != 1 && fileModeChoice != 2) {
            System.out.println("Неверный режим файла");
            return;
        }

        boolean append = fileModeChoice == 2;

        System.out.println("Режим работы:");
        System.out.println("1 - выполнить один раз");
        System.out.println("2 - периодический опрос");

        int modeChoice = readInt();
        if (modeChoice == 1) {
            new AggregationService().run(apis, format, append);
        } else if (modeChoice == 2) {
            runPollingMenu(apis, format, append);
        } else {
            System.out.println("Неверный режим работы");
        }
    }

    private int readInt() {
        if (!scanner.hasNextInt()) {
            scanner.nextLine();
            return -1;
        }

        return scanner.nextInt();
    }

    private void runPollingMenu(List<String> apis, String format, boolean append) throws Exception {
        System.out.println("Максимальное количество одновременно выполняемых задач n:");
        int maxConcurrentTasks = readPositiveInt();

        System.out.println("Интервал опроса источников t в секундах:");
        int intervalSeconds = readPositiveInt();

        List<ApiClient> clients = apiRegistry.getClientsByNames(apis);
        AtomicReference<PollingService> pollingServiceRef = new AtomicReference<>();
        Thread shutdownHook = new Thread(() -> stopPolling(pollingServiceRef));
        boolean outputInitialized = false;

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try {
            while (true) {
                System.out.println("Управление опросом:");
                System.out.println("1 - запустить опрос");
                System.out.println("2 - остановить опрос");
                System.out.println("3 - выйти");

                int choice = readInt();

                if (choice == 1) {
                    PollingService currentService = pollingServiceRef.get();
                    if (currentService != null && currentService.isRunning()) {
                        System.out.println("Опрос уже запущен");
                        continue;
                    }

                    boolean serviceAppend = append || outputInitialized;
                    PollingService pollingService = new PollingService(clients, format, serviceAppend, maxConcurrentTasks, intervalSeconds);

                    pollingServiceRef.set(pollingService);
                    pollingService.start();
                    outputInitialized = true;
                    System.out.println("Опрос запущен");
                } else if (choice == 2) {
                    stopPolling(pollingServiceRef);
                    System.out.println("Опрос остановлен");
                } else if (choice == 3) {
                    stopPolling(pollingServiceRef);
                    return;
                } else {
                    System.out.println("Неверная команда");
                }
            }
        } finally {
            stopPolling(pollingServiceRef);
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    private int readPositiveInt() {
        int value = readInt();
        if (value < 1) {
            throw new IllegalArgumentException("Значение должно быть больше 0");
        }

        return value;
    }

    private void stopPolling(AtomicReference<PollingService> pollingServiceRef) {
        PollingService pollingService = pollingServiceRef.getAndSet(null);
        if (pollingService != null) {
            pollingService.stop();
        }
    }
}
