package com.fosscut.compare.solver.greedy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// DONE retest with collecting memory usage data
public class GreedyCompareSolverCBCOptimalgen3itTest extends GreedyCompareSolverOptimalgen3itPlot {

    private static String testName = "greedyCompareSolverCBCOptimalgen3it";
    private static String planCommand = "greedy --integer-solver CBC -in 1 --timeout-amount 5 --timeout-unit MINUTES";
    private static String cpu = "1";
    private static String memory = "3Gi"; // MEMORY SETTING READY

    /***************************** Results Report *****************************/

    @Test @Order(2) public void GreedyCompareSolverCBCOptimalgen3itResultsReport() {
        ResultsReport report = new ResultsReport(testName, new ArrayList<>(), seeds);
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx10() throws InterruptedException {
        String outputTypeCount = "10";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx20() throws InterruptedException {
        String outputTypeCount = "20";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx30() throws InterruptedException {
        String outputTypeCount = "30";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx40() throws InterruptedException {
        String outputTypeCount = "40";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx160() throws InterruptedException {
        String outputTypeCount = "160";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx170() throws InterruptedException {
        String outputTypeCount = "170";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx180() throws InterruptedException {
        String outputTypeCount = "180";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx190() throws InterruptedException {
        String outputTypeCount = "190";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

    @Test @Order(1) public void GreedyCompareSolverCBCOptimalgen3itx200() throws InterruptedException {
        String outputTypeCount = "200";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds));
    }

}
