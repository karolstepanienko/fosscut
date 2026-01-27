package com.fosscut.compare.cicd;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fosscut.compare.cicd.tekton.TektonCICDUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CICDCompareTektonTest {
    // DONE - run x tekton task runs
    // DONE - measure when each one starts and ends
    // DONE - should be able to run multiple such tasks in parallel
    // DONE - step for triggering task runs should be separate from step for measuring their status
    // DONE - create a report file that contains the list of all runs INPROGRESS/COMPLETED/FAILED with their start and end times
    // MAYBE - should be prepared for k8s to reject api calls when overloaded and retry them after some time, rate limiting

    private static String testName = "CICDCompareTektonTest";
    // RUN_ID: Run is a set of data, managed manually
    // NUM_PARTS: Number of task runs to create per run
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

    private TektonCICDUtils tektonCICDUtils;
    private CICDUtils cicdUtils;
    private List<String> identifiers;

    @Test public void report() throws IOException {
        Instant startTimestamp = cicdUtils.loadStartTimestampFromFile();
        List<CICDReportLine> reportLines = tektonCICDUtils.prepareReportLines(identifiers);
        cicdUtils.saveReport(new CICDReport(reportLines, startTimestamp));
    }

    @Test public void runJobs() {
        cicdUtils.saveStartTimestampToFile();
        identifiers.parallelStream().forEach( identifier ->
            tektonCICDUtils.createTaskRun(identifier)
        );
    }

    @Test public void removeJobs() {
        identifiers.parallelStream().forEach( identifier ->
            tektonCICDUtils.deleteTaskRun(identifier)
        );
    }

    @BeforeAll
    void setUp() {
        tektonCICDUtils = new TektonCICDUtils(RUN_ID, NUM_PARTS);
        cicdUtils = new CICDUtils(testName, RUN_ID, NUM_PARTS);
        identifiers = cicdUtils.generateIdentifiers();
    }

    @AfterAll
    void tearDown() {
        tektonCICDUtils.close();
    }

}
