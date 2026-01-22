package com.fosscut.shared.util;

import java.util.Base64;

public class JenkinsSecrets {

    private String hostname;
    private String port;
    private String username;
    private String token;

    public JenkinsSecrets(String hostname, String port, String username,
        String token
    ) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.token = token;
    }

    public String getUrl() {
        return "https://" + hostname + ":" + port;
    }

    public String getAuthHeader() {
        return "Basic " + getBasicAuth();
    }

    // Helper methods

    private String getBasicAuth() {
        return Base64.getEncoder().encodeToString((username + ":" + token).getBytes());
    }

}
