package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.Utils;

public class FFD {

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

    @Test public void simpleFfd() {
        Command command = new Command("ffd " + Utils.getAbsolutePath(TestDefaults.SIMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void simpleFfdQuiet() throws IOException {
        Command command = new Command("ffd -q " + Utils.getAbsolutePath(TestDefaults.SIMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
    }

    @Test public void simpleFfdSavePlanToFile() throws IOException {
        String testFileName = "simpleFfdSavePlanToFile";
        Command command = new Command("ffd -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.SIMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.SIMPLE_FFD_PLAN)
        );
    }

    @Test public void simpleFfdQuietSavePlanToFile() throws IOException {
        String testFileName = "simpleFfdQuietSavePlanToFile";
        Command command = new Command("ffd -q -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.SIMPLE_ORDER));
        command.run();
        assert(!command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(!command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.SIMPLE_FFD_PLAN)
        );
    }

    @Test public void simpleFfdRedis() {
        Command command = new Command("ffd "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a first-fit-decreasing algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void simpleFfdRedisQuiet() throws IOException {
        Command command = new Command("ffd -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().equals(""));
    }

}
