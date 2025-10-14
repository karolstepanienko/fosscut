package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.Utils;

public class OptimalGen {

    @Test public void optimalgenCommand() {
        Command command = new Command("optimalgen");
        command.run();
        assert(command.getError().contains("Usage"));
        assert(command.getError().contains("Missing required options: '--output"));
        if (Utils.isLinux()) assertEquals(2, command.getExitCode());
        if (Utils.isWindows()) assertEquals(1, command.getExitCode());
    }

    @Test public void shortHelp() {
        RepetitiveTests.testHelp(new Command("optimalgen -h"));
    }

    @Test public void longHelp() {
        RepetitiveTests.testHelp(new Command("optimalgen --help"));
    }

    @Test public void shortVersion() {
        RepetitiveTests.testVersion(new Command("optimalgen -v"));
    }

    @Test public void longVersion() {
        RepetitiveTests.testVersion(new Command("optimalgen --version"));
    }

    @Test public void optimalgenSingleInput() throws IOException {
        String testFileName = "optimalgenSingleInput";
        Command command = new Command("optimalgen -i 100 -ol 0.2 -ou 0.8 -oc 100 -ot 5 --seed 1 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.OPTIMAL_GEN_SINGLE_INPUT),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void optimalgenMultiInput() throws IOException {
        String testFileName = "optimalgenMultiInput";
        Command command = new Command("optimalgen -it 3 -il 100 -iu 120 -ol 0.2 -ou 0.8 -oc 100 -ot 10 --seed 1 -o " + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
        assertEquals(
            Utils.loadFile(TestDefaults.OPTIMAL_GEN_MULTI_INPUT),
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName)
        );
    }

    @Test public void optimalgenSingleInputRedis() throws IOException {
        String testFileName = "optimalgenSingleInputRedis";
        Command command = new Command("optimalgen -i 100 -ol 0.2 -ou 0.8 -oc 100 -ot 5 --seed 1"
            + " --redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + " -o " + TestDefaults.REDIS_URL + testFileName);
        command.run();
        assertEquals(0, command.getExitCode());
    }

}
