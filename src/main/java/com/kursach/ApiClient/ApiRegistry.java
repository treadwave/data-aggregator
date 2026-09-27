package com.kursach.ApiClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kursach.util.ClasspathScanner;

public class ApiRegistry {
    private static final String BASE_PACKAGE = "com.kursach.ApiClient";
    private final ClasspathScanner classpathScanner = new ClasspathScanner();

    public List<ApiClient> getAvailableApis() {
        return classpathScanner.findImplementations(BASE_PACKAGE, ApiClient.class);
    }

    public List<ApiClient> getClientsByNames(List<String> names) {
        Map<String, ApiClient> clientsByName = new LinkedHashMap<>();

        for (ApiClient client : getAvailableApis()) {
            clientsByName.put(client.getName().toLowerCase(), client);
        }

        return names.stream().map(name -> getClientByName(name, clientsByName)).toList();
    }

    private ApiClient getClientByName(String name, Map<String, ApiClient> clientsByName) {
        String normalizedName = name.trim().toLowerCase();
        ApiClient client = clientsByName.get(normalizedName);

        if (client == null) {
            throw new IllegalArgumentException(
                "Unknown API: " + name + ". Available APIs: " + String.join(", ", clientsByName.keySet())
            );
        }

        return client;
    }
}