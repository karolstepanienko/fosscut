package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fosscut.util.Command;
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

    @Test public void simpleOrder() {
        Command command = new Command("cutgen -i 1000 -ol 0.3 -ou 0.7 -ot 10 -d 10 --seed 1");
        command.run();
        assertEquals(0, command.getExitCode());
        assert(command.getOutput().contains("Generated order"));
        assert(command.getOutput().contains("inputs:"));
        assert(command.getOutput().contains("outputs:"));
        assert(command.getOutput().contains("- length: 688"));
    }

    @Test public void simpleOrderSaveToFile() throws IOException {
        String testFileName = "simpleOrderSaveToFile";
        Command command = new Command("cutgen -i 1000 -ol 0.3 -ou 0.7 -ot 10 -d 10 --seed 1 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_SIMPLE_ORDER),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void multiInputOrderSaveToFile() throws IOException {
        String testFileName = "multiInputOrderSaveToFile";
        Command command = new Command("cutgen -il 1000 -iu 1200 -it 3 -ol 0.3 -ou 0.7 -ot 10 -d 10 --seed 2 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_MULTI_INPUT_ORDER),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void throwInputDuplicates() {
        Command command = new Command("cutgen -il 1000 -iu 1001 -it 3 -ol 0.3 -ou 0.7 -ot 5 -d 10 --seed 1");
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains("Duplicates detected while generating input elements."));
        assert(command.getOutput().contains("Duplicates can be allowed using: '--allow-input-type-duplicates'."));
    }

    @Test public void allowInputDuplicates() throws IOException {
        String testFileName = "allowInputDuplicates";
        Command command = new Command("cutgen -il 1000 -iu 1001 -it 3 -ol 0.3 -ou 0.7 -ot 5 -d 10 --seed 1 -ai -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_ALLOW_INPUT_DUPLICATES),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void throwOutputDuplicates() {
        Command command = new Command("cutgen -i 20 -ol 0.4 -ou 0.6 -ot 5 -d 10 --seed 1");
        command.run();
        assertEquals(1, command.getExitCode());
        assert(command.getOutput().contains("Duplicates detected while generating output elements."));
        assert(command.getOutput().contains("Duplicates can be allowed using: '--allow-output-type-duplicates'."));
    }

    @Test public void allowOutputDuplicates() throws IOException {
        String testFileName = "allowOutputDuplicates";
        Command command = new Command("cutgen -i 20 -ol 0.4 -ou 0.6 -ot 5 -d 10 --seed 1 -ao -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.CUTGEN_ALLOW_OUTPUT_DUPLICATES),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

}
