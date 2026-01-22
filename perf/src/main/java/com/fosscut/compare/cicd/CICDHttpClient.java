package com.fosscut.compare.cicd;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CICDHttpClient {

    protected OkHttpClient client;

    protected String executeApiCall(Request request, String errorMessage) {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException(errorMessage + ": "
                    + response.code() + " " + response.message());
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("Failed Airflow API call", e);
        }
    }

}
