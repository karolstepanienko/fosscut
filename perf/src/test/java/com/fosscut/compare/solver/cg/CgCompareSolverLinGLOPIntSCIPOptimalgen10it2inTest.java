package com.fosscut.compare.solver.cg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

public class CgCompareSolverLinGLOPIntSCIPOptimalgen10it2inTest extends CgCompareSolverOptimalgen10itPlot {

    private static String testName = "cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inTest";
    protected static String planCommand = "cg --linear-solver GLOP --integer-solver SCIP -ln 1 -in 2 --timeout-amount 5 --timeout-unit MINUTES";
    private static String cpu = "2";
    private static String memory = "5Gi";

    private static int N_RUNS_INIT = 1000; // larger than range to accommodate for future increases
    private static int N_RUNS_WITH_IDENTICAL_SEED_START = 1;
    private static int N_RUNS_WITH_IDENTICAL_SEED_END = 10;

    /***************************** Results Report *****************************/

    @Test @Order(2) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inResultsReport() {
        ResultsReport report = new ResultsReport(testName, getXAxisLabelSeedsMapOfLists(), N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END);
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(x50seedsLinkedList, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(x60seedsLinkedList, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(x70seedsLinkedList, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(x80seedsLinkedList, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(x90seedsLinkedList, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgCompareSolverLinGLOPIntSCIPOptimalgen10it2inx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand, cpu, memory
        );
        assertTrue(cmd.run(x100seedsLinkedList, N_RUNS_INIT, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    // tests for larger numbers of output types not done due to
    // a high chance of a timeout in GLOP-SCIP combination

}
