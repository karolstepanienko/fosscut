package com.fosscut.compare.solver.greedy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// not used in fosscut-doc since it is similar to GreedyCompareSolverSCIPOptimalgen3itTest
// probably orders are too simple for SCIP multithreaded to show any difference
public class GreedyCompareSolverSCIPOptimalgen3it2inTest extends GreedyCompareSolverOptimalgen3itPlot {

    private static String testName = "greedyCompareSolverSCIPOptimalgen3it2in";
    protected static String planCommand = "greedy --integer-solver SCIP -in 2 --timeout-amount 5 --timeout-unit MINUTES";
    private static String cpu = "2";
    private static String memory = "5Gi";

    private static int N_RUNS_INIT = 1000; // larger than range to accommodate for future increases
    private static int N_RUNS_WITH_IDENTICAL_SEED_START = 1;
    private static int N_RUNS_WITH_IDENTICAL_SEED_END = 10;

    /***************************** Results Report *****************************/

    @Test @Order(2) public void GreedyCompareSolverSCIPOptimalgen3it2inResultsReport() {
        ResultsReport report = new ResultsReport(testName, new ArrayList<>(), seeds,
            N_RUNS_INIT,
            N_RUNS_WITH_IDENTICAL_SEED_START,
            N_RUNS_WITH_IDENTICAL_SEED_END
        );
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx10() throws InterruptedException {
        String outputTypeCount = "10";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx20() throws InterruptedException {
        String outputTypeCount = "20";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx30() throws InterruptedException {
        String outputTypeCount = "30";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx40() throws InterruptedException {
        String outputTypeCount = "40";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx160() throws InterruptedException {
        String outputTypeCount = "160";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx170() throws InterruptedException {
        String outputTypeCount = "170";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx180() throws InterruptedException {
        String outputTypeCount = "180";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx190() throws InterruptedException {
        String outputTypeCount = "190";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void GreedyCompareSolverSCIPOptimalgen3it2inx200() throws InterruptedException {
        String outputTypeCount = "200";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(seeds, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

}
