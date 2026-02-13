package com.fosscut.relax;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// DONE generate results
// NOT USED IN fosscut-doc since c = 5 was very similar to c = 2, so not really worth plotting
public class WITHDRAWNGreedyRelaxOptimalgenIntSCIP50p10lp5c extends RelaxOptimalgenPlot {

    private static String testName = "greedyRelaxOptimalgenIntSCIP50p10lp5c";
    private static String orderCommandSuffix = " -otrp 50 -otlrp 10";
    private static String planCommand = "greedy -r -c 5 --integer-solver SCIP -in 1 --timeout-amount 5 --timeout-unit MINUTES";
    private static String cpu = "1";
    private static String memory = "27Gi"; // MEMORY SETTING READY

    /***************************** Results Report *****************************/

    @Test @Order(2) public void greedyRelaxOptimalgenIntSCIP50p10lp5cResultsReport() {
        ResultsReport report = new ResultsReport(testName, getXAxisLabelSeedsMap());
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx10() throws InterruptedException {
        String outputTypeCount = "10";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x10seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx20() throws InterruptedException {
        String outputTypeCount = "20";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x20seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx30() throws InterruptedException {
        String outputTypeCount = "30";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x30seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx40() throws InterruptedException {
        String outputTypeCount = "40";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x40seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x50seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x60seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x70seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x80seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x90seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x100seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x110seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x120seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x130seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x140seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x150seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx160() throws InterruptedException {
        String outputTypeCount = "160";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x160seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx170() throws InterruptedException {
        String outputTypeCount = "170";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x170seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx180() throws InterruptedException {
        String outputTypeCount = "180";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x180seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx190() throws InterruptedException {
        String outputTypeCount = "190";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x190seedsLinkedHashMap));
    }

    @Test @Order(1) public void greedyRelaxOptimalgenIntSCIP50p10lp5cx200() throws InterruptedException {
        String outputTypeCount = "200";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x200seedsLinkedHashMap));
    }

}
