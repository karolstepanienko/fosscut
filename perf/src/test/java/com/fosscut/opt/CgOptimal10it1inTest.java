package com.fosscut.opt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// Test generates 1 plan for each order (identified with it's seed)
// Since used SCIP is running with 1 CPU, results are deterministic
public class CgOptimal10it1inTest extends CgOptimal10itPlot {
    private static String testName = "CgOptimal10it1inTest";
    private static String planCommand = "cg --linear-solver CLP --integer-solver SCIP -ln 1 -in 1 --timeout-amount 3 --timeout-unit MINUTES";
    private static String cpu = "1";
    private static String memory = "5Gi"; // MEMORY SETTING READY

    // each order (identified with a seed) is ran only once

    @Test @Order(2) public void cgOptimal10it1inResultsReport() {
        ResultsReport report = new ResultsReport(testName,
            new ArrayList<>(List.of()),
            getAllSeeds()
        );
        report.generateReport();
    }

    @Test @Order(1) public void cgOptimal10it1in() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(getAllSeeds()));
    }

}
