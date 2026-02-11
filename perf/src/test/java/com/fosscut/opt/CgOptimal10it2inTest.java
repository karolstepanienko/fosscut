package com.fosscut.opt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// Test generates 100 plans (identifies with run number)
// for each order (identified with it's seed)
// Since used SCIP is running with 2 CPUs and 7Gi memory,
// results are non-deterministic and some of them will be better/worse
// than others for the same order seed
// Then we can filter out how many seeds were solved optimally
// and determine how many times each order had to be solved to finally
// generate an optimal solution
public class CgOptimal10it2inTest extends CgOptimal10itPlot {
    private static String testName = "CgOptimal10it2inTest";
    private static String planCommand = "cg --linear-solver CLP --integer-solver SCIP -ln 1 -in 2 --timeout-amount 3 --timeout-unit MINUTES";
    private static String cpu = "2";
    private static String memory = "9Gi"; // MEMORY SETTING READY

    // each order (identified with a seed) is ran this amount of times
    private static int N_RUNS_INIT = 1000; // larger than range to accommodate for future increases
    private static int N_RUNS_STEP = 1000;
    private static int N_RUNS_WITH_IDENTICAL_SEED_START = 1;
    private static int N_RUNS_WITH_IDENTICAL_SEED_END = 100;

    @Test @Order(2) public void cgOptimal10it2inResultsReport() {
        ResultsReport report = new ResultsReport(testName,
            new ArrayList<>(List.of()),
            getAllSeeds(), N_RUNS_INIT, N_RUNS_STEP,
            N_RUNS_WITH_IDENTICAL_SEED_START,
            N_RUNS_WITH_IDENTICAL_SEED_END
        );
        report.generateReport();
    }

    @Test @Order(1) public void cgOptimal10it2inA() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsA, N_RUNS_INIT + 0 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inB() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsB, N_RUNS_INIT + 1 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inC() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsC, N_RUNS_INIT + 2 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inD() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsD, N_RUNS_INIT + 3 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inE() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsE, N_RUNS_INIT + 4 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inF() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsF, N_RUNS_INIT + 5 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inG() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsG, N_RUNS_INIT + 6 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inH() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsH, N_RUNS_INIT + 7 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inI() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsI, N_RUNS_INIT + 8 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inJ() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsJ, N_RUNS_INIT + 9 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inK() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsK, N_RUNS_INIT + 10 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inL() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsL, N_RUNS_INIT + 11 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inM() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsM, N_RUNS_INIT + 12 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inN() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsN, N_RUNS_INIT + 13 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inO() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsO, N_RUNS_INIT + 14 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inP() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsP, N_RUNS_INIT + 15 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inQ() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsQ, N_RUNS_INIT + 16 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inR() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsR, N_RUNS_INIT + 17 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inS() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsS, N_RUNS_INIT + 18 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inT() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsT, N_RUNS_INIT + 19 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inU() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsU, N_RUNS_INIT + 20 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inV() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsV, N_RUNS_INIT + 21 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inW() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsW, N_RUNS_INIT + 22 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inX() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsX, N_RUNS_INIT + 23 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

    @Test @Order(1) public void cgOptimal10it2inY() throws InterruptedException {
        CloudCommand cmd = new CloudCommand(testName, xAxisLabel,
            orderCommand, planCommand, cpu, memory, false);
        assertTrue(cmd.run(seedsY, N_RUNS_INIT + 24 * N_GROUP_SIZE * N_RUNS_INIT, N_RUNS_STEP, N_RUNS_WITH_IDENTICAL_SEED_START, N_RUNS_WITH_IDENTICAL_SEED_END));
    }

}

