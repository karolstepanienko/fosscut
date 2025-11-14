package com.fosscut.compare.solver.cg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.fosscut.utils.CloudCommand;
import com.fosscut.utils.ResultsReport;

public class CgCompareSolverLinCLPIntCBCOptimalgen10itTest extends CgCompareSolverOptimalgen10itPlot{

    private static String testName = "cgCompareSolverLinCLPIntCBCOptimalgen10itTest";
    protected static String planCommand = "cg --linear-solver CLP --integer-solver CBC -ln 1 -in 1 --timeout-amount 5 --timeout-unit MINUTES";

    /***************************** Results Report *****************************/

    @Test @Order(2) public void cgCompareSolverLinCLPIntCBCOptimalgen10itResultsReport() {
        ResultsReport report = new ResultsReport(testName, getXAxisLabelSeedsMap());
        report.generateReport();
    }

    /********************************* Tests **********************************/

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx50() throws InterruptedException {
        String outputTypeCount = "50";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x50seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx60() throws InterruptedException {
        String outputTypeCount = "60";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x60seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx70() throws InterruptedException {
        String outputTypeCount = "70";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x70seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx80() throws InterruptedException {
        String outputTypeCount = "80";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x80seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx90() throws InterruptedException {
        String outputTypeCount = "90";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x90seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx100() throws InterruptedException {
        String outputTypeCount = "100";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x100seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx110() throws InterruptedException {
        String outputTypeCount = "110";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x110seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx120() throws InterruptedException {
        String outputTypeCount = "120";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x120seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx130() throws InterruptedException {
        String outputTypeCount = "130";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x130seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx140() throws InterruptedException {
        String outputTypeCount = "140";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x140seeds));
    }

    @Test @Order(1) public void cgCompareSolverLinCLPIntCBCOptimalgen10itx150() throws InterruptedException {
        String outputTypeCount = "150";
        CloudCommand cmd = new CloudCommand(testName, "x" + outputTypeCount,
            orderCommand + " -ot " + outputTypeCount, planCommand
        );
        assertTrue(cmd.run(x150seeds));
    }

}
