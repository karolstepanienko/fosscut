package com.fosscut.compare.cicd;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fosscut.compare.cicd.airflow.AirflowCICDHttpClient;
import com.google.common.util.concurrent.RateLimiter;

// NOT USED in fosscut-doc:
// airflow horizontally scaled setup did not change performance results significantly
// also triggering became harder and failed more often
// triggering 200 DAG runs often resulted in only about 185-190 actually starting and finishing
// and the rest was rejected by the overloaded scheduler
// Some scheduler pods also failed (cpu to slow, health probe failed) during
// the test runs which resulted in some DAG runs being in "failed" state
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CICDCompareAirflowHorizontallyScaledTest {

    private static String testName = "CICDCompareAirflowHorizontallyScaledTest";
    // RUN_ID: Run is a set of data, managed manually
    // NUM_PARTS: Number of DAG runs to create per run:

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 50; // TODO
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 50; // TODO
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 50; // TODO
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 50; // TODO
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 50; // DONE

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 100; // TODO
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 100; // TODO
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 100; // TODO
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 100; // TODO
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 100; // DONE

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 150; // TODO
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 150; // TODO
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 150; // TODO
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 150; // TODO
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 150; // DONE

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 200; // TODO
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 200; // TODO
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 200; // TODO
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 200; // TODO
    private static String RUN_ID = "e"; private static int NUM_PARTS = 200; // INCOMPLETE

    private AirflowCICDHttpClient httpClient;
    private CICDUtils cicdUtils;
    private List<String> identifiers;

    @Test public void report() throws IOException {
        Instant startTimestamp = cicdUtils.loadStartTimestampFromFile();
        List<CICDReportLine> reportLines = httpClient.prepareReportLines(identifiers);
        cicdUtils.saveReport(new CICDReport(reportLines, startTimestamp));
    }

    @Test public void runJobs() {
        cicdUtils.saveStartTimestampToFile();
        RateLimiter limiter = RateLimiter.create(25.0); // 25 requests / second
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
        cicdUtils = new CICDUtils(testName, RUN_ID, NUM_PARTS);
        identifiers = cicdUtils.generateIdentifiers();
    }

}
