package com.kursach.ApiClient;

import java.util.List;

public interface ApiClient {
    String getName();
    String getKey();
    String getUrl();
    List<?> getData() throws Exception;
}
