package com.fosscut.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

public class RedisClient {

    private RedisConnectionSecrets redisConnectionSecrets;

    public RedisClient(File redisConnectionSecretsFile) {
        this.redisConnectionSecrets = loadRedisConnectionSecrets(redisConnectionSecretsFile);
    }

    public JedisPooled getClient(String hostname, Integer port) {
        HostAndPort address = new HostAndPort(hostname, port);

        SSLSocketFactory sslFactory = null;
        try {
            sslFactory = createSslSocketFactory(
                redisConnectionSecrets.getTruststorePath(),
                redisConnectionSecrets.getTruststorePassword(),
                redisConnectionSecrets.getKeystorePath(),
                redisConnectionSecrets.getKeystorePassword()
            );
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }

        JedisClientConfig config = DefaultJedisClientConfig.builder()
            .ssl(true).sslSocketFactory(sslFactory)
            .password(redisConnectionSecrets.getPassword())
            .build();

        return new JedisPooled(address, config);
    }

    private RedisConnectionSecrets loadRedisConnectionSecrets(File redisConnectionSecretsFile) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        RedisConnectionSecrets redisConnectionSecrets = new RedisConnectionSecrets();
        try {
            redisConnectionSecrets = yamlMapper.readValue(
                redisConnectionSecretsFile,
             RedisConnectionSecrets.class);
        }
        catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
        return redisConnectionSecrets;
    }

    private static SSLSocketFactory createSslSocketFactory(
        String caCertPath, String caCertPassword, String userCertPath,
        String userCertPassword) throws IOException, GeneralSecurityException {

        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(new FileInputStream(userCertPath), userCertPassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance("pkcs12");
        trustStore.load(new FileInputStream(caCertPath), caCertPassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
        trustManagerFactory.init(trustStore);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
        keyManagerFactory.init(keyStore, userCertPassword.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }

}

class RedisConnectionSecrets {
    private String password;
    private String truststorePath;
    private String truststorePassword;
    private String keystorePath;
    private String keystorePassword;

    public String getPassword() {
        return password;
    }

    public String getTruststorePath() {
        return truststorePath;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }
}
