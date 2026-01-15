package com.fosscut.compare.cicd;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.fosscut.compare.cicd.tekton.TektonCICDReportLine;
import com.fosscut.compare.cicd.tekton.TektonCICDReportMetadata;
import com.fosscut.compare.cicd.tekton.TektonCICDUtils;
import com.fosscut.shared.SharedDefaults;
import com.fosscut.utils.PerformanceDefaults;
import com.fosscut.utils.ResultsReport;

import io.fabric8.tekton.v1.TaskRun;
import io.fabric8.tekton.v1.TaskRunStatus;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TektonCompareTest {
    // DONE - run x tekton task runs
    // DONE - measure when each one starts and ends
    // DONE - should be able to run multiple such tasks in parallel
    // DONE - step for triggering task runs should be separate from step for measuring their status
    // DONE - create a report file that contains the list of all runs INPROGRESS/COMPLETED/FAILED with their start and end times
    // MAYBE - should be prepared for k8s to reject api calls when overloaded and retry them after some time

    private static String testName = "tektonCompareTest";
    private static String RUN_ID = "1"; // Run is a set of data, managed manually
    private static int NUM_PARTS = 200; // Number of task runs to create per run

    private TektonCICDUtils tektonCICDUtils;
    private List<String> identifiers;

    @Test public void report() {
        List<TektonCICDReportLine> reportLines = new ArrayList<>();

        for (String identifier : identifiers.reversed()) {
            String name = PerformanceDefaults.CICD_PERFORMANCE_TEKTON_TASK_RUN_NAME_PREFIX + identifier;
            TaskRun taskRun = tektonCICDUtils.getTektonClient().v1().taskRuns()
                .inNamespace(SharedDefaults.TEKTON_NAMESPACE)
                .withName(name)
                .get();

            String creationTimestamp = null;
            String completionTimestamp = null;

            assertNotNull(taskRun);
            if (taskRun.getMetadata() != null) {
                creationTimestamp = taskRun.getMetadata().getCreationTimestamp();
            }

            if (taskRun.getStatus() != null) {
                TaskRunStatus status = taskRun.getStatus();
                completionTimestamp = status.getCompletionTime();
            }
            assertNotNull(creationTimestamp);
            assertNotNull(completionTimestamp);

            reportLines.add(new TektonCICDReportLine(
                name,
                creationTimestamp == null ? null : Instant.parse(creationTimestamp),
                completionTimestamp == null ? null : Instant.parse(completionTimestamp)));
        }

        // prepare a report
        TektonCICDReportMetadata metadata = new TektonCICDReportMetadata(reportLines);

        String reportString = "";
        reportString += metadata.toString();
        reportString += "\n" + String.join("\n", reportLines.stream().map(Object::toString).toList());

        // save report to results folder
        ResultsReport.saveReportToFile(reportString, testName);
    }

    @Test public void runJobs() {
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
        identifiers = tektonCICDUtils.generateIdentifiers();
    }

    @AfterAll
    void tearDown() {
        tektonCICDUtils.close();
    }

}
