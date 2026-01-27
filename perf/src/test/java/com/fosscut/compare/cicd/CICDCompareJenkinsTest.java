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
    // RUN_ID: Run is a set of data, managed manually
    // NUM_PARTS: Number of builds to create per run

    private static String RUN_ID = "a"; private static int NUM_PARTS = 50; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 50; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 50; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 50; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 50; // DONE arch-beta + arch-gamma and cpu and memory request

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 100; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 100; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 100; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 100; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 100; // DONE arch-beta + arch-gamma and cpu and memory request

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 150; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 150; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 150; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 150; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 150; // DONE arch-beta + arch-gamma and cpu and memory request

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 200; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 200; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 200; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 200; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 200; // DONE arch-beta + arch-gamma and cpu and memory request

    // private static String RUN_ID = "a"; private static int NUM_PARTS = 250; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "b"; private static int NUM_PARTS = 250; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "c"; private static int NUM_PARTS = 250; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "d"; private static int NUM_PARTS = 250; // DONE arch-beta + arch-gamma and cpu and memory request
    // private static String RUN_ID = "e"; private static int NUM_PARTS = 250; // DONE arch-beta + arch-gamma and cpu and memory request

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
