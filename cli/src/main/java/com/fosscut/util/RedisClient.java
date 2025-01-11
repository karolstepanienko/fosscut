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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private RedisConnectionSecrets redisConnectionSecrets;

    public RedisClient(File redisConnectionSecretsFile) {
        this.redisConnectionSecrets = loadRedisConnectionSecrets(redisConnectionSecretsFile);
    }

    public JedisPooled getReadClient(String hostname, Integer port) {
        HostAndPort address = new HostAndPort(hostname, port);
        JedisClientConfig config = getJedisClientConfig(redisConnectionSecrets.getPassword());
        return new JedisPooled(address, config);
    }

    public JedisPooled getWriteClient() {
        if (redisConnectionSecrets.getWriteHost() == null
        || redisConnectionSecrets.getWritePort() == null
        || redisConnectionSecrets.getWritePassword() == null) {
            return null;
        } else {
            HostAndPort address = new HostAndPort(redisConnectionSecrets.getWriteHost(), redisConnectionSecrets.getWritePort());
            JedisClientConfig config = getJedisClientConfig(redisConnectionSecrets.getWritePassword());
            return new JedisPooled(address, config);
        }
    }

    private SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory sslFactory = null;

        try {
            sslFactory = createSslSocketFactory(
                redisConnectionSecrets.getTruststorePath(),
                redisConnectionSecrets.getTruststorePassword(),
                redisConnectionSecrets.getKeystorePath(),
                redisConnectionSecrets.getKeystorePassword()
            );
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            System.exit(1);
        }

        return sslFactory;
    }

    private JedisClientConfig getJedisClientConfig(String password) {
        return DefaultJedisClientConfig.builder()
            .ssl(true).sslSocketFactory(getSSLSocketFactory())
            .password(password)
            .build();
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
            logger.error(e.getLocalizedMessage());
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
    private String writeHost;
    private Integer writePort;
    private String writePassword;

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

    public String getWriteHost() {
        return writeHost;
    }

    public Integer getWritePort() {
        return writePort;
    }

    public String getWritePassword() {
        return writePassword;
    }

}
