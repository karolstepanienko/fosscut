package com.fosscut.compare.cicd;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fosscut.compare.cicd.jenkins.JenkinsCICDHttpClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CICDCompareJenkinsTest {

    private static String testName = "CICDCompareJenkinsTest";
    private static String RUN_ID = "1"; // Run is a set of data, managed manually
    private static int NUM_PARTS = 100; // Number of builds to create per run

    private JenkinsCICDHttpClient httpClient;
    private CICDUtils cicdUtils;
    private List<String> identifiers;

    @Test public void report() throws IOException {
        Instant startTimestamp = cicdUtils.loadStartTimestampFromFile();
        List<CICDReportLine> reportLines = httpClient.prepareReportLines(identifiers);
        cicdUtils.saveReport(new CICDReport(reportLines, startTimestamp));
    }

    @Test public void runJobs() {
        cicdUtils.saveStartTimestampToFile();
        identifiers.parallelStream().forEach( identifier -> {
            httpClient.runBuild(identifier);
        });
    }

    @Test public void removeJobs() {
        List<String> toDeleteBuildIds = httpClient.getToDeleteBuildIds(identifiers);
        toDeleteBuildIds.parallelStream().forEach( buildId ->
            httpClient.deleteBuild(buildId)
        );
    }

    @BeforeAll
    void setUp() {
        httpClient = new JenkinsCICDHttpClient();
        cicdUtils = new CICDUtils(testName, RUN_ID, NUM_PARTS);
        identifiers = cicdUtils.generateIdentifiers();
    }

}
