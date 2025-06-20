package com.fosscut.api.client;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fosscut.api.type.JenkinsJobLogsDTO;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

public class FosscutJenkinsClient {

    @Value("${jenkins.hostname}")
    private String hostname;

    @Value("${jenkins.port}")
    private String port;

    @Value("${jenkins.username}")
    private String username;

    @Value("${jenkins.token}")
    private String token;

    private String basicAuth;
    private WebClient webClient;

    public FosscutJenkinsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostConstruct
    private void init() {
        this.basicAuth = Base64.getEncoder().encodeToString((username + ":" + token).getBytes());
    }

    @SuppressWarnings("null")
    public String triggerJob() {
        // returns HTTP code and headers, body is empty
        Mono<ResponseEntity<Void>> monoResponse = webClient.post()
                .uri(getUrl() + "/job/fosscut/buildWithParameters")
                .header("Authorization", getAuthHeader())
                .header("Content-Type","application/json")
                .bodyValue("subcommand=ffd&redis_url=redis://redis-replicas.redis.svc.cluster.local:6379/example-order")
                .retrieve().toBodilessEntity();
        return monoResponse.block().getHeaders().getFirst("location")
            .split("/queue/item/")[1].split("/")[0];
    }

    public JenkinsJobLogsDTO getJobLogs(Integer queueItemIdentifier, Integer jobNumberIdentifier) {
        Integer httpStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String status = null;
        String building = null;
        String result = null;
        String logs = null;

        try {
            if (jobNumberIdentifier == null || jobNumberIdentifier <= 0) {
                Map<String, Object> json = getResponseSpec("/queue/item/" + queueItemIdentifier + "/api/json")
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

                status = extractValueFromJson(json, "why");
                jobNumberIdentifier = extractJobNumberIdentifier(json, jobNumberIdentifier);
                httpStatusCode = HttpServletResponse.SC_ACCEPTED;
            } else {
                Map<String, Object> json = getResponseSpec("/job/fosscut/" + jobNumberIdentifier + "/api/json")
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

                building = extractValueFromJson(json, "building");
                result = extractValueFromJson(json, "result");

                logs = getResponseSpec("/job/fosscut/" + jobNumberIdentifier + "/consoleText")
                    .bodyToMono(String.class)
                    .block();

                httpStatusCode = HttpServletResponse.SC_OK;
            }
        } catch (WebClientResponseException.NotFound | IndexOutOfBoundsException ex) {
            httpStatusCode = HttpServletResponse.SC_ACCEPTED;
        }

        return new JenkinsJobLogsDTO(httpStatusCode, jobNumberIdentifier, status, building, result, logs);
    }

    /////////////////////////// Web Helper /////////////////////////////////////

    private WebClient.ResponseSpec getResponseSpec(String uri) {
        return webClient.get()
                .uri(getUrl() + uri)
                .header("Authorization", getAuthHeader())
                .retrieve();
    }

    ///////////////////////// String Helpers ///////////////////////////////////

    private String extractValueFromJson(Map<String, Object> json, String key) {
        Object value = json.get(key);
        return value != null ? value.toString() : null;
    }

    private Integer extractJobNumberIdentifier(Map<String, Object> json, Integer jobNumberIdentifier)
    throws IndexOutOfBoundsException {
        Object executableOrNull = json.get("executable");
        if (executableOrNull != null) {
            String[] parts = executableOrNull.toString().split(" ");
            for (String part : parts) {
                if (part.contains("number")) {
                    String numberPart = part.split("number=")[1];
                    jobNumberIdentifier = Integer.valueOf(numberPart.substring(0, numberPart.length() - 1)); // drop last char
                }
            }
        }

        return jobNumberIdentifier;
    }

    private String getAuthHeader() {
        return "Basic " + this.basicAuth;
    }

    private String getUrl() {
        return "https://" + hostname + ":" + port;
    }

}
