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

public class FFD {

    /******************************* Command **********************************/

    @Test public void ffdCommand() {
        RepetitiveTests.testHelpWithOrderPath(new Command("ffd"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("ffd -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("ffd --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("ffd -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("ffd --version"));
    }

    /******************************* General **********************************/

    @Test public void ffd() {
        Command command = new Command("ffd "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void ffdQuiet() throws IOException {
        Command command = new Command("ffd -q "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
    }

    @Test public void ffdSavePlanToFile() throws IOException {
        String testFileName = "ffdSavePlanToFile";
        Command command = new Command("ffd -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_PLAN)
        );
    }

    @Test public void ffdQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdQuietSavePlanToFile";
        Command command = new Command("ffd -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_PLAN)
        );
    }

    @Test public void ffdWithoutRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdWithoutRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_WITHOUT_RELAX_PLAN)
        );
    }

    @Test public void ffdComplexPatternQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdComplexPatternQuietSavePlanToFile";
        Command command = new Command("ffd -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_PLAN)
        );
    }

    /******************************** Redis ***********************************/

    @Test public void ffdRedis() {
        Command command = new Command("ffd "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void ffdRedisQuiet() throws IOException {
        Command command = new Command("ffd -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().equals(""));
    }

    /***************************** Relaxation *********************************/

    @Test public void ffdDefaultRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdDefaultRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_DEFAULT_RELAX_PLAN)
        );
    }

    @Test public void ffdDefaultIntegerRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdDefaultIntegerRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_DEFAULT_INT_RELAX_PLAN)
        );
    }

    @Test public void ffdIntegerRelaxStratEqualQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdIntegerRelaxStratEqualQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i --relaxation-spread-strategy EQUAL -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_RELAX_EQUAL_PLAN)
        );
    }

    @Test public void ffdIntegerRelaxStratLongestQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdIntegerRelaxStratLongestQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i --relaxation-spread-strategy LONGEST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_RELAX_LONGEST_PLAN)
        );
    }

    @Test public void ffdIntegerRelaxStratShortestQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdIntegerRelaxStratShortestQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i --relaxation-spread-strategy SHORTEST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_RELAX_SHORTEST_PLAN)
        );
    }

    @Test public void ffdIntegerRelaxComplexPatternStratEqualQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdIntegerRelaxComplexPatternStratEqualQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i --relaxation-spread-strategy EQUAL -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_EQUAL_PLAN)
        );
    }

    @Test public void ffdIntegerRelaxComplexPatternStratLongestQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdIntegerRelaxComplexPatternStratLongestQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i --relaxation-spread-strategy LONGEST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_LONGEST_PLAN)
        );
    }

    @Test public void ffdIntegerRelaxComplexPatternStratShortestQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdIntegerRelaxComplexPatternStratShortestQuietSavePlanToFile";
        Command command = new Command("ffd -q -r -i --relaxation-spread-strategy SHORTEST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_SHORTEST_PLAN)
        );
    }

    /**************************** Input count *********************************/

    @Test public void ffdInputCount() throws IOException {
        String testFileName = "ffdInputCount";
        Command command = new Command("ffd -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_INPUT_COUNT_PLAN)
        );
    }

    @Test public void ffdInputCountZeros() throws IOException {
        String testFileName = "ffdInputCountZeros";
        Command command = new Command("ffd -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ZEROS_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_INPUT_COUNT_ZEROS_PLAN)
        );
    }

    @Test public void ffdInputCountExecutionError() {
        Command command = new Command("ffd "
            + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assert(command.getOutput().contains(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS));
        assertEquals(1, command.getExitCode());
    }

    @Test public void ffdInputCountExecutionErrorQuiet() {
        Command command = new Command("ffd -q "
            + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assert(command.getOutput().equals(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS));
        assertEquals(1, command.getExitCode());
    }

    /******************************* Cost *************************************/

    @Test public void ffdCost() throws IOException {
        String testFileName = "ffdCost";
        Command command = new Command("ffd -q --optimization-criterion MIN_COST -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COST_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.FFD_INPUT_COST_PLAN)
        );
    }

    @Test public void ffdNullCostException() {
        Command command = new Command("ffd --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains(SharedMessages.NULL_COST_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

    @Test public void ffdNullCostExceptionQuiet() {
        Command command = new Command("ffd -q --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(SharedMessages.NULL_COST_EXCEPTION));
        assertEquals(1, command.getExitCode());
    }

}
