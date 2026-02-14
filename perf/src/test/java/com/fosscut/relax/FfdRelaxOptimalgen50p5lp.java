package com.fosscut.relax;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// DONE generate results
public class FfdRelaxOptimalgen50p5lp extends RelaxOptimalgenPlot {

    private static String testName = "ffdRelaxOptimalgen50p5lp";
    private static String orderCommandSuffix = " -otrp 50 -otlrp 5";
    private static String planCommand = "ffd -r --timeout-amount 5 --timeout-unit MINUTES";
    // MEMORY SETTING UNNECESSARY FOR FFD since usage is very low

    /***************************** Results Report *****************************/

    @Test @Order(2) public void ffdRelaxOptimalgen50p5lpResultsReport() {
        ResultsReport report = new ResultsReport(testName, getXAxisLabelSeedsMap());
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx10() throws InterruptedException {
        String outputTypeCount = "10";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x10seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx20() throws InterruptedException {
        String outputTypeCount = "20";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x20seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx30() throws InterruptedException {
        String outputTypeCount = "30";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x30seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx40() throws InterruptedException {
        String outputTypeCount = "40";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x40seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x50seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x60seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x70seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x80seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x90seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x100seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x110seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x120seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x130seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x140seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x150seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx160() throws InterruptedException {
        String outputTypeCount = "160";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x160seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx170() throws InterruptedException {
        String outputTypeCount = "170";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x170seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx180() throws InterruptedException {
        String outputTypeCount = "180";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x180seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx190() throws InterruptedException {
        String outputTypeCount = "190";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x190seedsLinkedHashMap));
    }

    @Test @Order(1) public void ffdRelaxOptimalgen50p5lpx200() throws InterruptedException {
        String outputTypeCount = "200";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand);
        assertTrue(cmd.run(x200seedsLinkedHashMap));
    }

}
