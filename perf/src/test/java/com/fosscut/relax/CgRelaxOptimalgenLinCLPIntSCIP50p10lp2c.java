package com.fosscut.relax;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

// DONE generate results
public class CgRelaxOptimalgenLinCLPIntSCIP50p10lp2c extends RelaxOptimalgenPlot {

    private static String testName = "cgRelaxOptimalgenLinCLPIntSCIP50p10lp2c";
    private static String orderCommandSuffix = " -otrp 50 -otlrp 10";
    private static String planCommand = "cg -r -c 2 --linear-solver CLP --integer-solver SCIP -ln 1 -in 1 --timeout-amount 5 --timeout-unit MINUTES";
    private static String cpu = "1";
    private static String memory = "9Gi"; // MEMORY SETTING READY

    /***************************** Results Report *****************************/

    @Test @Order(2) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cResultsReport() {
        ResultsReport report = new ResultsReport(testName, getXAxisLabelSeedsMap());
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx10() throws InterruptedException {
        String outputTypeCount = "10";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x10seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx20() throws InterruptedException {
        String outputTypeCount = "20";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x20seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx30() throws InterruptedException {
        String outputTypeCount = "30";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x30seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx40() throws InterruptedException {
        String outputTypeCount = "40";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x40seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x50seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x60seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x70seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x80seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x90seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x100seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x110seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x120seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x130seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x140seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x150seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx160() throws InterruptedException {
        String outputTypeCount = "160";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x160seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx170() throws InterruptedException {
        String outputTypeCount = "170";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x170seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx180() throws InterruptedException {
        String outputTypeCount = "180";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x180seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx190() throws InterruptedException {
        String outputTypeCount = "190";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x190seedsLinkedHashMap));
    }

    @Test @Order(1) public void cgRelaxOptimalgenLinCLPIntSCIP50p10lp2cx200() throws InterruptedException {
        String outputTypeCount = "200";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount, orderCommand + orderCommandSuffix + " -ot " + outputTypeCount, planCommand, cpu, memory);
        assertTrue(cmd.run(x200seedsLinkedHashMap));
    }

}
