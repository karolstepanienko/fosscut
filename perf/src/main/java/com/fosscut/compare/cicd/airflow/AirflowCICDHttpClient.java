package com.fosscut.compare.cicd.airflow;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fosscut.compare.cicd.CICDHttpClient;
import com.fosscut.compare.cicd.CICDReportLine;
import com.fosscut.shared.util.AirflowSecrets;
import com.fosscut.utils.FosscutInternalHttpClient;
import com.fosscut.utils.PerformanceDefaults;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AirflowCICDHttpClient extends CICDHttpClient {

    private AirflowSecrets airflowSecrets;

    public AirflowCICDHttpClient() {
        this.airflowSecrets = new AirflowSecrets(
            PerformanceDefaults.CICD_PERFORMANCE_AIRFLOW_HOSTNAME,
            PerformanceDefaults.CICD_PERFORMANCE_AIRFLOW_PORT,
            PerformanceDefaults.CICD_PERFORMANCE_AIRFLOW_USERNAME,
            PerformanceDefaults.CICD_PERFORMANCE_AIRFLOW_PASSWORD,
            PerformanceDefaults.CICD_PERFORMANCE_AIRFLOW_DAG_ID,
            PerformanceDefaults.CICD_PERFORMANCE_AIRFLOW_CLEANUP_DAG_ID
        );
        try {
            FosscutInternalHttpClient httpClient = new FosscutInternalHttpClient();
            this.client = httpClient.getAirflowOrJenkinsClient();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create AirflowCICDHttpClient", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<CICDReportLine> prepareReportLines(List<String> identifiers) {
        List<CICDReportLine> reportLines = new ArrayList<>();

        String json = getDAGs(airflowSecrets.getUrl());
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> root = mapper.readValue(json, Map.class);
            List<Map<String, Object>> dagRuns = (List<Map<String, Object>>) root.get("dag_runs");
            for (Map<String, Object> dagRun : dagRuns) {
                String state = (String) dagRun.get("state");
                String dagRunId = (String) dagRun.get("dag_run_id");
                if ((state.equals("success") || state.equals("failed"))
                    && dagRunStartsWithIdentifier(dagRunId, identifiers)) {
                    // used as creationTimestamp
                    String logicalDate = (String) dagRun.get("logical_date");
                    // both below used as completionTimestamp, whichever will be later
                    String endDate = (String) dagRun.get("end_date");
                    String lastSchedulingDecision = (String) dagRun.get("last_scheduling_decision");

                    Instant creationTimestamp = Instant.parse(logicalDate);
                    Instant completionTimestampEndDate = Instant.parse(endDate);
                    Instant completionTimestampLastSchedulingDecision = Instant.parse(lastSchedulingDecision);
                    Instant completionTimestamp = completionTimestampEndDate.isAfter(completionTimestampLastSchedulingDecision)
                        ? completionTimestampEndDate
                        : completionTimestampLastSchedulingDecision;

                    reportLines.add(new CICDReportLine(
                        dagRunId,
                        creationTimestamp,
                        completionTimestamp
                    ));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Airflow DAGs JSON", e);
        }
        return reportLines;
    }

    public void runDAG(String identifier) throws RuntimeException {
        runDAGRun(
            airflowSecrets.getUrl(),
            airflowSecrets.getBodyJson(identifier),
            "Failed to trigger Airflow DAG for identifier: " + identifier
        );
    }

    public List<String> getToDeleteDagRunIds(List<String> identifiers) {
        String jsonString = getDAGs(airflowSecrets.getUrl());
        List<String> dagRunIds = extractDagRunIds(jsonString);
        return filterToDeleteDagRunIds(identifiers, dagRunIds);
    }

    public void deleteDAG(String dagRunId) {
        deleteDAGRun(airflowSecrets.getUrl(), dagRunId);
    }

    public void cleanupLogs() throws RuntimeException {
        // delete existing DAG runs for cleanup DAG
        String jsonString = getDAGs(airflowSecrets.getCleanupUrl());
        List<String> dagRunIds = extractDagRunIds(jsonString);
        for (String dagRunId : dagRunIds) {
            deleteDAGRun(airflowSecrets.getCleanupUrl(), dagRunId);
        }

        // trigger a cleanup task through Airflow API
        runDAGRun(airflowSecrets.getCleanupUrl(), airflowSecrets.getMinimalBodyJson(), "Failed to trigger Airflow cleanup task");
    }

    private void runDAGRun(String url, String bodyJson, String errorMessage) throws RuntimeException {
        RequestBody body = RequestBody.create(
            bodyJson,
            MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", airflowSecrets.getAuthHeader())
                .header("Content-Type", "application/json")
                .build();

        executeApiCall(request, errorMessage);
    }

    private void deleteDAGRun(String url, String dagRunId) throws RuntimeException {
        Request request = new Request.Builder()
                .url(url + "/" + dagRunId)
                .delete()
                .header("Authorization", airflowSecrets.getAuthHeader())
                .build();
        try (Response response = client.newCall(request).execute()) {
            // allow 404 as the DAG run may have been already deleted
            if (!response.isSuccessful() && response.code() != 404) {
                throw new RuntimeException("Failed to delete Airflow DAG run: " + dagRunId + ": "
                    + response.code() + " " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed Airflow API call", e);
        }
    }

    private String getDAGs(String url) throws RuntimeException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", airflowSecrets.getAuthHeader())
                .build();
        return executeApiCall(request, "Failed to get Airflow DAGs");
    }

    @SuppressWarnings("unchecked")
    private List<String> extractDagRunIds(String json) {
        List<String> toDeleteDagRunIds = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> root = mapper.readValue(json, Map.class);
            List<Map<String, Object>> dagRuns =
                (List<Map<String, Object>>) root.get("dag_runs");
            for (Map<String, Object> dagRun : dagRuns) {
                String dagRunId = (String) dagRun.get("dag_run_id");
                String state = (String) dagRun.get("state");
                if (state.equals("success") || state.equals("failed")) {
                    toDeleteDagRunIds.add(dagRunId);
                }
            }
            return toDeleteDagRunIds;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Airflow DAGs JSON", e);
        }
    }

    private List<String> filterToDeleteDagRunIds(List<String> identifiers, List<String> dagRunIds) {
        List<String> filteredDagRunIds = new ArrayList<>();
        for (String identifier : identifiers) {
            for (String dagRunId : dagRunIds) {
                // filter dag run ids by identifier prefix
                if (dagRunId.startsWith(identifier)) {
                    filteredDagRunIds.add(dagRunId);
                }
            }
        }
        return filteredDagRunIds;
    }

    private boolean dagRunStartsWithIdentifier(String dagRunId, List<String> identifiers) {
        for (String identifier : identifiers) {
            if (dagRunId.startsWith(identifier)) {
                return true;
            }
        }
        return false;
    }

}
