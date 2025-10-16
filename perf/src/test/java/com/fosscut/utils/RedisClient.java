package com.fosscut.utils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.fosscut.shared.SharedDefaults;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RedisClient {

    private OkHttpClient client;

    RedisClient() {
        try {
            this.client = getClient();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RedisClient", e);
        }
    }

    public String getOrder(String key) throws RuntimeException, IOException {
        return get("order", key);
    }

    public String getPlan(String key) throws RuntimeException, IOException {
        return get("plan", key);
    }

    private String get(String type, String key) throws RuntimeException, IOException {
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

    private OkHttpClient getClient() throws Exception {
        // --- Load the truststore ---
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(PerformanceDefaults.FOSSCUT_API_TRUSTSTORE_PATH)) {
            trustStore.load(fis, "password".toCharArray());
        }

        // --- Create TrustManagerFactory using the truststore ---
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        tmf.init(trustStore);

        // --- SSL context ---
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // --- Create OkHttp client ---
        return new OkHttpClient.Builder()
            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
            .hostnameVerifier((hostname, session) -> true) // ignore host verification
            .build();
    }

}
