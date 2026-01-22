package com.fosscut.utils;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class FosscutInternalHttpClient {

    public OkHttpClient getAirflowOrJenkinsClient() throws Exception {
        TrustManagerFactory tmf = buildTrustManagerFactory(PerformanceDefaults.FOSSCUT_INTERNAL_TRUSTSTORE_PATH);
        SSLContext sslContext = createSslContext(tmf);

        return new OkHttpClient.Builder()
            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
            .build();
    }

    public OkHttpClient getRedisClient() throws Exception {
        TrustManagerFactory tmf = buildTrustManagerFactory(PerformanceDefaults.FOSSCUT_API_TRUSTSTORE_PATH);
        SSLContext sslContext = createSslContext(tmf);

        return new OkHttpClient.Builder()
            .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) tmf.getTrustManagers()[0])
            .hostnameVerifier((hostname, session) -> true) // ignore host verification
            .build();
    }

    private TrustManagerFactory buildTrustManagerFactory(String truststorePath) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(truststorePath)) {
            trustStore.load(fis, PerformanceDefaults.FOSSCUT_API_TRUSTSTORE_PASSWORD.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        );
        tmf.init(trustStore);
        return tmf;
    }

    private SSLContext createSslContext(TrustManagerFactory tmf) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

}