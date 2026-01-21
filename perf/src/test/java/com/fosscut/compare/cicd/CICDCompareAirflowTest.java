package com.fosscut.compare.cicd;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fosscut.compare.cicd.airflow.AirflowCICDHttpClient;
import com.fosscut.compare.cicd.airflow.AirflowCICDUtils;
import com.google.common.util.concurrent.RateLimiter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CICDCompareAirflowTest {

    private static String testName = "CICDCompareAirflowTest";
    private static String RUN_ID = "1"; // Run is a set of data, managed manually
    private static int NUM_PARTS = 100; // Number of DAG runs to create per run

    private AirflowCICDHttpClient httpClient;
    private List<String> identifiers;

    @Test public void report() {
        CICDReport cicdReport = new CICDReport(httpClient.prepareReportLines(identifiers));
        cicdReport.saveReport(testName);
    }

    @Test public void runJobs() {
        RateLimiter limiter = RateLimiter.create(20.0); // 20 requests / second
        identifiers.parallelStream().forEach( identifier -> {
            limiter.acquire();
            httpClient.runDAG(identifier);
        });
    }

    @Test public void removeJobs() {
        List<String> toDeleteDagRunIds = httpClient.getToDeleteDagRunIds(identifiers);
        toDeleteDagRunIds.parallelStream().forEach( dagRunId ->
            httpClient.deleteDAG(dagRunId)
        );
        httpClient.cleanupLogs();
    }

    @BeforeAll
    void setUp() {
        httpClient = new AirflowCICDHttpClient();
        identifiers = new AirflowCICDUtils(RUN_ID, NUM_PARTS).generateIdentifiers();
    }

}
