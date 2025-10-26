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

    @Test public void timeout() {
        RepetitiveTests.testTimeout(
            new Command("ffd " + " --timeout-amount 1 --timeout-unit NANOSECONDS " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER)),
            Messages.PLAN_GENERATION_TIMEOUT
        );
    }

    /******************************* General **********************************/

    @Test public void ffd() {
        Command command = new Command("ffd "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void ffdQuiet() throws IOException {
        Command command = new Command("ffd -q "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
    }

    @Test public void ffdSavePlanToFile() throws IOException {
        String testFileName = "ffdSavePlanToFile";
        Command command = new Command("ffd -d -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdWithoutRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdWithoutRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_WITHOUT_RELAX_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdComplexPatternQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdComplexPatternQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdShortInputCountCostNullQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdShortInputCountCostNullQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_SHORT_INPUT_COUNT_COST_NULL_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.SHORT_INPUT_COUNT_COST_NULL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdElapsedTimeQuietSavePlanToFile() throws IOException {
        Command command = new Command("ffd "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("elapsedTimeMilliseconds:"));
    }

    /******************************** Redis ***********************************/

    @Test public void ffdRedis() {
        Command command = new Command("ffd "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void ffdRedisQuiet() throws IOException {
        Command command = new Command("ffd -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
    }

    /***************************** Relaxation *********************************/

    @Test public void ffdDefaultRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdDefaultRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_DEFAULT_RELAX_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /************************ Relaxation strategies ***************************/

    @Test public void ffdRelaxStratEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratEqualRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxStratEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratEqualSpaceQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxStratStartQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratStartQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy START -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_START_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxStratEndQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratEndQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy END -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_END_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /*** Relaxation strategies: all relax strategies produce different plans **/

    @Test public void ffdAllRelaxStrategiesDifferentPlansEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdAllRelaxStrategiesDifferentPlansEqualRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_EQUAL_RELAX_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdAllRelaxStrategiesDifferentPlansEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdAllRelaxStrategiesDifferentPlansEqualSpaceQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_EQUAL_SPACE_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdAllRelaxStrategiesDifferentPlansStartQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdAllRelaxStrategiesDifferentPlansStartQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy START -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_START_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdAllRelaxStrategiesDifferentPlansEndQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdAllRelaxStrategiesDifferentPlansEndQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy END -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_ALL_RELAX_STRATEGIES_DIFFERENT_PLANS_END_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /***************** Relaxation strategies: standard plan *******************/

    @Test public void ffdRelaxStandardPlanStratEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStandardPlanStratEqualRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_RELAX_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxStratStandardPlanEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratStandardPlanEqualSpaceQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_RELAX_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxStratStandardPlanStartQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratStandardPlanStartQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy START -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_RELAX_START_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxStratStandardPlanEndQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxStratStandardPlanEndQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy END -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_RELAX_END_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /***************** Relaxation strategies: complex plan ********************/

    @Test public void ffdRelaxComplexPatternStratEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxComplexPatternStratEqualRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxComplexPatternStratEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxComplexPatternStratEqualSpaceQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxComplexPatternStratStartQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxComplexPatternStratStartQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy START -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_START_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdRelaxComplexPatternStratEndQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdRelaxComplexPatternStratEndQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy END -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_COMPLEX_PATTERN_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_COMPLEX_PATTERN_END_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /*************** Relaxation strategies: multi-relax plan ******************/

    @Test public void ffdMultiRelaxStratEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdMultiRelaxStratEqualRelaxQuietSavePlanToFile";
       Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_MULTI_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_MULTI_RELAX_EQUAL_RELAX_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdMultiRelaxStratEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdMultiRelaxStratEqualSpaceQuietSavePlanToFile";
       Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_MULTI_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_MULTI_RELAX_EQUAL_SPACE_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdLargeMultiRelaxStratEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdLargeMultiRelaxStratEqualRelaxQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_LARGE_MULTI_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_LARGE_MULTI_RELAX_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdLargeMultiRelaxStratEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "ffdLargeMultiRelaxStratEqualSpaceQuietSavePlanToFile";
        Command command = new Command("ffd -d -q -r --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_FFD_LARGE_MULTI_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_LARGE_MULTI_RELAX_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /**************************** Input count *********************************/

    @Test public void ffdInputCount() throws IOException {
        String testFileName = "ffdInputCount";
        Command command = new Command("ffd -d -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_INPUT_COUNT_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdInputCountZeros() throws IOException {
        String testFileName = "ffdInputCountZeros";
        Command command = new Command("ffd -d -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ZEROS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_INPUT_COUNT_ZEROS_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdInputCountExecutionError() {
        Command command = new Command("ffd "
            + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS));
    }

    @Test public void ffdInputCountExecutionErrorQuiet() {
        Command command = new Command("ffd -q "
            + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains(Messages.UNABLE_TO_GENERATE_NEW_PATTERNS));
    }

    /******************************* Cost *************************************/

    @Test public void ffdCost() throws IOException {
        String testFileName = "ffdCost";
        Command command = new Command("ffd -d -q --optimization-criterion MIN_COST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COST_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FFD_INPUT_COST_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void ffdNullCostException() {
        Command command = new Command("ffd --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains(SharedMessages.NULL_COST_EXCEPTION));
    }

    @Test public void ffdNullCostExceptionQuiet() {
        Command command = new Command("ffd -q --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().equals(SharedMessages.NULL_COST_EXCEPTION));
    }

}
