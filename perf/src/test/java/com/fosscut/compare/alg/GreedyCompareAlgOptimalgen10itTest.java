package com.fosscut.compare.alg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

public class GreedyCompareAlgOptimalgen10itTest extends CompareAlgOptimalgen10itPlot {

    private static String testName = "greedyCompareAlgOptimalgen10itTest";
    // Since FFD is only single-threaded, we use only one thread everywhere
    protected static String planCommand = "greedy --integer-solver SCIP -in 1 --timeout-amount 5 --timeout-unit MINUTES";

    /***************************** Results Report *****************************/

    @Test @Order(2) public void greedyCompareAlgOptimalgen10itResultsReport() {
        ResultsReport report = new ResultsReport(testName, getXAxisLabelSeedsMap());
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx10() throws InterruptedException {
        String outputTypeCount = "10";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x10seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx20() throws InterruptedException {
        String outputTypeCount = "20";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x20seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx30() throws InterruptedException {
        String outputTypeCount = "30";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x30seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx40() throws InterruptedException {
        String outputTypeCount = "40";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x40seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x50seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x60seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x70seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x80seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x90seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x100seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x110seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x120seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x130seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x140seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x150seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx160() throws InterruptedException {
        String outputTypeCount = "160";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x160seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx170() throws InterruptedException {
        String outputTypeCount = "170";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x170seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx180() throws InterruptedException {
        String outputTypeCount = "180";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x180seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx190() throws InterruptedException {
        String outputTypeCount = "190";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x190seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyCompareAlgOptimalgen10itx200() throws InterruptedException {
        String outputTypeCount = "200";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x200seedsLinkedHashMap));
    }

}
