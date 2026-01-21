package com.fosscut.api.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.fosscut.api.type.AirflowDAGLogsDTO;
import com.fosscut.api.type.Settings;
import com.fosscut.shared.util.AirflowSecrets;

import jakarta.annotation.PostConstruct;

public class FosscutAirflowClient extends AbstractClient {

    @Value("${airflow.hostname}")
    private String hostname;

    @Value("${airflow.port}")
    private String port;

    @Value("${airflow.username}")
    private String username;

    @Value("${airflow.password}")
    private String password;

    private AirflowSecrets airflowSecrets;
    private WebClient webClient;

    private static final String DAG_ID = "fosscut_generate_kubernetes_executor";
    private static final String TASK_ID = "fosscut_generate_kubernetes_executor_task_id";

    public FosscutAirflowClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    private void init() {
        this.airflowSecrets = new AirflowSecrets(hostname, port, username, password, DAG_ID, "");
    }

    public String runFosscutGenerateDAG(String identifier, Settings settings) {
        String dagRunID = airflowSecrets.getDAGRunID(identifier);
        String body = getBodyJson(dagRunID, settings);

        webClient.post()
                .uri(airflowSecrets.getUrl())
                .header("Authorization", airflowSecrets.getAuthHeader())
                .header("Content-Type","application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class).block();

        return dagRunID;
    }

    private Map<String, Object> getTaskDetails(String dagRunID) {
        Map<String, Object> taskDetails = webClient.get()
                .uri(airflowSecrets.getUrl() + "/" + dagRunID + "/taskInstances/" + TASK_ID)
                .header("Authorization", airflowSecrets.getAuthHeader())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return taskDetails;
    }

    private String getDAGLogsString(String dagRunID, Integer taskTryNumber) {
        String logs = webClient.get()
                .uri(airflowSecrets.getUrl() + "/" + dagRunID + "/taskInstances/" + TASK_ID + "/logs/" + taskTryNumber.toString())
                .header("Authorization", airflowSecrets.getAuthHeader())
                .retrieve()
                .bodyToMono(String.class).block();

        return logs;
    }

    public AirflowDAGLogsDTO getDAGLogs(String dagRunID) {
        Map<String, Object> taskDetails = getTaskDetails(dagRunID);
        String status = (String) taskDetails.get("state");
        Integer taskTryNumber = (Integer) taskDetails.get("try_number");
        String logs = getDAGLogsString(dagRunID, taskTryNumber);

        return new AirflowDAGLogsDTO(status, logs);
    }

    ///////////////////////// String Helpers ///////////////////////////////////

    private String getBodyJson(String dagRunID, Settings settings) {
        return "{" +
            airflowSecrets.getDAGRunIDJson(dagRunID) +
            airflowSecrets.getLogicalDateJson() +
            settings.toAirflowParamsString(redisReadHost, redisReadPort) +
        "}";
    }

}
