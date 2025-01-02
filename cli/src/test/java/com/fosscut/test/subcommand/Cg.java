package com.fosscut.test.subcommand;

import java.io.IOException;

import org.junit.Test;

import com.fosscut.util.Command;
import com.fosscut.util.TestDefaults;
import com.fosscut.util.RepetitiveTests;
import com.fosscut.util.Utils;

public class Cg {
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

    @Test public void simpleCg() {
        Command command = new Command("cg " + Utils.getAbsolutePath(TestDefaults.SIMPLE_CG_ORDER));
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using column generation algorithm..."));
        assert(command.getOutput().contains("Status: OPTIMAL"));
    }

    @Test public void simpleCgQuiet() throws IOException {
        Command command = new Command("cg -q " + Utils.getAbsolutePath(TestDefaults.SIMPLE_CG_ORDER));
        command.run();
        assert(command.getOutput().equals(Utils.loadFile(TestDefaults.SIMPLE_CG_PLAN)));
    }

    @Test public void simpleCgRedis() {
        Command command = new Command("cg "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().contains("Running cutting plan generation using column generation algorithm..."));
        assert(command.getOutput().contains("Status: OPTIMAL"));
    }

    @Test public void simpleCgRedisQuiet() throws IOException {
        Command command = new Command("cg -q "
            + "--redis-connection-secrets " + Utils.getAbsolutePath(TestDefaults.EXAMPLE_REDIS_CONNECTION_SECRETS)
            + TestDefaults.REDIS_ORDER_PATH);
        command.run();
        assert(command.getOutput().equals(Utils.loadFile(TestDefaults.SIMPLE_CG_PLAN)));
    }
}
