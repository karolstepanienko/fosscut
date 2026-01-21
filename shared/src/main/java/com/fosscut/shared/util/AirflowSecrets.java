package com.fosscut.shared.util;

import java.time.Instant;
import java.util.Base64;

public class AirflowSecrets {

    private String hostname;
    private String port;
    private String username;
    private String password;
    private String dagId;
    private String cleanupDagId;

    public AirflowSecrets(String hostname, String port, String username,
        String password, String dagId, String cleanupDagId
    ) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.dagId = dagId;
        this.cleanupDagId = cleanupDagId;
    }

    public String getUrl() {
        return "https://" + hostname + ":" + port +
            "/api/v1/dags/" + dagId + "/dagRuns";
    }

    public String getCleanupUrl() {
        return "https://" + hostname + ":" + port +
            "/api/v1/dags/" + cleanupDagId + "/dagRuns";
    }

    public String getAuthHeader() {
        return "Basic " + getBasicAuth();
    }

    public String getDAGRunID(String identifier) {
        String unixEpochMillisString = String.valueOf(System.currentTimeMillis());
        return identifier + "_" + unixEpochMillisString;
    }

    public String getDAGRunIDJson(String dagRunID) {
        return "\"dag_run_id\": \"" + dagRunID + "\",";
    }

    public String getLogicalDateJson() {
        return "\"logical_date\": \"" + getLogicalDate() + "\",";
    }

    public String getBodyJson(String dagRunID) {
        return "{" +
            getDAGRunIDJson(dagRunID) +
            getLogicalDateJson() +
            "\"conf\": {}" +
        "}";
    }

    public String getMinimalBodyJson() {
        return "{" +
            "\"conf\": {}" +
        "}";
    }

    // Helper methods

    private String getBasicAuth() {
        return Base64.getEncoder()
            .encodeToString((username + ":" + password).getBytes());
    }

    private String getLogicalDate() {
        String timeStamp = Instant.now().toString();
        return timeStamp;
    }

}
