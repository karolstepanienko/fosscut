package com.fosscut.compare.cicd.jenkins;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fosscut.compare.cicd.CICDHttpClient;
import com.fosscut.compare.cicd.CICDReportLine;
import com.fosscut.shared.util.JenkinsSecrets;
import com.fosscut.utils.FosscutInternalHttpClient;
import com.fosscut.utils.PerformanceDefaults;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class JenkinsCICDHttpClient extends CICDHttpClient {

    private JenkinsSecrets jenkinsSecrets;
    private ObjectMapper mapper;

    public JenkinsCICDHttpClient() {
        this.jenkinsSecrets = new JenkinsSecrets(
            PerformanceDefaults.CICD_PERFORMANCE_JENKINS_HOSTNAME,
            PerformanceDefaults.CICD_PERFORMANCE_JENKINS_PORT,
            PerformanceDefaults.CICD_PERFORMANCE_JENKINS_USERNAME,
            PerformanceDefaults.CICD_PERFORMANCE_JENKINS_API_TOKEN
        );
        this.mapper = new ObjectMapper();
        try {
            FosscutInternalHttpClient httpClient = new FosscutInternalHttpClient();
            this.client = httpClient.getAirflowOrJenkinsClient();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create JenkinsCICDHttpClient", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CICDReportLine> prepareReportLines(List<String> identifiers) {
        List<CICDReportLine> reportLines = new ArrayList<>();

        List<String> allBuildIds = getAllBuildIds();
        for (String buildId : allBuildIds) {
            String json = getBuildInfo(buildId);
            try {
                Map<String, Object> root = mapper.readValue(json, Map.class);
                Long duration = Long.valueOf(root.get("duration").toString());
                Long timestamp = Long.valueOf(root.get("timestamp").toString());
                List<Map<String, Object>> actions = (List<Map<String, Object>>) root.get("actions");
                Map<String, Object> paramAction = actions.get(0);
                List<Map<String, Object>> parameters = (List<Map<String, Object>>) paramAction.get("parameters");
                parameters.stream().forEach( parameter -> {
                    String name = String.valueOf(parameter.get("name"));
                    if (name.equals("IDENTIFIER")) {
                        String value = String.valueOf(parameter.get("value"));
                        if (identifiers.contains(value)) {
                            Map<String, Object> timeInQueueAction = actions.get(2);
                            // Waiting for scheduler's decision
                            Long waitingDurationMillis = Long.valueOf(timeInQueueAction.get("waitingDurationMillis").toString());
                            Long waitingTimeMillis = Long.valueOf(timeInQueueAction.get("waitingTimeMillis").toString());
                            assertEquals(waitingDurationMillis, waitingTimeMillis);
                            // Blocked due to concurrency limits
                            Long blockedDurationMillis = Long.valueOf(timeInQueueAction.get("blockedDurationMillis").toString());
                            Long blockedTimeMillis = Long.valueOf(timeInQueueAction.get("blockedTimeMillis").toString());
                            assertEquals(blockedDurationMillis, blockedTimeMillis);
                            assertEquals(blockedDurationMillis, 0L);
                            // Pod creation time
                            Long buildableDurationMillis = Long.valueOf(timeInQueueAction.get("buildableDurationMillis").toString());
                            Long buildableTimeMillis = Long.valueOf(timeInQueueAction.get("buildableTimeMillis").toString());
                            assertEquals(buildableDurationMillis, buildableTimeMillis);
                            // Actual build time
                            Long buildingDurationMillis = Long.valueOf(timeInQueueAction.get("buildingDurationMillis").toString());
                            Long executingTimeMillis = Long.valueOf(timeInQueueAction.get("executingTimeMillis").toString());
                            assertEquals(buildingDurationMillis, executingTimeMillis);
                            assertEquals(buildingDurationMillis, duration);

                            Instant creationTimestamp = Instant.ofEpochMilli(timestamp);
                            Instant completionTimestamp = creationTimestamp.plusMillis(
                                waitingDurationMillis
                                + blockedDurationMillis
                                + buildableDurationMillis
                                + buildingDurationMillis
                            );

                            reportLines.add(
                                new CICDReportLine(
                                    value,
                                    creationTimestamp,
                                    completionTimestamp
                                )
                            );
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse Jenkins build JSON", e);
            }
        }

        return reportLines;
    }

    private void assertEquals(Long i1, Long i2) {
        if (!i1.equals(i2)) {
            throw new RuntimeException("Assertion failed: " + i1 + " != " + i2);
        }
    }

    public void runBuild(String identifier) {
        Request request = new Request.Builder()
                .url(jenkinsSecrets.getUrl() + "/job/"
                    + PerformanceDefaults.CICD_PERFORMANCE_JENKINS_JOB
                    + "/buildWithParameters?IDENTIFIER=" + identifier)
                .post(RequestBody.create("", MediaType.get("application/json")))
                .header("Authorization", jenkinsSecrets.getAuthHeader())
                .header("Content-Type", "application/json")
                .build();

        executeApiCall(request, "Failed to trigger Jenkins build for identifier: " + identifier);
    }

    public void deleteBuild(String buildId) {
        Request request = new Request.Builder()
                .url(jenkinsSecrets.getUrl() + "/job/"
                    + PerformanceDefaults.CICD_PERFORMANCE_JENKINS_JOB
                    + "/" + buildId + "/doDelete")
                .post(RequestBody.create("", MediaType.get("application/json")))
                .header("Authorization", jenkinsSecrets.getAuthHeader())
                .header("Content-Type", "application/json")
                .build();

        executeApiCall(request, "Failed to delete Jenkins build for identifier: " + buildId);
    }

    @SuppressWarnings("unchecked")
    public List<String> getToDeleteBuildIds(List<String> identifiers) {
        List<String> toDeleteBuildIds = new ArrayList<>();

        List<String> allBuildIds = getAllBuildIds();
        for (String buildId : allBuildIds) {
            String json = getBuildInfo(buildId);
            try {
                Map<String, Object> root = mapper.readValue(json, Map.class);
                List<Map<String, Object>> actions = (List<Map<String, Object>>) root.get("actions");
                Map<String, Object> action = actions.get(0);
                List<Map<String, Object>> parameters = (List<Map<String, Object>>) action.get("parameters");
                parameters.stream().forEach( parameter -> {
                    String name = String.valueOf(parameter.get("name"));
                    if (name.equals("IDENTIFIER")) {
                        String value = String.valueOf(parameter.get("value"));
                        if (identifiers.contains(value))
                            toDeleteBuildIds.add(buildId);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse Jenkins build JSON", e);
            }
        }

        return toDeleteBuildIds;
    }

    private String getBuildInfo(String buildId) {
        Request request = new Request.Builder()
                .url(jenkinsSecrets.getUrl() + "/job/"
                    + PerformanceDefaults.CICD_PERFORMANCE_JENKINS_JOB
                    + "/" + buildId + "/api/json")
                .get()
                .header("Authorization", jenkinsSecrets.getAuthHeader())
                .header("Content-Type", "application/json")
                .build();

        return executeApiCall(request, "Failed to get Jenkins build info for buildId: " + buildId);
    }

    @SuppressWarnings("unchecked")
    private List<String> getAllBuildIds() {
        List<String> allBuildIds = new ArrayList<>();

        Request request = new Request.Builder()
                .url(jenkinsSecrets.getUrl() + "/job/"
                    + PerformanceDefaults.CICD_PERFORMANCE_JENKINS_JOB
                    + "/api/json")
                .get()
                .header("Authorization", jenkinsSecrets.getAuthHeader())
                .header("Content-Type", "application/json")
                .build();

        String json = executeApiCall(request, "Failed to get Jenkins builds");

        try {
            Map<String, Object> root = mapper.readValue(json, Map.class);
            List<Map<String, Object>> builds = (List<Map<String, Object>>) root.get("builds");
            builds.stream().forEach( build -> {
                allBuildIds.add(String.valueOf(build.get("number")));
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Airflow DAGs JSON", e);
        }

        return allBuildIds;
    }

}
