package com.fosscut.utils;

import com.fosscut.shared.SharedDefaults;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RedisClient {

    private OkHttpClient client;

    RedisClient() {
        try {
            FosscutInternalHttpClient httpClient = new FosscutInternalHttpClient();
            this.client = httpClient.getRedisClient();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RedisClient", e);
        }
    }

    public String getOrder(String key) throws RuntimeException, IOException {
        return getRedisKeyValue("order", key);
    }

    public String getPlan(String key) throws RuntimeException, IOException {
        return getRedisKeyValue("plan", key);
    }

    private String getRedisKeyValue(String type, String key) throws RuntimeException, IOException {
        Request request = new Request.Builder()
                .url(PerformanceDefaults.FOSSCUT_API_REDIS_URL + type)
                // okhttp is required for overriding the host header,
                // java.net.http.HttpClient does not support it
                .addHeader("Host", PerformanceDefaults.FOSSCUT_API_HOSTNAME)
                .addHeader("Cookie", SharedDefaults.COOKIE_IDENTIFIER + "=" + key)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to get key from Redis: "
                    + response.code() + " " + response.message());
            }
            return response.body().string();
        }
    }

}
