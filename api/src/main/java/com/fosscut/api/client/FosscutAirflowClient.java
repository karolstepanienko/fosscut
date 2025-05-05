package com.fosscut.api.client;

import java.time.Instant;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;

public class FosscutAirflowClient {

    @Value("${airflow.hostname}")
    private String hostname;

    @Value("${airflow.port}")
    private String port;

    @Value("${airflow.username}")
    private String username;

    @Value("${airflow.password}")
    private String password;

    private String basicAuth;
    private WebClient webClient;

    private static final String DAG_ID = "fosscut_generate_kubernetes_executor";
    private static final String TASK_ID = "fosscut_generate_kubernetes_executor_task_id";
    private static final String TASK_TRY_NUMBER = "1";

    public FosscutAirflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    private void init() {
        this.basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public String runFosscutGenerateDAG(String identifier) {
        String dagRunID = getDAGRunID(identifier);
        String body = getBodyJson(dagRunID);

        webClient.post()
                .uri(getUrl())
                .header("Authorization", getAuthHeader())
                .header("Content-Type","application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class).block();

        return dagRunID;
    }

    public String getDAGLogs(String dagRunID) {
        String logs = webClient.get()
                .uri(getUrl() + "/" + dagRunID + "/taskInstances/" + TASK_ID + "/logs/" + TASK_TRY_NUMBER)
                .header("Authorization", getAuthHeader())
                .retrieve()
                .bodyToMono(String.class).block();

        return logs;
    }

    ///////////////////////// String Helpers ///////////////////////////////////

    private String getUrl() {
        return "https://" + hostname + ":" + port + "/api/v1/dags/" + DAG_ID + "/dagRuns";
    }

    private String getAuthHeader() {
        return "Basic " + this.basicAuth;
    }

    private String getDAGRunID(String identifier) {
        String unixEpochMillisString = String.valueOf(System.currentTimeMillis());
        return "manual_run_" + identifier + "_" + unixEpochMillisString;
    }

    private String getBodyJson(String dagRunID) {
        return "{" +
            getDAGRunIDJson(dagRunID) +
            getLogicalDateJson() +
            getConfJson() +
        "}";
    }

    private String getDAGRunIDJson(String dagRunID) {
        return "\"dag_run_id\": \"" + dagRunID + "\",";
    }

    private String getLogicalDateJson() {
        return "\"logical_date\": \"" + getLogicalDate() + "\",";
    }

    private String getLogicalDate() {
        String timeStamp = Instant.now().toString();
        return timeStamp;
    }

    private String getConfJson() {
        return "\"conf\": {" +
            "\"subcommand\": \"cg\"," +
            "\"redis_url\": \"redis://redis-replicas.redis.svc.cluster.local:6379/example-order\"" +
        "}";
    }

}
