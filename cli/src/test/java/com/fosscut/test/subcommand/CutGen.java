package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.Messages;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.Utils;

public class CutGen {

    @Test public void cutgenCommand() {
        Command command = new Command("cutgen");
        command.run();
        assert(command.getError().contains("Usage"));
        assert(command.getError().contains("Missing required options: '--output"));
        if (Utils.isLinux()) assertEquals(2, command.getExitCode());
        if (Utils.isWindows()) assertEquals(1, command.getExitCode());
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("cutgen -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("cutgen --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("cutgen -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("cutgen --version"));
    }

    @Test public void timeout() {
        RepetitiveTests.testTimeout(
            new Command("cutgen -i 999999999 -ol 0.1 -ou 0.9 -ot 19999 -oc 999999999 --seed 1 --timeout-amount 1 --timeout-unit NANOSECONDS"),
            Messages.ORDER_GENERATION_TIMEOUT
        );
    }

    @Test public void cutgenSimpleOrder() {
        Command command = new Command("cutgen -i 1000 -ol 0.3 -ou 0.7 -ot 10 -oc 100 --seed 1");
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Generated order"));
        assert(command.getOutput().contains("inputs:"));
        assert(command.getOutput().contains("outputs:"));
        assert(command.getOutput().contains("- length: 688"));
    }

    @Test public void cutgenSimpleOrderSaveToFile() throws IOException {
        String testFileName = "cutgenSimpleOrderSaveToFile";
        Command command = new Command("cutgen -i 1000 -ol 0.3 -ou 0.7 -ot 10 -oc 100 --seed 1 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_SIMPLE_ORDER),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cutgenSimpleOrderRedis() {
        String testFileName = "cutgenSimpleOrderRedis";
        Command command = new Command("cutgen -i 1000 -ol 0.3 -ou 0.7 -ot 10 -oc 100 --seed 1 "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + " -o " + TestDefaults.REDIS_URL + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
    }

    @Test public void cutgenMultiInputOrderSaveToFile() throws IOException {
        String testFileName = "cutgenMultiInputOrderSaveToFile";
        Command command = new Command("cutgen -il 1000 -iu 1200 -it 3 -ol 0.3 -ou 0.7 -ot 10 -oc 100 --seed 2 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_MULTI_INPUT_ORDER),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    //***************************** Duplicates ********************************/

    @Test public void cutgenThrowInputDuplicates() {
        Command command = new Command("cutgen -il 1000 -iu 1001 -it 3 -ol 0.3 -ou 0.7 -ot 5 -oc 50 --seed 1");
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains("Duplicates detected while generating input elements."));
        assert(command.getError().contains("Duplicates can be allowed using: '--allow-input-type-duplicates'."));
    }

    @Test public void cutgenAllowInputDuplicates() throws IOException {
        String testFileName = "cutgenAllowInputDuplicates";
        Command command = new Command("cutgen -il 1000 -iu 1001 -it 3 -ol 0.3 -ou 0.7 -ot 5 -oc 50 --seed 1 -ai -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_ALLOW_INPUT_DUPLICATES),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cutgenThrowOutputDuplicates() {
        Command command = new Command("cutgen -i 20 -ol 0.4 -ou 0.6 -ot 5 -oc 50 --seed 1");
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains("Duplicates detected while generating output elements."));
        assert(command.getError().contains("Duplicates can be allowed using: '--allow-output-type-duplicates'."));
    }

    @Test public void cutgenAllowOutputDuplicates() throws IOException {
        String testFileName = "cutgenAllowOutputDuplicates";
        Command command = new Command("cutgen -i 20 -ol 0.4 -ou 0.6 -ot 5 -oc 50 --seed 1 -ao -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_ALLOW_OUTPUT_DUPLICATES),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cutgenRelax() throws IOException {
        String testFileName = "cutgenRelax";
        Command command = new Command("cutgen -i 100 -ol 0.2 -ou 0.8 -oc 100 -ot 5 --seed 1 -otrp 20 -otlrp 10 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_RELAX),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void cutgenRelaxNullError() throws IOException {
        String testFileName = "cutgenRelaxNullError";
        Command command = new Command("cutgen -i 100 -ol 0.2 -ou 0.8 -oc 100 -ot 5 --seed 1 -otrp 1 -o " + testFileName);
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains(Messages.RELAX_APPLY_NULL_ERROR));
    }

    @Test public void cutgenRelaxPercentageError() throws IOException {
        String testFileName = "cutgenRelaxPercentageError";
        Command command = new Command("cutgen -i 100 -ol 0.2 -ou 0.8 -oc 100 -ot 5 --seed 1 -otrp 10 -otlrp 110 -o " + testFileName);
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains(Messages.RELAX_APPLY_PERCENTAGE_ERROR));
    }

    @Test public void cutgenNoRelaxAppliedError() throws IOException {
        String testFileName = "cutgenNoRelaxAppliedError";
        Command command = new Command("cutgen -i 100 -ol 0.2 -ou 0.8 -oc 100 -ot 5 --seed 1 -otrp 1 -otlrp 1 -o " + testFileName);
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getError().contains(Messages.RELAX_APPLY_NO_RELAX_APPLIED));
    }

}
