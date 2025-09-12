package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fosscut.shared.util.SharedMessages;
import com.fosscut.util.Command;
import com.fosscut.util.Messages;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.Utils;

public class Greedy {

    /******************************* Command **********************************/

    @Test public void greedyCommand() {
        RepetitiveTests.testHelpWithOrderPath(new Command("greedy"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("greedy -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("greedy --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("greedy -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("greedy --version"));
    }

    /******************************* General **********************************/

    @Test public void greedy() {
        Command command = new Command("greedy "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void greedyQuiet() throws IOException {
        Command command = new Command("greedy -q "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
    }

    @Test public void greedySavePlanToFile() throws IOException {
        String testFileName = "greedySavePlanToFile";
        Command command = new Command("greedy -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    @Test public void greedyQuietSavePlanToFile() throws IOException {
        String testFileName = "greedyQuietSavePlanToFile";
        Command command = new Command("greedy -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    @Test public void greedyShortInputCountCostNullQuietSavePlanToFile() throws IOException {
        String testFileName = "greedyShortInputCountCostNullQuietSavePlanToFile";
        Command command = new Command("greedy -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_SHORT_INPUT_COUNT_COST_NULL_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.SHORT_INPUT_COUNT_COST_NULL_PLAN)
        );
    }

    /******************************** Redis ***********************************/

    @Test public void greedyRedis() {
        Command command = new Command("greedy "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void greedyRedisQuiet() throws IOException {
        Command command = new Command("greedy -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
    }

    /***************************** Relaxation *********************************/

    @Test public void greedyRelaxCost0QuietSavePlanToFile() throws IOException {
        String testFileName = "greedyRelaxCost0QuietSavePlanToFile";
        Command command = new Command("greedy -q -c 0 -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_RELAX_0_PLAN)
        );
    }

    @Test public void greedyRelaxCost1QuietSavePlanToFile() throws IOException {
        String testFileName = "greedyRelaxCost1QuietSavePlanToFile";
        Command command = new Command("greedy -q -c 1 -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_RELAX_1_PLAN)
        );
    }

    @Test public void greedyRelaxCost0RelaxDisabledQuietSavePlanToFile() throws IOException {
        String testFileName = "greedyRelaxCost0RelaxDisabledQuietSavePlanToFile";
        Command command = new Command("greedy -q -c 0 -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    /****************************** Solvers ***********************************/

    @Test public void greedySolverCBC() throws IOException {
        String testFileName = "greedySolverCBC";
        Command command = new Command("greedy --integer-solver CBC -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    @Test public void greedySolverSAT() throws IOException {
        String testFileName = "greedySolverSAT";
        Command command = new Command("greedy --integer-solver SAT -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_SAT_PLAN)
        );
    }

    @Test public void greedySolverSCIP() throws IOException {
        String testFileName = "greedySolverSCIP";
        Command command = new Command("greedy --integer-solver SCIP -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    /**************************** Input count *********************************/

    @Test public void greedyInputCount() throws IOException {
        String testFileName = "greedyInputCount";
        Command command = new Command("greedy -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_INPUT_COUNT_PLAN)
        );
    }

    @Test public void greedyInputCountZeros() throws IOException {
        String testFileName = "greedyInputCountZeros";
        Command command = new Command("greedy -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ZEROS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_CG_INPUT_COUNT_ZEROS_PLAN)
        );
    }

    @Test public void greedyInputCountExecutionError() {
        Command command = new Command("greedy "
            + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS));
    }

    @Test public void greedyInputCountExecutionErrorQuiet() {
        Command command = new Command("greedy -q "
            + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().equals(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS));
    }

    /******************************* Cost *************************************/

    @Test public void greedyCost() throws IOException {
        String testFileName = "greedyCost";
        Command command = new Command("greedy -q --optimization-criterion MIN_COST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COST_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_INPUT_COST_PLAN)
        );
    }

    @Test public void greedyNullCostException() {
        Command command = new Command("greedy --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains(SharedMessages.NULL_COST_EXCEPTION));
    }

    @Test public void greedyNullCostExceptionQuiet() {
        Command command = new Command("greedy -q --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().equals(SharedMessages.NULL_COST_EXCEPTION));
    }

}
