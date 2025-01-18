package com.fosscut.test.subcommand;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.Utils;

public class Greedy {

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

    @Test public void simpleGreedy() {
        Command command = new Command("greedy " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void simpleGreedyQuiet() throws IOException {
        Command command = new Command("greedy -q " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
    }

    @Test public void simpleGreedySavePlanToFile() throws IOException {
        String testFileName = "simpleGreedySavePlanToFile";
        Command command = new Command("greedy -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    @Test public void simpleGreedyQuietSavePlanToFile() throws IOException {
        String testFileName = "simpleGreedyQuietSavePlanToFile";
        Command command = new Command("greedy -q -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(!command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(!command.getOutput().contains("Order demands"));
        assert(!command.getOutput().contains("Generated cutting plan:"));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_PLAN)
        );
    }

    @Test public void simpleGreedyRedis() {
        Command command = new Command("greedy "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using a greedy algorithm..."));
        assert(command.getOutput().contains("Order demands"));
    }

    @Test public void simpleGreedyRedisQuiet() throws IOException {
        Command command = new Command("greedy -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().equals(""));
    }

    @Test public void simpleGreedyRelaxCost0QuietSavePlanToFile() throws IOException {
        String testFileName = "simpleGreedyRelaxCost0QuietSavePlanToFile";
        Command command = new Command("greedy -q -c 0 -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_RELAX_0_PLAN)
        );
    }

    @Test public void simpleGreedyRelaxCost1QuietSavePlanToFile() throws IOException {
        String testFileName = "simpleGreedyRelaxCost1QuietSavePlanToFile";
        Command command = new Command("greedy -q -c 1 -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_RELAX_1_PLAN)
        );
    }

    @Test public void simpleGreedyIntegerRelaxCost0QuietSavePlanToFile() throws IOException {
        String testFileName = "simpleGreedyIntegerRelaxCost0QuietSavePlanToFile";
        Command command = new Command("greedy -q -c 0 -i -o " + testFileName + " " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_ORDER));
        command.run();
        assert(command.getOutput().equals(""));
        assertEquals(
            Utils.loadFile(TestDefaults.FOSSCUT_BINARY_FOLDER_PATH + File.separator + testFileName),
            Utils.loadFile(TestDefaults.GREEDY_INT_RELAX_0_PLAN)
        );
    }

}
