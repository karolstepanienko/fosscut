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

public class CG {

    /******************************* Command **********************************/

    @Test public void cgCommand() {
        RepetitiveTests.testHelpWithOrderPath(new Command("cg"));
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("cg -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("cg --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("cg -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("cg --version"));
    }

    /******************************* General **********************************/

    @Test public void cg() {
        Command command = new Command("cg " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a column generation algorithm..."));
        assert(command.getOutput().contains("Status: OPTIMAL"));
    }

    @Test public void cgQuiet() throws IOException {
        Command command = new Command("cg -q " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
    }

    @Test public void cgSavePlanToFile() throws IOException {
        String testFileName = "cgSavePlanToFile";
        Command command = new Command("cg -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a column generation algorithm..."));
        assert(command.getOutput().contains("Status: OPTIMAL"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_CLP_GLOP_SCIP_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgQuietSavePlanToFile() throws IOException {
        String testFileName = "cgQuietSavePlanToFile";
        Command command = new Command("cg -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_CLP_GLOP_SCIP_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgShortInputCountCostNullQuietSavePlanToFile() throws IOException {
        String testFileName = "cgShortInputCountCostNullQuietSavePlanToFile";
        Command command = new Command("cg -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_SHORT_INPUT_COUNT_COST_NULL_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.SHORT_INPUT_COUNT_COST_NULL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgForceLinearImprovementQuietSavePlanToFile() throws IOException {
        String testFileName = "cgForceLinearImprovementSavePlanToFile";
        Command command = new Command("cg -fli -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_FORCE_LINEAR_IMPROVEMENT_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /******************************** Redis ***********************************/

    @Test public void cgRedis() {
        Command command = new Command("cg "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Running cutting plan generation using a column generation algorithm..."));
        assert(command.getOutput().contains("Status: OPTIMAL"));
    }

    @Test public void cgRedisQuiet() throws IOException {
        Command command = new Command("cg -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
    }

    /***************************** Relaxation *********************************/

    @Test public void cgRelaxCost0QuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost0QuietSavePlanToFile";
        Command command = new Command("cg -q -c 0 -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_RELAX_0_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgRelaxCost1QuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost1QuietSavePlanToFile";
        Command command = new Command("cg -q -c 1 -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_RELAX_1_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /************************** Multi relaxation ******************************/

    @Test public void cgMultiRelaxCost1QuietSavePlanToFile() throws IOException {
        String testFileName = "cgMultiRelaxCost1QuietSavePlanToFile";
        Command command = new Command("cg -q -c 1 -r -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_MULTI_RELAX_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_MULTI_RELAX_1_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgRelaxCost0RelaxDisabledQuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost0RelaxDisabledQuietSavePlanToFile";
        Command command = new Command("cg -q -c 0 -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_CLP_GLOP_SCIP_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /************************ Relaxation strategies ***************************/

    @Test public void cgRelaxCost01StratEqualRelaxQuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost01StratEqualRelaxQuietSavePlanToFile";
        Command command = new Command("cg -q -r -c 0.1 --relaxation-spread-strategy EQUAL_RELAX -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgRelaxCost01StratEqualSpaceQuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost01StratEqualSpaceQuietSavePlanToFile";
        Command command = new Command("cg -q -r -c 0.1 --relaxation-spread-strategy EQUAL_SPACE -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_EQUAL_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgRelaxCost01StratStartQuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost01StratStartQuietSavePlanToFile";
        Command command = new Command("cg -q -r -c 0.1 --relaxation-spread-strategy START -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_START_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgRelaxCost01StratEndQuietSavePlanToFile() throws IOException {
        String testFileName = "cgRelaxCost01StratEndQuietSavePlanToFile";
        Command command = new Command("cg -q -r -c 0.1 --relaxation-spread-strategy END -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_RELAX_STRATEGIES_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.RELAX_END_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /****************************** Solvers ***********************************/

    @Test public void cgLinearSolverCLPIntegerSolverCBC() throws IOException {
        String testFileName = "cgLinearSolverCLPIntegerSolverCBC";
        Command command = new Command("cg --linear-solver CLP --integer-solver CBC -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_CLP_CBC_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgLinearSolverCLPIntegerSolverSAT() throws IOException {
        String testFileName = "cgLinearSolverCLPIntegerSolverSAT";
        Command command = new Command("cg --linear-solver CLP --integer-solver SAT -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));

        // SAT solver is nondeterministic, it can randomly generate three different cutting plans
        String result = Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName);
        assert(
            result.equals(Utils.loadFile(TestDefaults.CG_CLP_GLOP_SAT_1_PLAN))
            || result.equals(Utils.loadFile(TestDefaults.CG_CLP_GLOP_SAT_2_PLAN))
            || result.equals(Utils.loadFile(TestDefaults.CG_CLP_GLOP_SAT_3_PLAN))
        );
    }

    @Test public void cgLinearSolverCLPIntegerSolverSCIP() throws IOException {
        String testFileName = "cgLinearSolverCLPIntegerSolverSCIP";
        Command command = new Command("cg --linear-solver CLP --integer-solver SCIP -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_CLP_GLOP_SCIP_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgLinearSolverGLOPIntegerSolverCBC() throws IOException {
        String testFileName = "cgLinearSolverGLOPIntegerSolverCBC";
        Command command = new Command("cg --linear-solver GLOP --integer-solver CBC -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_GLOP_CBC_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgLinearSolverGLOPIntegerSolverSAT() throws IOException {
        String testFileName = "cgLinearSolverGLOPIntegerSolverSAT";
        Command command = new Command("cg --linear-solver GLOP --integer-solver SAT -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));

        // SAT solver is nondeterministic, it can randomly generate three different cutting plans
        String result = Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName);
        assert(
            result.equals(Utils.loadFile(TestDefaults.CG_CLP_GLOP_SAT_1_PLAN))
            || result.equals(Utils.loadFile(TestDefaults.CG_CLP_GLOP_SAT_2_PLAN))
            || result.equals(Utils.loadFile(TestDefaults.CG_CLP_GLOP_SAT_3_PLAN))
        );
    }

    @Test public void cgLinearSolverGLOPIntegerSolverSCIP() throws IOException {
        String testFileName = "cgLinearSolverGLOPIntegerSolverSCIP";
        Command command = new Command("cg --linear-solver GLOP --integer-solver SCIP -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_CLP_GLOP_SCIP_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgLinearSolverPDLPIntegerSolverCBC() throws IOException {
        String testFileName = "cgLinearSolverPDLPIntegerSolverCBC";
        Command command = new Command("cg --linear-solver PDLP --integer-solver CBC -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_PDLP_CBC_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgLinearSolverPDLPIntegerSolverSAT() throws IOException {
        String testFileName = "cgLinearSolverPDLPIntegerSolverSAT";
        Command command = new Command("cg --linear-solver PDLP --integer-solver SAT -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));

        // SAT solver is nondeterministic, it can randomly generate two different cutting plans
        String result = Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName);
        assert(
            result.equals(Utils.loadFile(TestDefaults.CG_PDLP_SAT_1_PLAN))
            || result.equals(Utils.loadFile(TestDefaults.CG_PDLP_SAT_2_PLAN))
        );
    }

    @Test public void cgLinearSolverPDLPIntegerSolverSCIP() throws IOException {
        String testFileName = "cgLinearSolverPDLPIntegerSolverSCIP";
        Command command = new Command("cg --linear-solver PDLP --integer-solver SCIP -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_PDLP_SCIP_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    /**************************** Input count *********************************/

    @Test public void cgInputCount() throws IOException {
        String testFileName = "cgInputCount";
        Command command = new Command("cg -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_INPUT_COUNT_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgInputCountZeros() throws IOException {
        String testFileName = "cgInputCountZeros";
        Command command = new Command("cg -q -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COUNT_ZEROS_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.GREEDY_CG_INPUT_COUNT_ZEROS_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgInputCountExecutionError() {
        Command command = new Command("cg " + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains(Messages.LP_UNFEASIBLE_EXCEPTION));
    }

    @Test public void cgInputCountExecutionErrorQuiet() {
        Command command = new Command("cg -q " + Utils.getAbsolutePath(TestDefaults.FAIL_EXECUTION_INPUT_COUNT));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().equals(Messages.LP_UNFEASIBLE_EXCEPTION));
    }

    /******************************* Cost *************************************/

    @Test public void cgCost() throws IOException {
        String testFileName = "cgCost";
        Command command = new Command("cg -q --optimization-criterion MIN_COST -o " + testFileName + " "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_INPUT_COST_ORDER));
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.CG_INPUT_COST_PLAN),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cgNullCostException() {
        Command command = new Command("cg --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains(SharedMessages.NULL_COST_EXCEPTION));
    }

    @Test public void cgNullCostExceptionQuiet() {
        Command command = new Command("cg -q --optimization-criterion MIN_COST "
            + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().equals(SharedMessages.NULL_COST_EXCEPTION));
    }

}
