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
                .uri("https://" + hostname + ":" + port + "/job/fosscut/buildWithParameters")
                .header("Authorization", getAuthHeader())
                .header("Content-Type","application/json")
                .bodyValue("subcommand=ffd&redis_url=redis://redis-replicas.redis.svc.cluster.local:6379/example-order")
                .retrieve().toBodilessEntity();
        return monoResponse.block().getHeaders().getFirst("location")
            .split("/queue/item/")[1].split("/")[0];
    }

    public JenkinsJobLogsDTO getJobLogs(String queueItemIdentifier, String jobNumberIdentifier) {
        String status = null;

        try {
            if (jobNumberIdentifier.isEmpty()) {
                // queue/item/9/api/json
                Map<String, Object> json = webClient.get()
                    .uri("https://" + hostname + ":" + port + "/queue/item/" + queueItemIdentifier + "/api/json")
                    .header("Authorization", getAuthHeader())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

                Object statusOrNull = json.get("why");
                if (statusOrNull != null) status = statusOrNull.toString();

                try {
                    Object executableOrNull = json.get("executable");
                    if (executableOrNull != null) {
                        String[] parts = executableOrNull.toString().split(" ");
                        for (String part : parts) {
                            if (part.contains("number")){
                                String numberPart= part.split("number=")[1];
                                jobNumberIdentifier = numberPart.substring(0, numberPart.length() - 1); // drop last char
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {}
            }

            String logs = webClient.get()
                    .uri("https://" + hostname + ":" + port + "/job/fosscut/" + jobNumberIdentifier + "/consoleText")
                    .header("Authorization", getAuthHeader())
                    .retrieve().bodyToMono(String.class).block();

            return new JenkinsJobLogsDTO(HttpServletResponse.SC_OK, jobNumberIdentifier, status, logs);
        } catch (WebClientResponseException.NotFound ex) {
            return new JenkinsJobLogsDTO(HttpServletResponse.SC_NOT_FOUND, jobNumberIdentifier, status, null);
        }
    }

    ///////////////////////// String Helpers ///////////////////////////////////

    private String getAuthHeader() {
        return "Basic " + this.basicAuth;
    }

}
